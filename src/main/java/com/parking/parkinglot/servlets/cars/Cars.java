package com.parking.parkinglot.servlets.cars;

import com.parking.parkinglot.common.CarDto;
import com.parking.parkinglot.ejb.CarsBean;
import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DeclareRoles({"READ_CARS","WRITE_CARS"})
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"READ_CARS"}),httpMethodConstraints = {@HttpMethodConstraint(value = "POST", rolesAllowed = {"WRITE_CARS"})})
@WebServlet(name = "Cars", value = "/Cars")
public class Cars extends HttpServlet {
    private static int TOTAL_PARKING_SPOTS = 10;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        List<CarDto> cars=carsBean.findAllCars();
        int occupiedSpots = cars.size();
        int numberOfFreeParkingSpots = TOTAL_PARKING_SPOTS - occupiedSpots;

        request.setAttribute("cars", cars);
        request.setAttribute("numberOfFreeParkingSpots", numberOfFreeParkingSpots);
        request.getRequestDispatcher("/WEB-INF/pages/cars/cars.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
            String[] carIdsAsString = request.getParameterValues("car_Ids");
            if (carIdsAsString != null) {
                List<Long> carIds = new ArrayList<>();
                for(String carIdAsString : carIdsAsString) {
                    carIds.add(Long.parseLong(carIdAsString));
                }
                carsBean.deleteCarsByIds(carIds);
            }
        response.sendRedirect(request.getContextPath()+"/Cars");
    }
@Inject
    CarsBean carsBean;
}