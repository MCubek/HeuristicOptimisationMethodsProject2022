package hr.fer.hom.project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bero
 * Created on 27.12.2022.
 */

@Getter
@Setter
public class Route {

    private List<Customer> customers;
    private int capacity = 0;

    public Route(List<Customer> customers, int capacity) {
		super();
		this.customers = customers;
		this.capacity = capacity;
	}

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
        for (int i = 0; i < customers.size(); i++) {
            var currentCustomer = customers.get(i);

            // Wait for customer to be ready if it is not
            if (time < currentCustomer.readyTime()) {
                time = currentCustomer.readyTime();
            }

            time += currentCustomer.serviceTime();
            if(i+1 != customers.size())
                time += currentCustomer.calculateDistanceCeil(customers.get(i + 1));
        }
        return time;
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

    public List<Customer> getCustomers() {
		return customers;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
    public String toString() {
        return getCustomers().stream()
                .map(Customer::customerNumber)
                .map(Object::toString)
                .collect(Collectors.joining("->", "(", ")"));
    }

}
