package hr.fer.hom.project.loader;

import hr.fer.hom.project.model.Customer;
import hr.fer.hom.project.model.Instance;
import hr.fer.hom.project.model.VehicleInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author matejc
 * Created on 27.12.2022.
 */

public class InstanceLoader {
    private InstanceLoader() {
    }

    @SuppressWarnings("UnusedAssignment")
    public static Instance loadInstance(Path file) throws IOException {
        try (var reader = Files.newBufferedReader(file)) {
            List<Customer> customerList = new ArrayList<>();

            String line = reader.readLine();

            // Look for vehicle instance in file
            while (line != null && !line.strip().equals("VEHICLE")) {
                line = reader.readLine();
            }
            // Skip to data
            line = reader.readLine();
            line = reader.readLine();

            var split = line.trim().split("\\s+");
            VehicleInstance vehicleInstance = new VehicleInstance(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

            // Look for customers instances in file
            while (line != null && !line.strip().equals("CUSTOMER")) {
                line = reader.readLine();
            }
            // Skip to data
            line = reader.readLine();
            line = reader.readLine();
            line = reader.readLine();

            // Read customers
            while (line != null && !line.isEmpty()) {
                split = line.trim().split("\\s+");

                customerList.add(new Customer(
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2]),
                        Integer.parseInt(split[3]),
                        Integer.parseInt(split[4]),
                        Integer.parseInt(split[5]),
                        Integer.parseInt(split[6])
                ));

                line = reader.readLine();
            }
            return new Instance(vehicleInstance, customerList);
        }
    }
}
