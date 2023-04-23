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

