package pbo.f01.model;

// Nama: Silvia Eklesiana Sitorus
// NIM : 12S24004

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "parking_area")
public class ParkingArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "allowed_type", nullable = false, length = 20)
    private String allowedType;

    @OneToMany(mappedBy = "parkingArea", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> parkedVehicles = new ArrayList<>();

    // Konstruktor kosong wajib untuk JPA
    public ParkingArea() {}

    public ParkingArea(String name, int capacity, String allowedType) {
        this.name        = name;
        this.capacity    = capacity;
        this.allowedType = allowedType;
    }

    public Long          getId()             { return id; }
    public String        getName()           { return name; }
    public int           getCapacity()       { return capacity; }
    public String        getAllowedType()     { return allowedType; }
    public List<Vehicle> getParkedVehicles() { return parkedVehicles; }

    // Validasi: tipe cocok DAN kapasitas belum penuh
    public boolean canPark(Vehicle v) {
        return v.getType().equals(allowedType)
            && parkedVehicles.size() < capacity;
    }

    public void addVehicle(Vehicle v) {
        parkedVehicles.add(v);
        v.setParkingArea(this);
    }

    // Format: Area Rektorat car 5|2
    @Override
    public String toString() {
        return name + " " + allowedType + " " + capacity + "|" + parkedVehicles.size();
    }
}
