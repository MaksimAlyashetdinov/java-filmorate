package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.Data;

@Data
public class User {

    private int id;
    @NotNull(message = "Введен недопустимый email.")
    @Email(message = "Введен недопустимый email.")
    private String email;
    @NotNull(message = "Введен недопустимый логин. Логин не может быть пустым и содержать пробелы.")
    @NotBlank(message = "Введен недопустимый логин. Логин не может быть пустым и содержать пробелы.")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть позже текущей даты.")
    private LocalDate birthday;
}