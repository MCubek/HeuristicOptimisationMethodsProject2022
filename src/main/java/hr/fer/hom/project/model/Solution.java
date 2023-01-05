package hr.fer.hom.project.model;

import hr.fer.hom.project.constraints.ISolutionConstraint;
import hr.fer.hom.project.neighbourhood.ISolutionNeighbourhoodIterator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author matejc
 * Created on 03.01.2023.
 */

@Getter
@Setter
public class Solution implements Iterable<Solution> {
    private final ISolutionNeighbourhoodIterator neighbourhoodIterator;
    private List<Route> routes;
    private List<Customer> allCustomers;

    public Solution(List<Route> routes, ISolutionNeighbourhoodIterator neighbourhoodIterator, List<Customer> allCustomers) {
		this.neighbourhoodIterator = neighbourhoodIterator;
		this.routes = routes;
		this.allCustomers = allCustomers;
	}

	public Solution(List<Route> routes, ISolutionNeighbourhoodIterator neighbourhoodIterator) {
        this.routes = routes;
        this.neighbourhoodIterator = neighbourhoodIterator;
    }

    public Solution(ISolutionNeighbourhoodIterator neighbourhoodIterator, List<Customer> allCustomers) {
        this(new ArrayList<>(), neighbourhoodIterator, allCustomers);
    }

	public List<Customer> getAllCustomers() {
		return allCustomers;
	}

	public void setAllCustomers(List<Customer> allCustomers) {
		this.allCustomers = allCustomers;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public int getNumberOfVehicles() {
        return getRoutes().size();
    }

	public int getAllRoutesTime() {
        return getRoutes().stream()
                .mapToInt(Route::getTotalRouteTime)
                .sum();
    }

    public Route getRoute(int index) {
        return routes.get(index);
    }

    public boolean containsCustomer(Customer customer) {
        for (var route : routes) {
            if (route.containsCustomer(customer)) return true;
        }
        return false;
    }

    public boolean satisfiesConstraint(ISolutionConstraint constraint) {
        return constraint.checkConstraint(this);
    }

    @Override
    public Iterator<Solution> iterator() {
        return neighbourhoodIterator;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("%d%n".formatted(routes.size()));
    	int i = 1;
    	
    	for(Route route : routes) sb.append(i++ + ": " + route + "\n");
    	
    	sb.append(("%.2f%n".formatted(routes.stream().mapToDouble(Route::getTotalRouteDistance).sum())).replace(".", ","));
    	
        return sb.toString();
    }
}
