#Movie Rating service

RESTful microservice, powered by Spring Boot, one-button bootstrap, can benefit from Redis datastore to optimise movie ranking on-the-fly, in-memory H2 database, which can simply be replaced by MongoDB or any other DB with a few lines of code. 

##To run the program:

- Clone this repository

- `./gradlew bootRun`

##Available options:

`-Dserver.port` - port for this application

`-Dpath` - path to file with input data

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

**NB** JSONView plugin for chrome is highly recommended: https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc/related?hl=en

Movie API will be available at `localhost:8080/movies{?page,size,sort}`

Movie search API reference will be available at `localhost:8080/movies/search`

Current rankings available at `localhost:8080/movies/rankings`

##Bonus: 

Spring Rest Repositories also support updating/inserting entities via rest:

$ curl -X POST -i -H "Content-Type:application/json" -d '{"year":"1989", "rating":"100", "title":"Westworld"}' http://localhost:8080/movies