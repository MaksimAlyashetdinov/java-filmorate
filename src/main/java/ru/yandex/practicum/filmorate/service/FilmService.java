package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

@Service
public class FilmService {

    public void addLike(Film film, User user) {
        Set<Integer> filmLikes = new HashSet<>();
        if (!film.getLikes().isEmpty()) {
            filmLikes = film.getLikes();
        }
        filmLikes.add(user.getId());
        film.setLikes(filmLikes);
    }

    public void deleteLike(Film film, User user) {
        Set<Integer> filmLikes = film.getLikes();
        filmLikes.remove(user.getId());
        film.setLikes(filmLikes);
    }

    public List<Film> bestFilms (List<Film> allFilms) {
        List<Film> bestFilms = new ArrayList<>();
        for (Film film : allFilms) {
            if (!film.getLikes().isEmpty()) {
                bestFilms.add(film);
            }
        }
        Collections.sort(bestFilms, new FilmComparator());
        Collections.reverse(bestFilms);
        if (bestFilms.size() >= 10) {
            bestFilms = bestFilms.subList(0, 10);
        }
        return bestFilms;
    }
}
