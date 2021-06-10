package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jspPage = "";
        String action = request.getParameter("action");

        List<MealTo> mealsTo = MealsUtil.filteredByStreams(
                MealsUtil.getMeals(),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                MealsUtil.CALORIES_PER_DAY);

        if (action.equalsIgnoreCase("listMeals")) {
            request.setAttribute("mealsTo", mealsTo);
            request.getRequestDispatcher("/listMeals.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("insert")) {
            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int a = 0;
    }

}
