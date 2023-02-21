package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

   private final FilmService filmService;
   private final InMemoryFilmStorage inMemoryFilmStorage;

   @Autowired
   public FilmController(FilmService filmService, InMemoryFilmStorage inMemoryFilmStorage) {
       this.filmService = filmService;
       this.inMemoryFilmStorage = inMemoryFilmStorage;
   }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.addNewFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
       return inMemoryFilmStorage.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, int userId) {
       filmService.addLike(id,userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, int userId) {
       filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> bestFilms(@RequestParam(defaultValue = "10") int count){
        return filmService.bestFilms(count);
    }
}