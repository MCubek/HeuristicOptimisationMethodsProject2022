package hr.fer.hom.project.algorithm;

import hr.fer.hom.project.model.Customer;
import hr.fer.hom.project.model.Route;
import hr.fer.hom.project.model.Solution;
import hr.fer.hom.project.model.VehicleInstance;
import hr.fer.hom.project.neighbourhood.ISolutionNeighbourhoodIterator;

import java.util.*;

/**
 * @author matejc
 * Created on 04.01.2023.
 */

public class GreedyAlgorithm implements IAlgorithm {
    private final List<Customer> allCustomers;
    private final ISolutionNeighbourhoodIterator neighbourhoodIterator;
    private final VehicleInstance vehicleInstance;


    public GreedyAlgorithm(List<Customer> allCustomers,
                           VehicleInstance vehicleInstance,
                           ISolutionNeighbourhoodIterator neighbourhoodIterator) {
        this.allCustomers = allCustomers;
        this.vehicleInstance = vehicleInstance;
        this.neighbourhoodIterator = neighbourhoodIterator;
    }


    @Override
    public Solution run(Solution ignore) {
        LinkedList<Customer> leftCustomers = new LinkedList<>(allCustomers);

        List<Route> routes = new ArrayList<>();

        Customer depot = leftCustomers.removeFirst();

        while (!leftCustomers.isEmpty()) {
            Route route = new Route();
            Customer lastCustomer = depot;
            
            route.setCapacitiy(vehicleInstance.capacity());
            route.addCustomerToRouteEnd(depot);

            while (true) {
                final Customer lastCustomerFinal = lastCustomer;

                Optional<Customer> potentialNextStop = leftCustomers.stream()
                        .filter(customer -> hasCargoToSatisfyCustomer(customer, route))
                        .filter(customer -> customerIsOpen(customer, lastCustomerFinal, route))
                        .filter(customer -> canReachCustomerAndReturnToDepotInTime(customer, lastCustomerFinal, depot, route))
                        .min(Comparator.comparingDouble(lastCustomer::calculateDistance));

                if (potentialNextStop.isEmpty()) break;

                lastCustomer = potentialNextStop.get();

                route.addCustomerToRouteEnd(lastCustomer);
                leftCustomers.remove(lastCustomer);
            }
            route.addCustomerToRouteEnd(depot);
            routes.add(route);
        }

        return new Solution(routes, neighbourhoodIterator, allCustomers);
    }

    private boolean customerIsOpen(Customer nextCustomer, Customer lastCustomer, Route route) {
        int arrivalTime = route.getTotalRouteTime() + lastCustomer.calculateDistanceCeil(nextCustomer);

        return arrivalTime <= nextCustomer.dueDate();
    }

    private boolean hasCargoToSatisfyCustomer(Customer customer, Route route) {
        return vehicleInstance.capacity() >= customer.demand() + route.getUsedCargo();
    }

    private boolean canReachCustomerAndReturnToDepotInTime(Customer nextCustomer, Customer lastCustomer, Customer depot, Route route) {
        int totalRouteTime = route.getTotalRouteTime();

        totalRouteTime += lastCustomer.calculateDistanceCeil(nextCustomer);
        int nextCustomerArrivalTime = totalRouteTime;

        if (totalRouteTime < nextCustomer.readyTime()) {
            totalRouteTime = nextCustomer.readyTime();
        }
        
        totalRouteTime += nextCustomer.serviceTime();
        totalRouteTime += nextCustomer.calculateDistanceCeil(depot);

        return totalRouteTime <= depot.dueDate() && nextCustomerArrivalTime <= nextCustomer.dueDate();
    }
}
