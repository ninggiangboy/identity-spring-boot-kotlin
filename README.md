# Getting Started

## Generate private and public key

```bash
openssl genrsa -out private_key.pem 2048

openssl rsa -in private_key.pem -pubout -out public_key.pem
```

Then move the file ``public_key.pem`` to the ``src/main/resources`` folder.

## Change the environment

Some environment variables are not required to run the application (just need to set up some docker services)

| Variable             | Description                                              |
|----------------------|----------------------------------------------------------|
| `DB_HOST`            | Database host address                                    |
| `DB_PORT`            | Database port                                            |
| `DB_NAME`            | Database name                                            |
| `DB_USERNAME`        | Database username                                        |
| `DB_PASSWORD`        | Database password                                        |
| `PGADMIN_EMAIL`      | Email for pgAdmin login                                  |
| `PGADMIN_PASSWORD`   | Password for pgAdmin login                               |
| `PGADMIN_PORT`       | Port for accessing pgAdmin                               |
| `RD_HOST`            | Redis host address                                       |
| `RD_PORT`            | Redis port                                               |
| `RD_STACK_PORT`      | Redis stack port                                         |
| `MAIL_PORT`          | Port for mail service                                    |
| `MAIL_USERNAME`      | Username for mail service                                |
| `MAIL_PASSWORD`      | Password for mail service                                |
| `MAIL_SERVER_PORT`   | Server port for mail service                             |
| `JWT_SECRET`         | Secret key for JWT authentication                        |
| `FRONT_END_URL`      | URL for the front-end application                        |

The ``JWT_SECRET`` must be the content of key of the file ``private_key.pem`` generated in the previous step. Then you can remove the file ``private_key.pem``.

You can use the ``.env`` file to set up the environment variables and set to IDE to read this file.

## Setup Docker
You can remove pgAdmin and MailDev services from the ``docker-compose.yml`` file if you don't need them.

Start the services with the command:
```bash
docker-compose up -d
```

## Migration
This project uses Flyway to manage the database schema. You can change the migration files in the ``src/main/resources/db/migration`` folder. Or disable the Flyway in the ``application.yml`` file.

```yml
spring:
  flyway:
    enabled: false
```

## Run the application
You can set up to run application with environment variables or with the ``.env`` file.

## Documentation
The documentation is available at the ``/swagger-ui.html`` endpoint.