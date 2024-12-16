CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE types
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    user_id         VARCHAR(255),
    max_members     INT         NOT NULL DEFAULT 2,
    default_role_id BIGINT
);

INSERT INTO types (name, max_members)
VALUES ('PRIVATE', 2);
INSERT INTO types (name, max_members)
VALUES ('PUBLIC', 50);

CREATE TABLE chats
(
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    default_type_id BIGINT       NOT NULL
);

CREATE OR REPLACE FUNCTION set_default_type_id()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.default_type_id IS NULL THEN
        NEW.default_type_id := (SELECT id FROM types WHERE name = 'PRIVATE' LIMIT 1);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_default_type_id
    BEFORE INSERT
    ON chats
    FOR EACH ROW
EXECUTE FUNCTION set_default_type_id();

CREATE TABLE roles
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(20) NOT NULL,
    chat_id    BIGINT      NOT NULL,
    can_write  BOOLEAN     NOT NULL DEFAULT true,
    can_read   BOOLEAN     NOT NULL DEFAULT true,
    can_delete BOOLEAN     NOT NULL DEFAULT true,
    can_invite BOOLEAN     NOT NULL DEFAULT true,
    can_manage BOOLEAN     NOT NULL DEFAULT true,
    CONSTRAINT fk_chat_role FOREIGN KEY (chat_id) REFERENCES chats (id)
);

CREATE TABLE members
(
    id                   BIGSERIAL PRIMARY KEY,
    user_id              VARCHAR(255) NOT NULL,
    chat_id              BIGINT       NOT NULL,
    role_id              BIGINT       NOT NULL,
    inviter_id           BIGINT,
    preferred_name       VARCHAR(255),
    enable_notifications BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_chat FOREIGN KEY (chat_id) REFERENCES chats (id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_inviter FOREIGN KEY (inviter_id) REFERENCES members (id)
);

ALTER TABLE types
    ADD CONSTRAINT fk_default_role FOREIGN KEY (default_role_id) REFERENCES roles (id);

ALTER TABLE chats
    ADD CONSTRAINT fk_default_type FOREIGN KEY (default_type_id) REFERENCES types (id);

CREATE TABLE default_messages
(
    id           UUID PRIMARY KEY       DEFAULT gen_random_uuid(),
    chat_id      BIGINT,
    created_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    initiator_id BIGINT,
    victim_id    BIGINT,
    text         VARCHAR(1000) NOT NULL,
    type         VARCHAR(50)   NOT NULL,
    CONSTRAINT fk_chat_message FOREIGN KEY (chat_id) REFERENCES chats (id),
    CONSTRAINT fk_initiator FOREIGN KEY (initiator_id) REFERENCES members (id),
    CONSTRAINT fk_victim FOREIGN KEY (victim_id) REFERENCES members (id)
);

ALTER TABLE chats
    ADD COLUMN last_message_id UUID REFERENCES default_messages (id);

CREATE OR REPLACE FUNCTION create_default_role_and_member()
    RETURNS TRIGGER AS
$$
DECLARE
    default_role_id_value BIGINT;
BEGIN

    INSERT INTO roles (name, chat_id, can_write, can_read, can_delete, can_invite, can_manage)
    VALUES ('default', NEW.id, true, true, false, true, false)
    RETURNING id INTO default_role_id_value;


    UPDATE types
    SET default_role_id = default_role_id_value
    WHERE id = NEW.default_type_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_create_default_role_and_member
    AFTER INSERT
    ON chats
    FOR EACH ROW
EXECUTE FUNCTION create_default_role_and_member();

CREATE OR REPLACE FUNCTION set_default_role_id()
    RETURNS TRIGGER AS
$$
BEGIN

    IF NEW.role_id IS NULL THEN
        NEW.role_id := (SELECT t.default_role_id
                        FROM chats c
                                 JOIN types t ON c.default_type_id = t.id
                        WHERE c.id = NEW.chat_id);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_write_permission()
    RETURNS TRIGGER AS
$$
DECLARE
    user_can_write BOOLEAN;
BEGIN

    SELECT r.can_write
    INTO user_can_write
    FROM members m
             JOIN roles r ON m.role_id = r.id
    WHERE m.id = NEW.initiator_id;


    IF NOT user_can_write THEN
        RAISE EXCEPTION 'User does not have permission to write messages';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_write_permission
    BEFORE INSERT
    ON default_messages
    FOR EACH ROW
EXECUTE FUNCTION check_write_permission();

CREATE TRIGGER trg_set_default_role_id
    BEFORE INSERT
    ON members
    FOR EACH ROW
EXECUTE FUNCTION set_default_role_id();

CREATE OR REPLACE FUNCTION update_last_message()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE chats
    SET last_message_id = NEW.id
    WHERE id = NEW.chat_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_last_message
    AFTER INSERT
    ON default_messages
    FOR EACH ROW
EXECUTE FUNCTION update_last_message();

CREATE OR REPLACE FUNCTION check_role_permissions()
    RETURNS TRIGGER AS
$$
DECLARE
    user_can_write  BOOLEAN;
    user_can_read   BOOLEAN;
    user_can_delete BOOLEAN;
    user_can_invite BOOLEAN;
    user_can_manage BOOLEAN;
BEGIN

    SELECT r.can_write, r.can_read, r.can_delete, r.can_invite, r.can_manage
    INTO user_can_write, user_can_read, user_can_delete, user_can_invite, user_can_manage
    FROM members m
             JOIN roles r ON m.role_id = r.id
    WHERE m.id = NEW.initiator_id;


    IF TG_OP = 'INSERT' THEN
        IF NOT user_can_write THEN
            RAISE EXCEPTION 'User does not have permission to write messages';
        END IF;
    END IF;


    IF TG_OP = 'DELETE' THEN
        IF NOT user_can_delete THEN
            RAISE EXCEPTION 'User does not have permission to delete messages';
        END IF;
    END IF;


    IF TG_OP = 'UPDATE' THEN
        IF NOT user_can_manage THEN
            RAISE EXCEPTION 'User does not have permission to manage messages';
        END IF;
    END IF;


    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_role_permissions
    BEFORE INSERT OR DELETE OR UPDATE
    ON default_messages
    FOR EACH ROW
EXECUTE FUNCTION check_role_permissions();

CREATE INDEX idx_chats_title ON chats (title);
CREATE INDEX idx_chats_default_type_id ON chats (default_type_id);

CREATE INDEX idx_types_name ON types (name);
CREATE INDEX idx_types_user_id ON types (user_id);

CREATE INDEX idx_members_user_id ON members (user_id);
CREATE INDEX idx_members_chat_id ON members (chat_id);
CREATE INDEX idx_members_role_id ON members (role_id);

CREATE INDEX idx_messages_chat_id ON default_messages (chat_id);
CREATE INDEX idx_messages_created_at ON default_messages (created_at);
CREATE INDEX idx_messages_initiator_id ON default_messages (initiator_id);
CREATE INDEX idx_messages_victim_id ON default_messages (victim_id);

CREATE INDEX idx_roles_name ON roles (name);
CREATE INDEX idx_roles_chat_id ON roles (chat_id);