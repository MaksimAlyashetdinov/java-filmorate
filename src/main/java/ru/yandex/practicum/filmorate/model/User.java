package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    int id;

    @NotNull(message = "An invalid email has been entered.")
    @Email(message = "An invalid email has been entered.")
    String email;

    @NotBlank(message = "An invalid login has been entered. The login cannot be empty and contain spaces.")
    String login;

    String name;

    @NotNull
    @Past(message = "The date of birth cannot be later than the current date.")
    LocalDate birthday;
}