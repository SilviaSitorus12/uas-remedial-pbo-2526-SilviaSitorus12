package pbo.f01;

// Nama: Silvia Eklesiana Sitorus
// NIM : 12S24004

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

public class App {

    public static void main(String[] args) {

        // Inisialisasi JPA EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("parkit-pu");
        EntityManager em = emf.createEntityManager();

        // List in-memory untuk operasi display-all (hindari lazy loading issue)
        List<Vehicle>     vehicles = new ArrayList<>();
        List<ParkingArea> areas    = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            // Hentikan jika baris kosong
            if (input.isEmpty()) break;

            String[] seg = input.split("#");
            String command = seg[0];

            // ── area-add ──────────────────────────────────────────────
            if (command.equals("area-add")) {
                // Format: area-add#<name>#<capacity>#<allowed_type>
                String areaName    = seg[1];
                int    capacity    = Integer.parseInt(seg[2]);
                String allowedType = seg[3];

                // Cek duplikasi berdasarkan name
                boolean duplicate = false;
                for (ParkingArea a : areas) {
                    if (a.getName().equals(areaName)) {
                        duplicate = true;
                        break;
                    }
                }

                if (!duplicate) {
                    ParkingArea area = new ParkingArea(areaName, capacity, allowedType);
                    em.getTransaction().begin();
                    em.persist(area);
                    em.getTransaction().commit();
                    areas.add(area);
                }

            // ── vehicle-add ───────────────────────────────────────────
            } else if (command.equals("vehicle-add")) {
                // Format: vehicle-add#<plate_number>#<owner>#<type>
                String plate = seg[1];
                String owner = seg[2];
                String type  = seg[3];

                // Cek duplikasi berdasarkan plate_number
                boolean duplicate = false;
                for (Vehicle v : vehicles) {
                    if (v.getPlateNumber().equals(plate)) {
                        duplicate = true;
                        break;
                    }
                }

                if (!duplicate) {
                    Vehicle vehicle;
                    if (type.equals("car")) {
                        vehicle = new Car(plate, owner);
                    } else {
                        vehicle = new Motorcycle(plate, owner);
                    }
                    em.getTransaction().begin();
                    em.persist(vehicle);
                    em.getTransaction().commit();
                    vehicles.add(vehicle);
                }

            // ── park ──────────────────────────────────────────────────
            } else if (command.equals("park")) {
                // Format: park#<plate_number>#<area_name>
                String plate    = seg[1];
                String areaName = seg[2];

                // Cari kendaraan
                Vehicle targetVehicle = null;
                for (Vehicle v : vehicles) {
                    if (v.getPlateNumber().equals(plate)) {
                        targetVehicle = v;
                        break;
                    }
                }

                // Cari area
                ParkingArea targetArea = null;
                for (ParkingArea a : areas) {
                    if (a.getName().equals(areaName)) {
                        targetArea = a;
                        break;
                    }
                }

                // Validasi dan parkir
                if (targetVehicle != null && targetArea != null
                        && targetArea.canPark(targetVehicle)) {
                    targetArea.addVehicle(targetVehicle);
                    em.getTransaction().begin();
                    em.merge(targetVehicle);
                    em.merge(targetArea);
                    em.getTransaction().commit();
                }

            // ── display-all ───────────────────────────────────────────
            } else if (command.equals("display-all")) {
                // Sort area ascending by name
                areas.sort(Comparator.comparing(ParkingArea::getName));

                for (ParkingArea area : areas) {
                    System.out.println(area);

                    // Sort kendaraan di area ascending by plate_number
                    area.getParkedVehicles()
                        .stream()
                        .sorted(Comparator.comparing(Vehicle::getPlateNumber))
                        .forEach(System.out::println);
                }
            }
        }

        scanner.close();
        em.close();
        emf.close();
    }
}
