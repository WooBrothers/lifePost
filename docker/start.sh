echo "deploy start."

cd /home/ec2-user/myapp/lifepost/service

docker build -t lifepost-service -f lifepost-service.Dockerfile .

docker-compose --env-file ../env/.env up -d