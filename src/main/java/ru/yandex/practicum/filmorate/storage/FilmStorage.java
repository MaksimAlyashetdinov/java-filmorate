package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film addNewFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();
}
