package com.parking.parkinglot.servlets.users;

import com.parking.parkinglot.common.UserDto;
import com.parking.parkinglot.ejb.InvoiceBean;
import com.parking.parkinglot.ejb.UsersBean;
import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.HttpMethodConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DeclareRoles({"READ_USERS", "WRITE_USERS", "INVOICING"})
@ServletSecurity(
        value = @HttpConstraint(rolesAllowed = {"READ_USERS"}),
        httpMethodConstraints = {
                @HttpMethodConstraint(value = "POST", rolesAllowed = {"WRITE_USERS", "INVOICING"})
        }
)
@WebServlet(name = "Users", value = "/Users")
public class Users extends HttpServlet {

    @Inject
    private UsersBean usersBean;

    @Inject
    private InvoiceBean invoiceBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<UserDto> users = usersBean.findAllUsers();
        request.setAttribute("users", users);

        if (request.isUserInRole("INVOICING")) {
            Collection<Long> userIdsWithInvoices = invoiceBean.getUserIds();
            if (!userIdsWithInvoices.isEmpty()) {
                Collection<String> usernames = usersBean.findUsernamesByUserIds(userIdsWithInvoices);
                request.setAttribute("invoices", usernames);
            }
        }

        request.getRequestDispatcher("/WEB-INF/pages/users/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.isUserInRole("INVOICING")) {
            String[] userIdsAsString = request.getParameterValues("user_ids");
            if (userIdsAsString != null) {
                try {
                    List<Long> userIds = new ArrayList<>();
                    for (String userIdAsString : userIdsAsString) {
                        userIds.add(Long.parseLong(userIdAsString));
                    }

                    invoiceBean.getUserIds().addAll(userIds);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid user ID format.");
                    request.getRequestDispatcher("/WEB-INF/pages/users/users.jsp").forward(request, response);
                    return;
                }
            }

            response.sendRedirect(request.getContextPath() + "/Users");
        }
    }
}
