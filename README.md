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

# **Run MySQL Database with Docker**

Run the following commands to start the MySQL database:
```sh
cd movies/src/main/docker
sudo docker-compose -f mysql.yml up -d
```

### **🔗 Connect the Application to the Database**
Edit the configuration file:  
📄 `movies/src/main/java/resources/application.properties`

Replace `custom.db.address` with your **Docker machine's IP address**.

---

# **API Endpoints**

## **📥 GET Requests**

Retrieve all movies:
```
http://localhost:8080/api/movies
```

Use query parameters to filter results:
```
http://localhost:8080/api/movies?authorNameLike=falcon
http://localhost:8080/api/movies?movieTypeIn=COMEDY,HORROR
http://localhost:8080/api/movies?movieTypeIn=COMEDY,HORROR&authorNameLike=falcon
```

Retrieve all authors:
```
http://localhost:8080/api/authors
```

---

# **🔍 Performance Testing: Effect of Joins**

### **1️⃣ Seed Test Data**
Run the following commands to add random test data (**this may take time**):
```sh
POST: /api/authors/seed-by-random-data/1000
POST: /api/movies/seed-by-random-data/10000
```

### **2️⃣ Compare Performance with and without Joins**
```sh
GET: /api/authors/reports/movies-count?page=0&size=10
GET: /api/authors/reports/movies-count-with-join?page=0&size=10
```

The implementation can be found in:  
📄 [`AuthorQueryService`](src/main/java/com/falcon/movies/service/query/AuthorQueryService.java)

Performance tests were conducted in:  
📄 [`BigDataTestIT`](src/test/java/com/falcon/movies/web/controller/BigDataTestIT.java)

---

# **✅ CRUD Operations in Tests**

### **🗄️ Repository Methods:**
- `createAuthor()`, `updateAuthor()`, `deleteById()` are implemented in:  
  📄 [`AuthorControllerTestIT`](src/test/java/com/falcon/movies/web/controller/AuthorControllerTestIT.java)

### **🔍 EntityManager Usage:**
- Static methods like `findByTitle()` and `countMovies()` can be used anywhere by providing an `entityManager` and `transaction`.
- Implemented in:  
  📄 [`AuthorControllerTestIT`](src/test/java/com/falcon/movies/web/controller/AuthorControllerTestIT.java)

---

# **🛠️ Using the Specification Pattern with JpaSpecificationExecutor**

The **Specification** pattern is used for filtering data using `JpaSpecificationExecutor`.  
It handles **Enums with "IN" clause**, **numbers with "equals"**, and **Strings with "LIKE"**.  
Implemented in:  
📄 [`MovieQueryService`](src/main/java/com/falcon/movies/service/query/MovieQueryService.java) → `findByCriteria()`

---

# **🎲 Capturing Variables & Functional Interfaces**

In 📄 [`RandomMoviePicker`](src/main/java/com/falcon/movies/service/impl/util/RandomMoviePicker.java), the following topics are demonstrated:

1. **Capturing Variables**: `captureMovies()` method captures variables within a lambda.
2. **Using `Runnable` Functional Interface**: `ifEmpty()` executes a block of code.
3. **Using `Consumer` Functional Interface**: `ifNonEmptyCapture()` processes a list of movies.
4. **Static Factory Method Pattern**: `from()` creates instances of `RandomMoviePicker`.

These concepts are applied in:  
📄 [`MovieToWatchServiceImpl`](src/main/java/com/falcon/movies/service/impl/MovieToWatchServiceImpl.java) → `pickMoviesToWatch()`

To test the implementation, call:
```sh
GET http://localhost:8080/api/movies-to-watch
```

