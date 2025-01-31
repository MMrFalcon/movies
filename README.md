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
method inside [BigDataTestIT](src/test/java/com/falcon/movies/web/controller/BigDataTestIT.java).

## How to check CRUD operations data in tests with EntityManager or JpaRepository
#### Repository:

createAuthor(), updateAuthor(), deleteById() inside [AuthorControllerTestIT](src/test/java/com/falcon/movies/web/controller/AuthorControllerTestIT.java).

#### EntityManager:

Static methods that can be called from anywhere if you provide entityManager and transaction:
findByTitle(), countMovies() inside [AuthorControllerTestIT](src/test/java/com/falcon/movies/web/controller/AuthorControllerTestIT.java)

## How to use "Specification" pattern provided by JpaSpecificationExecutor, added to any repository

Handling Enum with "in" clause, "equals" for numbers and "like" for Strings:
[MovieQueryService](src/main/java/com/falcon/movies/service/query/MovieQueryService.java) findByCriteria()

## Capturing variables and invoking methods in a chain.
In the implementation of the [RandomMoviePicker](src/main/java/com/falcon/movies/service/impl/util/RandomMoviePicker.java)
the following topics were covered:
1. **Capturing variables:** The `captureMovies` method demonstrates how to capture variables within a lambda expression.
2. **Using the `Runnable` Functional Interface:** The `ifEmpty` method utilizes the `Runnable` interface to execute a block of code.
3. **Using the `Consumer` Functional Interface:** The `ifNonEmptyCapture` method employs the `Consumer` interface to consume a list of movies.
4. **Static Factory Method Pattern:** The `from` method exemplifies the usage of the Static Factory Method pattern to create instances of the `RandomMoviePicker`.

All these concepts were combined to generate a list of three random movies within the
[MovieToWatchServiceImpl](src/main/java/com/falcon/movies/service/impl/MovieToWatchServiceImpl.java).`pickMoviesToWatch`.

The implementation's results can be accessed through the following API call:
```
GET http://localhost:8080/api/movies-to-watch
```

# **Grafana Setup**

The Docker Compose file **`grafana.yml`** is designed for **Windows systems**.  
If you're using a different setup, **comment out** or **update** the volume mapping:
```yaml
# E:/logs:/var/log  # Update this path to match your local setup
```

### **🚀 Start Grafana, Loki, and Promtail**
Run the following command to start the services:
```sh
docker compose -f .\grafana.yml up -d
```

---

## **🛠️ Enable Spring Boot Logging to Loki**
To enable **Spring Boot** logging with Loki, activate the **Maven profile** named `grafana`:
```sh
mvn spring-boot:run -Dspring-boot.run.profiles=grafana
```

---

## **📌 Access Grafana**
Open Grafana in your browser:  
👉 [http://localhost:3000](http://localhost:3000)

**Default login credentials:**
- **Username:** `admin`
- **Password:** `admin`

---

## **📡 Add Loki as a Data Source**
1. **Go to** ⚙️ **Settings** → **Data Sources**
2. Click **"Add data source"**
3. Select **Loki**
4. Set the **URL** to:
   ```
   http://loki:3100
   ```
5. Click **"Save & Test"**

---

## **📊 Create a Dashboard**
1. **Navigate to:**
    - **Home** → **Dashboards** → **New Dashboard**
2. Select **Loki** as the data source
3. Scroll down to **Label Filters**, then:
    - Select **`app`** → **`movies-app`**
    - Use the following LogQL query:
      ```logql
      {app="movies-app"} |= ""
      ```
4. Click **"Visualization Suggestion"** and select **"Logs"**
5. Use the right panel to **customize the view**
6. **Don't forget to save your dashboard!** 💾

---

