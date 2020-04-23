Перед запуском проекта в "Продовой" среде, необходимо запустить контейнер с базой

`` docker run -d --name testPostgresDb -p 5432:5432 -e POSTGRES_PASSWORD=pwd postgres ``

Для запуска тестов, используется H2DB, контейнер с постгрей они не требуют

`` ./gradlew check ``