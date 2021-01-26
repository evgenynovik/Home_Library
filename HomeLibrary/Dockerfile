FROM openjdk:11.0.9-jre
ENV JDBC_URL=jdbc:postgresql://192.168.99.100:8800/librarydb DB_USERNAME=pg DB_PASSWORD=12345
ENV DB_DRIVER=org.postgresql.Driver DB_NAME=POSTGRESQL DB_PLATFORM=org.hibernate.dialect.PostgreSQL10Dialect
ADD controllers/target/library.jar library.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "library.jar"]