package pbo.f01.model;

// Nama: Silvia Eklesiana Sitorus
// NIM : 12S24004

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("car")
public class Car extends Vehicle {

    public Car() {}

    public Car(String plateNumber, String owner) {
        super(plateNumber, owner, "car");
    }
}
