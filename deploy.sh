echo "deploy start."

cd /home/ec2-user/myapp/lifepost/service

docker build -t lifepost-service .

docker compose up -d