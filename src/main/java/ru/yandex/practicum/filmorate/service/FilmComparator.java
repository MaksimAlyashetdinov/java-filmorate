package ru.yandex.practicum.filmorate.service;

import java.util.Comparator;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmComparator implements Comparator<Film> {

    @Override
    public int compare(Film a, Film b) {
        return a.getLikes().size() - b.getLikes().size();
    }
}
