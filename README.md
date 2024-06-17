## About

This project is an email microservice in Java. It sends email notifications and tracks their delivery status. 
Emails are sent reliably, even if the email server is temporarily unavailable.

## Used Stack:

- **Development:** Java 17, Spring Boot 3.3.0
- **Build Tool:** Maven
- **Testing:** JUnit 5, AssertJ, Mockito
- **Code Simplification and Logging:** Lombok, SLF4J with Logback
- **Code Quality Tools:** Maven plugins (Checkstyle, PMD)
- **Test Coverage Tools:** JaCoCo Maven Plugin (aiming for 80% coverage)
- **CI/CD:** GitHub Actions
- **Mapping:** MapStruct
- **Documentation:** Javadoc
- **Database:** Elasticsearch
- **Message Broker:** Kafka
- **Monitoring:** Kibana

## How to run?

1) Clone repository

```shell
   git clone https://github.com/AndrewPleskanko/ProfITsoft_Course_Task5_MailSender
```

2) Create .env file in the project root directory and add your SMTP credentials:

 ```copy
MAIL_USERNAME=your_mail_username
MAIL_PASSWORD=your_mail_password
MAIL_HOST=syour_mail_host
MAIL_PORT=your_mail_port

KAFKA_ADDRESS=your_url
```

3) Build project

```shell
   docker build -t mail-sender .
   docker-compose up -d
```
## Services Started

This command will start the following services:

- mail-sender: The Spring Boot application (available at http://localhost:8092)
- elasticsearch: Elasticsearch database
- kibana: Kibana for Elasticsearch (available at http://localhost:5601)
- zookeeper: Apache ZooKeeper for Kafka
- kafka: Apache Kafka message broker

## Additional Notes
The application uses port 8092 for the Spring Boot application, 9200 for Elasticsearch, 
5601 for Kibana, 2181 for ZooKeeper, and 9092 for Kafka.



