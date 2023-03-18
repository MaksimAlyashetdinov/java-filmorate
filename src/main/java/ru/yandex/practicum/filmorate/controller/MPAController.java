package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

@RestController
@RequestMapping
public class MPAController {

    private final MPAService mpaService;


    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public List<MPA> getMPA() {
        return mpaService.getMPA();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPAById(@PathVariable int id) {
        return mpaService.getMPA(id);
    }
}
