package hr.fer.hom.project.model;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

/**
 * @author Bero
 * Created on 27.12.2022.
 */

@Getter
public class Route {

    private final List<Customer> customers;
    private final int capacity;

    public Route(List<Customer> customers, int capacity) {
        super();
        this.customers = customers;
        this.capacity = capacity;
    }

    public Route(int capacity) {
        this.customers = new LinkedList<>();
        this.capacity = capacity;
    }

    public int calculateRouteDemand() {
        return customers.stream().mapToInt(Customer::demand).sum();
    }

    public int getTotalRouteTime() {
        int time = 0;
        for (int i = 0; i < customers.size(); i++) {
            var currentCustomer = customers.get(i);

            // Wait for customer to be ready if it is not
            if (time < currentCustomer.readyTime()) {
                time = currentCustomer.readyTime();
            }

            time += currentCustomer.serviceTime();
            if (i + 1 != customers.size())
                time += currentCustomer.calculateDistanceCeil(customers.get(i + 1));
        }
        return time;
    }

    public double getTotalRouteDistance() {
        double totalDistance = 0;
        for (int i = 0; i < customers.size() - 1; i++)
            totalDistance += customers.get(i).calculateDistance(customers.get(i + 1));
        return totalDistance;
    }

    public int getUsedCargo() {
        return customers.stream()
                .mapToInt(Customer::demand)
                .sum();
    }

    public void addCustomerToIndex(Customer customer, int index) {
        customers.add(index, customer);
    }

    public void addCustomerToRouteEnd(Customer customer) {
        customers.add(customer);
    }

    public void removeCustomerFromIndex(int index) {
        customers.remove(index);
    }

    public boolean containsCustomer(Customer customer) {
        return customers.contains(customer);
    }

    public int getRouteLength() {
        return customers.size();
    }

    public Route copy() {
        return new Route(new LinkedList<>(getCustomers()), getCapacity());
    }
    
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int time = 0;

        for (int i = 0; i < customers.size(); i++) {
            Customer currentCustomer = customers.get(i);
            sb.append(currentCustomer.customerNumber()).append("(").append(time).append(")");
            if (i != customers.size() - 1) sb.append("->");

            if (time < currentCustomer.readyTime())
                time = currentCustomer.readyTime();
            time += currentCustomer.serviceTime();
            if (i != customers.size() - 1) time += currentCustomer.calculateDistanceCeil(customers.get(i + 1));
        }

        return sb.toString();
    }

}
