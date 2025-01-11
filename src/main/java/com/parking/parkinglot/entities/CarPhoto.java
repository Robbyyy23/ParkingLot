package com.parking.parkinglot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Entity
public class CarPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(nullable = false, length = 255)
    @NotBlank(message = "Filename is mandatory")
    @Size(max = 255, message = "Filename cannot exceed 255 characters")
    String filename;

    @Basic
    @Column(nullable = false, length = 50)
    @NotBlank(message = "File type is mandatory")
    @Size(max = 50, message = "File type cannot exceed 50 characters")
    String fileType;
    @Lob
    @Column(nullable = false)
    @NotNull(message = "File content is mandatory")
    byte[] fileContent;
    Car car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @OneToOne
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
