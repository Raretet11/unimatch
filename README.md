## О проекте
**UM (UniMatch)** - платформа для организации P-2-P обучения в рамках университета, пользователи публикуют **скиллы**, которыми они обладают и готовы поделиться и те, которым они хотят научиться, после ищут себе пару и учатся друг у друга

Например, вы можете научить **X**, и хотите научиться **Y**, а кто-то умеет **Y** и хочет научиться **X**, опубликовав эту информацию на платформу вы найдете друг-друга и получите интересный опыт + знания

### Оснонвные бизнес-фичи:
1) Полнотекстовой поиск скиллов по описанию
2) Автоматический поиск пар скиллов по тегам, названию, описанию и тп
3) Комментарии и рейтинг от юзеров
4) Кастомизация профиля, в том числе аватарки
5) Регистрация по email (+ потенциально интеграция с университетскими сервисами)

## Использованые технологии

* [![Java][Java-shield]][Java-url]

* [![Spring Boot][Spring-shield]][Spring-url]

* [![Gradle][Gradle-shield]][Gradle-url]

* [![OpenAPI][OpenAPI-shield]][OpenAPI-url]

* [![PostgreSQL][Postgres-shield]][Postgres-url]

* [![Flyway][Flyway-shield]][Flyway-url]

* [![Meilisearch][Meili-shield]][Meili-url]

* [![MinIO][MinIO-shield]][MinIO-url]

* [![Docker][Docker-shield]][Docker-url]

* [![Prometheus][Prometheus-shield]][Prometheus-url]

* [![Grafana][Grafana-shield]][Grafana-url]

* [![Loki][Loki-shield]][Loki-url]

* [![Promtail][Promtail-shield]][Promtail-url]

## Запуск
_Для локального запуска необходимо:_

1) Склонировать репозиторий
   ```sh
   git clone https://github.com/Raretet11/unimatch.git
   ```
2) Получить логин+пароль для отправки email
3) Полностью заполнить `.env`, в качестве примера можно использовать `.env.dev`
4) Запустить сборку проекта указав ваш `.env`
   ```sh
   docker compose --env-file .env up  --build
   ```

## Контакты

Захаров Сергей - [@raretet_11](https://t.me/raretet_11) - MagicRaretet@gmail.com

Ссылка на проект: [https://github.com/Raretet11/unimatch](https://github.com/Raretet11/unimatch)

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[Java-shield]: https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://openjdk.org/projects/jdk/21/

[Spring-shield]: https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot

[Gradle-shield]: https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white
[Gradle-url]: https://gradle.org/

[Flyway-shield]: https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white
[Flyway-url]: https://flywaydb.org/

[Postgres-shield]: https://img.shields.io/badge/PostgreSQL_15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[Postgres-url]: https://www.postgresql.org/

[Meili-shield]: https://img.shields.io/badge/Meilisearch-FF5CAA?style=for-the-badge&logo=meilisearch&logoColor=white
[Meili-url]: https://www.meilisearch.com/

[MinIO-shield]: https://img.shields.io/badge/MinIO-C7202C?style=for-the-badge&logo=minio&logoColor=white
[MinIO-url]: https://min.io/

[Docker-shield]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/

[Prometheus-shield]: https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white
[Prometheus-url]: https://prometheus.io/

[Grafana-shield]: https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white
[Grafana-url]: https://grafana.com/

[Loki-shield]: https://img.shields.io/badge/Loki-F5A623?style=for-the-badge&logo=grafana&logoColor=white
[Loki-url]: https://grafana.com/oss/loki/

[Promtail-shield]: https://img.shields.io/badge/Promtail-F5A623?style=for-the-badge&logo=grafana&logoColor=white
[Promtail-url]: https://grafana.com/docs/loki/latest/send-data/promtail/

[OpenAPI-shield]: https://img.shields.io/badge/OpenAPI_3.0-6BA539?style=for-the-badge&logo=openapiinitiative&logoColor=white
[OpenAPI-url]: https://spec.openapis.org/oas/latest.html
