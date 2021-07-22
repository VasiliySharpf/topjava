package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;

@Controller
public class JspMealController {

    @Autowired
    private MealService service;

    @GetMapping("/meals")
    public String getMeals(Model model) {
        //int userId = (int) model.getAttribute("userId");
        model.addAttribute("meals", service.getAll(100000));
        return "meals";
    }
}
