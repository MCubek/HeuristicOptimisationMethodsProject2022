package hr.fer.hom.project.model;

import hr.fer.hom.project.constraints.ISolutionConstraint;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author matejc
 * Created on 03.01.2023.
 */

@Getter
@RequiredArgsConstructor
public class Solution {
    private final List<Route> routes;
    private final List<Customer> allCustomers;
    private final int maximumNumberOfVehicles;

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("%d%n".formatted(routes.size()));
        int i = 1;

        for (Route route : routes)
            sb.append(i++).append(": ").append(route).append("\n");

        sb.append(("%f%n".formatted(routes.stream()
                .mapToDouble(Route::getTotalRouteDistance)
                .sum()))
        );

        return sb.toString();
    }

}
