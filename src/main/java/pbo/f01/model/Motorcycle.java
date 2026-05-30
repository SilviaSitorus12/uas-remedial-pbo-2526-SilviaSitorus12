package pbo.f01.model;

// Nama: Silvia Eklesiana Sitorus
// NIM: 12S24004

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Kelas Motorcycle merepresentasikan kendaraan jenis motor.
 * Mewarisi dari Vehicle dengan discriminator value "motorcycle".
 */
@Entity
@DiscriminatorValue("motorcycle")
public class Motorcycle extends Vehicle {

    /**
     * Konstruktor kosong wajib untuk JPA.
     */
    public Motorcycle() {
    }

    /**
     * Konstruktor dengan parameter plate number dan owner.
     * Memanggil super constructor dengan type "motorcycle".
     *
     * @param plateNumber nomor plat kendaraan
     * @param owner pemilik kendaraan
     */
    public Motorcycle(String plateNumber, String owner) {
        super(plateNumber, owner, "motorcycle");
    }
}