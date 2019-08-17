
git pull
mvn clean package
pkill -f alumni
java -jar target/alumni_server-0.0.1-SNAPSHOT.jar &