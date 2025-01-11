package com.parking.parkinglot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(unique = true, nullable = false, length = 100)
    @NotBlank(message = "License plate is mandatory")
    @Size(max = 100, message = "License plate cannot exceed 100 characters")
    private String licensePlate;

    @Basic
    @Column(unique = true,nullable = false, length = 100)
    @NotBlank(message = "Parking spot is mandatory")
    @Size(max = 100, message = "Parking spot name cannot exceed 100 characters")
    private String parkingSpot;
    private User owner;
    private CarPhoto photo;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(String parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }


    @OneToOne(mappedBy = "car" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public CarPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(CarPhoto photo) {
        this.photo = photo;
    }
}