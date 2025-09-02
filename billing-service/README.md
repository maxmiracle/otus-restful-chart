Или пересобрать с новым тегом.
```shell
docker build --platform linux/amd64 -t maxmiracle/otus-billing-service:1 .
```

Опубликовать
```shell
docker push maxmiracle/otus-billing-service:1
```

Запустить из dockerhub
```shell
docker run --name otus-billing-service-01 -d -p 8888:8080 maxmiracle/otus-billing-service:1
```
