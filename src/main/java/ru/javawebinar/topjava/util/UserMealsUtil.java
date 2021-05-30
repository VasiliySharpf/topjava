package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        // Превышает ли сумма каллорий за день, заданного для пользователя значения
        List<UserMealWithExcess> userMealWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> sumCaloriesPerDay = new HashMap<>();
        // сумма каллорий за день
        for (UserMeal meal : meals) {
            LocalDate dateMeal = meal.getDateTime().toLocalDate();
            Integer sumCalories = sumCaloriesPerDay.getOrDefault(dateMeal, 0);
            sumCalories += meal.getCalories();
            sumCaloriesPerDay.put(dateMeal, sumCalories);
        }
        // фильтрация по времени
        for (UserMeal meal : meals) {
            LocalTime timeMeal = meal.getDateTime().toLocalTime();
            if (timeMeal.compareTo(startTime) == 0
                    || TimeUtil.isBetweenHalfOpen(timeMeal, startTime, endTime)) {
                Integer sumCalories = sumCaloriesPerDay.get(meal.getDateTime().toLocalDate());
                userMealWithExcess.add(new UserMealWithExcess(
                        meal.getDateTime(), meal.getDescription(), meal.getCalories(), sumCalories > caloriesPerDay));
            }
        }
        return userMealWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> sumCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(x -> x.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> meal.getDateTime().toLocalTime().compareTo(startTime) == 0
                        || TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        sumCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
