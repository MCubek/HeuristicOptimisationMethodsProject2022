package hr.fer.hom.project.constraints;

import hr.fer.hom.project.model.Customer;
import hr.fer.hom.project.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * @author matejc
 * Created on 04.01.2023.
 */

public class SolutionConstraintFactory {
    private SolutionConstraintFactory() {
    }

    /**
     * Constrain 1 - Each customer is served by exactly one vehicle/route, with the resource amounts
     * that equal their demands.
     */
    public static final ISolutionConstraint eachCustomerForOneRoute = solution -> {

        List<Customer> allCustomersCopy = new ArrayList<>(solution.getAllCustomers());
        for (Route route : solution.getRoutes()) {
            for (Customer c : route.getCustomers()) {
                if (c.customerNumber() != 0 && !allCustomersCopy.contains(c)) return false;
                allCustomersCopy.remove(c);
            }
        }

        return true;
    };

    /**
     * Constrain 2 - The demand on each route must not exceed the capacity of the vehicle.
     */
    public static final ISolutionConstraint amountsMatchDemands = solution -> {

        for (Route route : solution.getRoutes())
            if (route.getCapacity() < route.calculateRouteDemand()) return false;

        return true;
    };

    /**
     * Constrain 3 - The vehicle servicing a certain customer must arrive at the customer location
     * within the interval given for that customer. The duration of the service can
     * exceed the interval.
     */
    public static final ISolutionConstraint arrivalInterval = solution -> {

        for (Route route : solution.getRoutes()) {
            int time = 0;

            List<Customer> customers = route.getCustomers();

            for (int i = 0; i < customers.size(); i++) {
                Customer currentCustomer = customers.get(i);

                if (time > currentCustomer.dueDate())
                    return false;

                if (time < currentCustomer.readyTime()) time = currentCustomer.readyTime();

                time += currentCustomer.serviceTime();
                if (i != customers.size() - 1) time += currentCustomer.calculateDistanceCeil(customers.get(i + 1));
            }
        }

        return true;
    };

    /**
     * Constrain 4 - Each vehicle starts and finishes its route in node 0 (customer 0 location; depot),
     * within the time interval given for customer 0.
     * This code does not check the time interval, add || solution.getAllRoutesTime() > endCustomer.dueDate() to if function to check time interval.
     */
    public static final ISolutionConstraint depotStartAndEnd = solution -> {

        for (Route route : solution.getRoutes()) {
            List<Customer> customers = route.getCustomers();
            Customer startCustomer = customers.get(0);
            Customer endCustomer = customers.get(customers.size() - 1);

            if (startCustomer.customerNumber() != 0 || endCustomer.customerNumber() != 0) return false;
        }

        return true;
    };
    
    /**
     * Constrain 5 - Maximum number of routes
     */
    public static final ISolutionConstraint maximumNumberOfRoutes = solution -> {
        return solution.getMaximumNumberOfVehicles() >= solution.getRoutes().size();
    };

    /**
     * All constrains combined.
     */
    public static final ISolutionConstraint allConstrains = solution ->
            eachCustomerForOneRoute.checkConstraint(solution) &&
                    amountsMatchDemands.checkConstraint(solution) &&
                    arrivalInterval.checkConstraint(solution) &&
                    depotStartAndEnd.checkConstraint(solution) &&
                    maximumNumberOfRoutes.checkConstraint(solution);

}
