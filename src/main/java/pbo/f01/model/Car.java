package pbo.f01.model;

// Nama: Silvia Eklesiana Sitorus
// NIM: 12S24004

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Kelas Car merepresentasikan kendaraan jenis mobil.
 * Mewarisi dari Vehicle dengan discriminator value "car".
 */
@Entity
@DiscriminatorValue("car")
public class Car extends Vehicle {

    /**
     * Konstruktor kosong wajib untuk JPA.
     */
    public Car() {
    }

    /**
     * Konstruktor dengan parameter plate number dan owner.
     * Memanggil super constructor dengan type "car".
     *
     * @param plateNumber nomor plat kendaraan
     * @param owner pemilik kendaraan
     */
    public Car(String plateNumber, String owner) {
        super(plateNumber, owner, "car");
    }
}