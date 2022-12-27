package hr.fer.hom.project.model;

import java.util.List;

/**
 * @author matejc
 * Created on 27.12.2022.
 */

public record Instance(VehicleInstance vehicleInstance, List<Customer> customers) {
}

