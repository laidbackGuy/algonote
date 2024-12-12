docker rm spring-container --force


docker build --no-cache -t spring-image .

docker run -p 8080:8081 -d --name spring-container spring-image