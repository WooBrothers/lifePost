echo "deploy start."

docker build -t lifepost-service -f lifepost-service.Dockerfile .

docker-compose --env-file ../env/.env up -d