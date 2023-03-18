package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

@Component
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getMPAs() {
        List<MPA> allMPAs = this.jdbcTemplate.query(
                "select MPA_ID, MPA_NAME from MPA",
                (resultSet, rowNum) -> {
                    MPA mpa = new MPA(resultSet.getInt("MPA_ID"), resultSet.getString("MPA_NAME"));
                    return mpa;
                });
        return allMPAs.stream().sorted((o1, o2) -> o1.getId() - o2.getId())
                .collect(Collectors.toList());
    }

    @Override
    public MPA getMPA(int id) {
        MPA mpa = jdbcTemplate.queryForObject(
                "select mpa_id, mpa_name from MPA where mpa_id = ?",
                (resultSet, rowNum) -> {
                    MPA newMPA = new MPA(resultSet.getInt("mpa_id"),
                            resultSet.getString("mpa_name"));
                    return newMPA;
                },
                id);
        return mpa;
    }
}
