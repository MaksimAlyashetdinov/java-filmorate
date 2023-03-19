package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.MPA;

public interface MPAStorage {

    List<MPA> getMPAs();

    MPA getMPA(int id);
}
