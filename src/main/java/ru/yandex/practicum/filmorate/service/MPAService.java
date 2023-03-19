package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

@Service
@Slf4j
public class MPAService {

    private final MPAStorage mpaStorage;

    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MPA> getMPA() {
        log.info("Get allMPAs");
        return mpaStorage.getMPAs();
    }

    public MPA getMPA(int id) {
        validate(id);
        log.info("Get MPA {}", id);
        return mpaStorage.getMPA(id);
    }

    private void validate(int id) {
        if (id < 1 || id > 5) {
            throw new NotFoundException(String.format("MPA %d not found.", id));
        }
    }
}
