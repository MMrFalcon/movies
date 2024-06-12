# movies
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/MMrFalcon/movies/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/MMrFalcon/movies/tree/master)

[![codecov](https://codecov.io/gh/MMrFalcon/movies/branch/master/graph/badge.svg?token=0UR5RDYHH9)](https://codecov.io/gh/MMrFalcon/movies)

## Run MySQL Database With Docker

```
cd movies/src/main/docker

sudo docker-compose -f mysql.yml up -d
```

Connect application with working database by editing:
`movies/src/main/java/resources/application.properties`

Replace `custom.db.address` IP value by your docker machine address.

********
## API

### GET

```
http://localhost:8080/api/movies
```
Use provided criteria:
```
http://localhost:8080/api/movies?authorNameLike=falcon
http://localhost:8080/api/movies?movieTypeIn=COMEDY,HORROR
http://localhost:8080/api/movies?movieTypeIn=COMEDY,HORROR&authorNameLike=falcon
```

```
http://localhost:8080/api/authors
```
# Covered topics
## Check how one "join" can reduce performance
1) Add some random data (this operation will take some time):
```
POST: /api/authors/seed-by-random-data/1000
POST: /api/movies/seed-by-random-data/10000
```
2) Ask for data with and without "join":
```
GET: /api/authors/reports/movies-count?page=0&size=10
GET: /api/authors/reports/movies-count-with-join?page=0&size=10
```
****
You can check implementation of getReportByCriteria() and getReportByCriteriaWithJoin() inside
[AuthorQueryService](src/main/java/com/falcon/movies/service/query/AuthorQueryService.java).

Performance was tested by compareMoviesCountReportsResponseTimeForWithAndWithoutJoinStatement() 
method inside [AuthorControllerTestIT](src/test/java/com/falcon/movies/web/controller/AuthorControllerTestIT.java).

## How to check CRUD operations data in tests with EntityManager or JpaRepository
#### Repository:

createAuthor(), updateAuthor(), deleteById() inside [AuthorControllerTestIT](src/test/java/com/falcon/movies/web/controller/AuthorControllerTestIT.java).

#### EntityManager:

Static methods that can be called from anywhere if you provide entityManager and transaction:
findByTitle(), countMovies() inside [AuthorControllerTestIT](src/test/java/com/falcon/movies/web/controller/AuthorControllerTestIT.java)

## How to use "Specification" pattern provided by JpaSpecificationExecutor, added to any repository

Handling Enum with "in" clause, "equals" for numbers and "like" for Strings:
[MovieQueryService](src/main/java/com/falcon/movies/service/query/MovieQueryService.java) findByCriteria()