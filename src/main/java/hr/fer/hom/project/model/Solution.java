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

    public Solution(List<Route> routes, ISolutionNeighbourhoodIterator neighbourhoodIterator) {
        this.routes = routes;
        this.neighbourhoodIterator = neighbourhoodIterator;
    }

    public Solution(ISolutionNeighbourhoodIterator neighbourhoodIterator) {
        this(new ArrayList<>(), neighbourhoodIterator);
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
        return "Vehicles: %d%nRoutes:%n".formatted(routes.size())
                + routes.stream()
                .map(Route::toString)
                .collect(Collectors.joining("\n"));
    }
}
