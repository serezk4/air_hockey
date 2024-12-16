CREATE USER game_user WITH PASSWORD 'game_password';
CREATE USER keycloak WITH ENCRYPTED PASSWORD 'keycloak_password';

GRANT ALL PRIVILEGES ON DATABASE game TO game_user;
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;

\c game
GRANT USAGE ON SCHEMA public TO game_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO game_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO game_user;
ALTER ROLE game_user SET search_path TO game;

\connect keycloak

GRANT USAGE, CREATE ON SCHEMA public TO keycloak;
