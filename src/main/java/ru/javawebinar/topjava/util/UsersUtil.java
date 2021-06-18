package ru.javawebinar.topjava.util;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {

    public static final List<User> users = Arrays.asList(
            new User(null, "Semen", "sem@mail.ru", "111", Role.USER),
            new User(null, "Petr", "petr@mail.ru", "112", Role.USER),
            new User(null, "Fedor", "fedor@mail.ru", "113", Role.USER),
            new User(null, "Ivan", "ivan@mail.ru", "114", Role.USER)
    );
}
