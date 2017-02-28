#Movie rating service

RESTful microservice, powered by Spring Boot, one-button bootstrap, can benefit from Redis datastore to optimise movie ranking on-the-fly, in-memory H2 database, which can simply be replaced by MongoDB or any other DB with a few lines of code. 

##To run the program:

- Clone this repository

- `./gradlew bootRun`

##To enable Redis:

a) Install Redis locally (default port - 6379)
b) Install Docker with Compose and run:

`docker-compose up -d`