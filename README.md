To reproduce the exception

```sh
docker-compose -f compose.yml up -d
```

Wait until a keycloak instance is ready (http://localhost:8081 to check ui)

```sh
./gradlew bootRun
```

open http://localhost:8080/trigger
