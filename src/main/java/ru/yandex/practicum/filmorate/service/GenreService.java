package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Service
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getGenres() {
        log.info("Get all genres.");
        return genreStorage.getGenres();
    }

    public Genre getGenreById(int id) {
        validate(id);
        log.info("Get genre {}.", id);
        return genreStorage.getGenreById(id);
    }

    private void validate(int id) {
        if (1 > id || id > 6) {
            throw new NotFoundException("Genre" + id + "not found.");
        }
    }
}
