package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;

    @NotNull(message = "An invalid email has been entered.")
    @Email(message = "An invalid email has been entered.")
    private String email;

    @NotBlank(message = "An invalid login has been entered. The login cannot be empty and contain spaces.")
    private String login;

    private String name;

    @NotNull
    @Past(message = "The date of birth cannot be later than the current date.")
    private LocalDate birthday;
}