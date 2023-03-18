package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreStorage {

    List<Genre> getGenres();

    Genre getGenreById(int id);
}
