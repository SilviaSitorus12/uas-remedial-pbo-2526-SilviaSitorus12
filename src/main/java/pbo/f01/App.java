package pbo.f01;

// Nama: Silvia Eklesiana Sitorus
// NIM: 12S24004

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import pbo.f01.model.Car;
import pbo.f01.model.Motorcycle;
import pbo.f01.model.ParkingArea;
import pbo.f01.model.Vehicle;

/**
 * Kelas utama aplikasi sistem manajemen parkir kendaraan.
 * Mengelola input perintah dari user dan melakukan operasi CRUD ke database.
 */
public class App {
    private static final String PERSISTENCE_UNIT = "parkit-pu";

    public static void main(String[] args) {
        // Inisialisasi EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();

        // Cache in-memory untuk list kendaraan dan area parkir
        List<Vehicle> vehicles = new ArrayList<>();
        List<ParkingArea> parkingAreas = new ArrayList<>();

        // Input scanner
        Scanner scanner = new Scanner(System.in);

        try {
            // Baca input baris per baris
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();

                // Hentikan jika baris kosong
                if (input == null || input.trim().isEmpty()) {
                    break;
                }

                // Split dengan delimiter "#"
                String[] parts = input.split("#");

                if (parts.length == 0 || parts[0].isEmpty()) {
                    continue;
                }

                String command = parts[0];

                // Proses perintah
                switch (command) {
                    case "area-add":
                        handleAreaAdd(parts, parkingAreas, em);
                        break;
                    case "vehicle-add":
                        handleVehicleAdd(parts, vehicles, em);
                        break;
                    case "park":
                        handlePark(parts, vehicles, parkingAreas, em);
                        break;
                    case "display-all":
                        handleDisplayAll(parkingAreas);
                        break;
                }
            }
        } finally {
            scanner.close();
            em.close();
            emf.close();
        }
    }

    /**
     * Menangani perintah area-add.
     * Format: area-add#<name>#<capacity>#<allowed_type>
     */
    private static void handleAreaAdd(String[] parts, List<ParkingArea> parkingAreas, EntityManager em) {
        if (parts.length < 4) {
            return;
        }

        String name = parts[1];
        int capacity = Integer.parseInt(parts[2]);
        String allowedType = parts[3];

        // Cek duplikasi by name
        for (ParkingArea area : parkingAreas) {
            if (area.getName().equals(name)) {
                return; // Skip jika sudah ada
            }
        }

        // Buat dan persist area parkir baru
        ParkingArea newArea = new ParkingArea(name, capacity, allowedType);
        em.getTransaction().begin();
        em.persist(newArea);
        em.getTransaction().commit();

        // Tambah ke list
        parkingAreas.add(newArea);
    }

    /**
     * Menangani perintah vehicle-add.
     * Format: vehicle-add#<plate_number>#<owner>#<type>
     */
    private static void handleVehicleAdd(String[] parts, List<Vehicle> vehicles, EntityManager em) {
        if (parts.length < 4) {
            return;
        }

        String plateNumber = parts[1];
        String owner = parts[2];
        String type = parts[3];

        // Cek duplikasi by plateNumber
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getPlateNumber().equals(plateNumber)) {
                return; // Skip jika sudah ada
            }
        }

        // Buat kendaraan sesuai tipe
        Vehicle newVehicle;
        if (type.equals("car")) {
            newVehicle = new Car(plateNumber, owner);
        } else if (type.equals("motorcycle")) {
            newVehicle = new Motorcycle(plateNumber, owner);
        } else {
            return; // Tipe tidak dikenali
        }

        // Persist ke database
        em.getTransaction().begin();
        em.persist(newVehicle);
        em.getTransaction().commit();

        // Tambah ke list
        vehicles.add(newVehicle);
    }

    /**
     * Menangani perintah park.
     * Format: park#<plate_number>#<area_name>
     */
    private static void handlePark(String[] parts, List<Vehicle> vehicles, 
                                   List<ParkingArea> parkingAreas, EntityManager em) {
        if (parts.length < 3) {
            return;
        }

        String plateNumber = parts[1];
        String areaName = parts[2];

        // Cari vehicle by plateNumber
        Vehicle vehicle = null;
        for (Vehicle v : vehicles) {
            if (v.getPlateNumber().equals(plateNumber)) {
                vehicle = v;
                break;
            }
        }

        // Cari area by name
        ParkingArea area = null;
        for (ParkingArea a : parkingAreas) {
            if (a.getName().equals(areaName)) {
                area = a;
                break;
            }
        }

        // Jika kedua ada dan bisa diparkir
        if (vehicle != null && area != null && area.canPark(vehicle)) {
            area.addVehicle(vehicle);
            em.getTransaction().begin();
            em.merge(vehicle);
            em.merge(area);
            em.getTransaction().commit();
        }
    }

    /**
     * Menangani perintah display-all.
     * Menampilkan semua area dan kendaraan yang diparkir.
     */
    private static void handleDisplayAll(List<ParkingArea> parkingAreas) {
        // Sort areas ascending by name
        parkingAreas.sort(Comparator.comparing(ParkingArea::getName));

        // Print setiap area
        for (ParkingArea area : parkingAreas) {
            System.out.println(area.toString());

            // Sort parkedVehicles ascending by plateNumber
            area.getParkedVehicles().sort(Comparator.comparing(Vehicle::getPlateNumber));

            // Print setiap kendaraan di area
            for (Vehicle vehicle : area.getParkedVehicles()) {
                System.out.println(vehicle.toString());
            }
        }
    }
} 