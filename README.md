#Movie Rating service

RESTful microservice, powered by Spring Boot, one-button bootstrap, can benefit from Redis datastore to optimise movie ranking on-the-fly, in-memory H2 database, which can simply be replaced by MongoDB or any other DB with a few lines of code. 

##To run the program:

- Clone this repository

- `./gradlew bootRun`

##Available options:

`-Dserver.port` - port for this application

`-Dpath` - path to file with input data

`-DskipRanks` - toggle on or off skipping rank when rating is equal

`-Dredis` - use Redis for rating service, needs Redis running locally on 6379

##To enable Redis:

a) Install Redis locally (default port - 6379)

OR 

b) Install Docker with Compose and run:

`docker-compose up -d`

Start service with `-Dredis=true`:

`./gradlew bootRun -Dredis=true`


##RESTful API:

Thanks to Spring HATEOAS, the service provides easy access to the state:

Rating API available at `localhost:8080/ratings{?page,size,sort}`

Movie API available at `localhost:8080/movies{?page,size,sort}`

Movie search API reference available at `localhost:8080/movies/search`

Rating search API reference available at `localhost:8080/ratings/search`
