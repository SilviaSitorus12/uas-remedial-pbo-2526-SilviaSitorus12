package pbo.f01.model;

// Nama: Silvia Eklesiana Sitorus
// NIM: 12S24004

import jakarta.persistence.*;

/**
 * Kelas abstrak Vehicle merepresentasikan kendaraan di sistem parkir.
 * Menggunakan warisan SINGLE_TABLE dengan discriminator column.
 */
@Entity
@Table(name = "vehicle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate_number", nullable = false, unique = true)
    private String plateNumber;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_area_id")
    private ParkingArea parkingArea;

    /**
     * Konstruktor kosong wajib untuk JPA.
     */
    public Vehicle() {
    }

    /**
     * Konstruktor dengan parameter.
     *
     * @param plateNumber nomor plat kendaraan
     * @param owner pemilik kendaraan
     * @param type tipe kendaraan (car/motorcycle)
     */
    public Vehicle(String plateNumber, String owner, String type) {
        this.plateNumber = plateNumber;
        this.owner = owner;
        this.type = type;
    }

    // Getter methods
    public Long getId() {
        return id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public ParkingArea getParkingArea() {
        return parkingArea;
    }

    // Setter methods
    public void setParkingArea(ParkingArea parkingArea) {
        this.parkingArea = parkingArea;
    }

    /**
     * Format string dari kendaraan: "BK1234AB Chandro Pardede car"
     */
    @Override
    public String toString() {
        return plateNumber + " " + owner + " " + type;
    }
}
