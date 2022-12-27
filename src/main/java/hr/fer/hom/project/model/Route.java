package hr.fer.hom.project.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Bero
 * Created on 27.12.2022.
 */
public class Route {
	
	private List<Customer> customers;
	
	public Route(List<Customer> customers) {
		super();
		this.customers = customers;
	}

	public Route() {
		super();
		this.customers = new ArrayList<>();
	}
	
	public Route createReverseRoute() {
		List<Customer> reversedCustomers = new ArrayList<>();
		for(int i = customers.size()-1; i >= 0; i--) 
			reversedCustomers.add(customers.get(i));
				
		return new Route(reversedCustomers);
	}
	
	
	public int calculateRouteDemand() {
		return customers.stream().mapToInt(Customer::demand).sum();
	}
	
	
	public void addCustomer(Customer customer) {
		customers.add(customer);
	}
	
	public void removeCustomer(Customer customer) {
		customers.remove(customer);
	}
	
	public boolean containsCustomer(Customer customer) {
		return customers.contains(customer);
	}
	

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
		
}
