
docker network create --driver bridge --attachable api_game_master
docker network create --driver bridge --attachable gateway_network

docker network create --driver bridge --attachable service_grafana
docker network create --driver bridge --attachable service_rabbitmq
docker network create --driver bridge --attachable service_prometheus
docker network create --driver bridge --attachable service_keycloak_network
docker network create --driver bridge --attachable service_postgres_network
docker network create --driver bridge --attachable service_fss
