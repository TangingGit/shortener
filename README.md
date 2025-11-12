# How to start shortener project
1. Go to docker folder then run command docker compose up.
2. run application on ShortenerApplication file.
3. Open Postman in /docker/postman, file shortener.postman_collection.json


# Table Relationals
## Table: shorten_url
For collect of shortened urls

| Column Name  | Data Type | Description       |     Constraints     | Not Null |
|:-------------|:---------:|:------------------|:-------------------:|:--------:|
| id           |  int(11)  | id of shorten url |         PK          |   yes    |
| short_url    |  varchar(62)  | short url         | INDEX (shorten_url_idx01)  |   yes    |
| original_url    |  varchar(100)  | original url      |INDEX (shorten_url_idx02)  |   yes    |

## Table: user
For collect of users information

| Column Name | Data Type | Description      | Constraints | Not Null |
|:------------|:---------:|:-----------------|:-----------:|:--------:|
| email       |  varchar(30) | email of user    |     PK      | yes      |
| password    |  varchar(64)  | password of user |             |yes      |

## Sequence: base62_counter_seq
For counter base62 logic<br />

|Start Value|Max Value|Increment By|Cache|
|:----------|:--------:|:-----------:|:----:|
|1          |9223372036854775806|1          |1000 |