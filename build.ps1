.\mvnw package -DskipTests

docker build -t retail-system-be-java .

docker image tag retail-system-be-java vuonghoang010100/retail-system:be-2.0-java

docker push vuonghoang010100/retail-system:be-2.0-java

docker rmi retail-system-be-java
docker rmi vuonghoang010100/retail-system:be-2.0-java

Write-Output "Build complete!"