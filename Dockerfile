FROM openjdk:17-jdk-slim
WORKDIR /app
COPY .env /app/.env
COPY ./target/mail-sender.jar /app/mail-sender.jar
EXPOSE 8092
CMD ["java", "-jar", "mail-sender.jar"]
