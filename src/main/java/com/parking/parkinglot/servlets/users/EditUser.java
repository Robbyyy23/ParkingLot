package com.parking.parkinglot.servlets.users;

import com.parking.parkinglot.common.UserDto;
import com.parking.parkinglot.ejb.UsersBean;
import com.parking.parkinglot.entities.UserGroup;
import jakarta.ejb.EJBException;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"WRITE_USERS"}))
@WebServlet(name = "EditUser", value = "/EditUser")
public class EditUser extends HttpServlet {
    @Inject
    UsersBean usersBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdParam = request.getParameter("id");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        Long userId = Long.parseLong(userIdParam);
        UserDto user = usersBean.findById(userId);
        request.setAttribute("user", user);

        request.setAttribute("userGroups", new String[] {"READ_CARS", "WRITE_CARS", "READ_USERS", "WRITE_USERS" , "INVOICING"});
        request.getRequestDispatcher("/WEB-INF/pages/users/editUser.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdParam = request.getParameter("user_id");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        Long userId = Long.valueOf(userIdParam);
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String[] userGroups = request.getParameterValues("user_groups");
        if (userGroups == null) {
            userGroups = new String[0];
        }

        try {
            usersBean.updateUser(userId, email, password, Arrays.asList(userGroups));
            response.sendRedirect(request.getContextPath() + "/Users");
        } catch (EJBException e) {
            request.setAttribute("error", "An error occurred while updating your information.");
            request.getRequestDispatcher("/WEB-INF/pages/users/editUser.jsp").forward(request, response);
        }
    }

}