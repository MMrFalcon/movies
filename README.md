# movies

[![CircleCI](https://circleci.com/gh/MMrFalcon/movies/tree/master.svg?style=svg&circle-token=380762a36dd83fce91665ff183297d7d7fd660a0)](https://circleci.com/gh/MMrFalcon/movies/tree/master)

[![codecov](https://codecov.io/gh/MMrFalcon/movies/branch/master/graph/badge.svg?token=0UR5RDYHH9)](https://codecov.io/gh/MMrFalcon/movies)

## Run MySQL Database With Docker

```
cd movies/src/main/docker

sudo docker-compose -f mysql.yml up -d
```

Connect application with working database by editing:
`movies/src/main/java/resources/application.properties`

Replace `custom.db.address` IP value by your docker machine address.

