-- Exported from QuickDBD: https://www.quickdatabasediagrams.com/
-- Link to schema: https://app.quickdatabasediagrams.com/#/d/sdoEM6
-- NOTE! If you have used non-SQL datatypes in your design, you will have to change these here.


CREATE TABLE "film" (
    "film_id" int   NOT NULL,
    "name" varchar   NOT NULL,
    "description" varchar   NOT NULL,
    "release_date" date   NOT NULL,
    "duration" int   NOT NULL
);

CREATE TABLE "film_genre" (
    "film_id" int   NOT NULL,
    "genre_id" int   NOT NULL
);

CREATE TABLE "films_likes" (
    "film_id" int   NOT NULL,
    "user_id" int   NOT NULL
);

CREATE TABLE "film_rating" (
    "film_id" int   NOT NULL,
    "rating_id" int   NOT NULL
);

CREATE TABLE "user" (
    "user_id" int   NOT NULL,
    "email" varchar   NOT NULL,
    "login" varchar   NOT NULL,
    "name" varchar   NOT NULL,
    "birthday" date   NOT NULL
);

CREATE TABLE "users_friends" (
    "user_id" int   NOT NULL,
    "friend_id" int   NOT NULL,
    "friendship_status_id" int   NOT NULL
);

CREATE TABLE "genre_catalog" (
    "genre_id" int   NOT NULL,
    "genre_name" varchar   NOT NULL
);

CREATE TABLE "ratings_catalog" (
    "rating_id" int   NOT NULL,
    "rating_name" varchar   NOT NULL
);

CREATE TABLE "friendship_status" (
    "friendship_status_id" int   NOT NULL,
    "friendship_status_name" varchar   NOT NULL
);

ALTER TABLE "film" ADD CONSTRAINT "fk_film_film_id" FOREIGN KEY("film_id")
REFERENCES "film_genre" ("film_id");

ALTER TABLE "film_genre" ADD CONSTRAINT "fk_film_genre_genre_id" FOREIGN KEY("genre_id")
REFERENCES "genre_catalog" ("genre_id");

ALTER TABLE "films_likes" ADD CONSTRAINT "fk_films_likes_film_id" FOREIGN KEY("film_id")
REFERENCES "film" ("film_id");

ALTER TABLE "films_likes" ADD CONSTRAINT "fk_films_likes_user_id" FOREIGN KEY("user_id")
REFERENCES "user" ("user_id");

ALTER TABLE "film_rating" ADD CONSTRAINT "fk_film_rating_film_id" FOREIGN KEY("film_id")
REFERENCES "film" ("film_id");

ALTER TABLE "film_rating" ADD CONSTRAINT "fk_film_rating_rating_id" FOREIGN KEY("rating_id")
REFERENCES "ratings_catalog" ("rating_id");

ALTER TABLE "user" ADD CONSTRAINT "fk_user_user_id" FOREIGN KEY("user_id")
REFERENCES "users_friends" ("user_id");

ALTER TABLE "users_friends" ADD CONSTRAINT "fk_users_friends_friend_id" FOREIGN KEY("friend_id")
REFERENCES "user" ("user_id");

ALTER TABLE "users_friends" ADD CONSTRAINT "fk_users_friends_friendship_status_id" FOREIGN KEY("friendship_status_id")
REFERENCES "friendship_status" ("friendship_status_id");

