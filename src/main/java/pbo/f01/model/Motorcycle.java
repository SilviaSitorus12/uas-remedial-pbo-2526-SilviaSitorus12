package pbo.f01.model;

// Nama: Silvia Eklesiana Sitorus
// NIM : 12S24004

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("motorcycle")
public class Motorcycle extends Vehicle {

    public Motorcycle() {}

    public Motorcycle(String plateNumber, String owner) {
        super(plateNumber, owner, "motorcycle");
    }
}
