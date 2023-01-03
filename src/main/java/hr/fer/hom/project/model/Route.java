package hr.fer.hom.project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Bero
 * Created on 27.12.2022.
 */

@Getter
@Setter
public class Route {

    private List<Customer> customers;

    public Route(List<Customer> customers) {
        this.customers = customers;
    }

    public Route() {
        this.customers = new LinkedList<>();
    }

    public int calculateRouteDemand() {
        return customers.stream().mapToInt(Customer::demand).sum();
    }

    public int getTotalRouteTime() {
        int time = 0;
        for (int i = 0; i < customers.size() - 1; i++) {
            var currentCustomer = customers.get(i);
            var nextCustomer = customers.get(i + 1);

            // Wait for customer to be ready if it is not
            if (time < currentCustomer.readyTime()) {
                time = currentCustomer.readyTime();
            }

            time += currentCustomer.serviceTime();
            time += currentCustomer.calculateDistanceCeil(nextCustomer);
        }
        return time;
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

}
