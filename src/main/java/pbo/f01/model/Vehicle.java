package pbo.f01.model;

// Nama: Silvia Eklesiana Sitorus
// NIM : 12S24004

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_area_id")
    private ParkingArea parkingArea;

    @Column(name = "plate_number", nullable = false, unique = true, length = 20)
    private String plateNumber;

    @Column(name = "owner", nullable = false, length = 100)
    private String owner;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    // Konstruktor kosong wajib untuk JPA
    public Vehicle() {}

    public Vehicle(String plateNumber, String owner, String type) {
        this.plateNumber = plateNumber;
        this.owner       = owner;
        this.type        = type;
    }

    public Long        getId()           { return id; }
    public String      getPlateNumber()  { return plateNumber; }
    public String      getOwner()        { return owner; }
    public String      getType()         { return type; }
    public ParkingArea getParkingArea()  { return parkingArea; }

    public void setParkingArea(ParkingArea parkingArea) {
        this.parkingArea = parkingArea;
    }

    // Format: BK1234AB Chandro Pardede car
    @Override
    public String toString() {
        return plateNumber + " " + owner + " " + type;
    }
}
