package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface LikesStorage {

    Set<Integer> likeFilm(int id, int userId);

    Set<Integer> deleteLikeFilm(int id, int userId);
}
