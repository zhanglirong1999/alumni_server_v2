git pull
mvn clean package
pkill -f alumni
nohup java -jar target/alumni_server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prd &