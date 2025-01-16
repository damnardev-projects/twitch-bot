# How to run

you have to define the following environment variables:

- DATABASE_PLATFORM (default: org.hibernate.dialect.PostgreSQLDialect)
- DATABASE_DRIVER (default: org.postgresql.Driver)
- DATABASE_URL
- DATABASE_USERNAME
- DATABASE_PASSWORD
- JASYPT_PASSWORD
- TWITCH_SAINT_URL
- CLIENT_ID (https://dev.twitch.tv/console)
- TWITCH_OAUTH_RETRY (default: 10)
- TWITCH_OAUTH_TIMEOUT in seconds (default: 6)
- TWITCH_SCHEDULED_CRON
- SECURITY_USER_NAME
- SECURITY_USER_PASSWORD

Run the following globalCommand:

```shell
mvn -P server -pl bot-server-runner org.springframework.boot:spring-boot-maven-plugin:run
```