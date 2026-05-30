package pbo.f01.model;

// Nama: Silvia Eklesiana Sitorus
// NIM: 12S24004

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas ParkingArea merepresentasikan area parkir dengan kapasitas dan tipe kendaraan yang diizinkan.
 */
@Entity
@Table(name = "parking_area")
public class ParkingArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "allowed_type", nullable = false, length = 20)
    private String allowedType;

    @OneToMany(mappedBy = "parkingArea", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> parkedVehicles;

    /**
     * Konstruktor kosong wajib untuk JPA.
     */
    public ParkingArea() {
        this.parkedVehicles = new ArrayList<>();
    }

    /**
     * Konstruktor dengan parameter.
     *
     * @param name nama area parkir
     * @param capacity kapasitas area parkir
     * @param allowedType tipe kendaraan yang diizinkan (car/motorcycle)
     */
    public ParkingArea(String name, Integer capacity, String allowedType) {
        this.name = name;
        this.capacity = capacity;
        this.allowedType = allowedType;
        this.parkedVehicles = new ArrayList<>();
    }

    // Getter methods
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getAllowedType() {
        return allowedType;
    }

    public List<Vehicle> getParkedVehicles() {
        return parkedVehicles;
    }

    /**
     * Cek apakah kendaraan dapat diparkir di area ini.
     * Kondisi: tipe kendaraan sesuai AND belum penuh.
     *
     * @param v kendaraan yang akan diparkir
     * @return true jika dapat diparkir, false sebaliknya
     */
    public boolean canPark(Vehicle v) {
        return v.getType().equals(allowedType) && parkedVehicles.size() < capacity;
    }

    /**
     * Tambahkan kendaraan ke area parkir.
     * Otomatis set parkingArea pada kendaraan.
     *
     * @param v kendaraan yang akan ditambahkan
     */
    public void addVehicle(Vehicle v) {
        parkedVehicles.add(v);
        v.setParkingArea(this);
    }

    /**
     * Format string dari area parkir: "Area Rektorat car 5|2"
     * Format: "Area <name> <allowedType> <capacity>|<jumlahTerparkir>"
     */
    @Override
    public String toString() {
        return "Area " + name + " " + allowedType + " " + capacity + "|" + parkedVehicles.size();
    }
}
