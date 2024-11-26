package com.parking.parkinglot.ejb;

import com.parking.parkinglot.common.CarDto;
import com.parking.parkinglot.entities.Car;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class CarsBean {
    private static final Logger LOG = Logger.getLogger(CarsBean.class.getName());
    @PersistenceContext
    EntityManager entityManager;
    public List<CarDto> findAllCars(){
        LOG.info("findAllCars");
        try{
            TypedQuery<Car> typeQuery = entityManager.createQuery("SELECT c FROM Car c", Car.class);
            List<Car> cars = typeQuery.getResultList();
            return copyCarsToDto(cars);
        }catch (Exception ex)
        {
            throw new EJBException(ex);
        }
    }
    public List<CarDto> copyCarsToDto(List<Car> cars) {
        return cars.stream()
                .map(car -> new CarDto(
                        car.getId(),
                        car.getLicensePlate(),
                        car.getOwner().getUsername(), // Ensure owner is not null
                        car.getParkingSpot()))
                .collect(Collectors.toList());
    }

}