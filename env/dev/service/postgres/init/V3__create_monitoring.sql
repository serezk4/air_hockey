CREATE USER monitoring WITH PASSWORD 'monitoring_password';

GRANT pg_read_all_stats to monitoring;
GRANT pg_monitor TO monitoring;

GRANT CONNECT ON DATABASE game TO monitoring;
GRANT CONNECT ON DATABASE keycloak TO monitoring;

\c game
GRANT USAGE ON SCHEMA public TO monitoring;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO monitoring;

\c keycloak
GRANT USAGE ON SCHEMA public TO monitoring;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO monitoring;
