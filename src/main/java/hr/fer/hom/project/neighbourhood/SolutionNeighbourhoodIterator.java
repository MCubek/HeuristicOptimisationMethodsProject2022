package hr.fer.hom.project.neighbourhood;

import hr.fer.hom.project.model.Customer;
import hr.fer.hom.project.model.Route;
import hr.fer.hom.project.model.Solution;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @author matejc
 * Created on 04.01.2023.
 */

public class SolutionNeighbourhoodIterator implements ISolutionNeighbourhoodIterator {

    private final Solution solution;
    private final int minCustomersToRemove;
    private final int maxCustomersToRemove;

    private static final Random random = new Random();

    public SolutionNeighbourhoodIterator(Solution solution, int minCustomersToRemove, int maxCustomersToRemove) {
        this.solution = solution;
        this.minCustomersToRemove = minCustomersToRemove;
        this.maxCustomersToRemove = maxCustomersToRemove;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Solution next() {
        if (!hasNext()) throw new NoSuchElementException();

        int numberOfCustomersToRemove = random.nextInt(minCustomersToRemove, maxCustomersToRemove + 1);

        List<Customer> customersToRemove = pickCustomersToRemove(numberOfCustomersToRemove);

        List<Route> routesCopy = copyRoutes();

        List<Route> routesWithRemovedCustomers = removeCustomersFromRoute(routesCopy, customersToRemove);

        List<Route> newRoutes = addCustomersToRoute(routesWithRemovedCustomers, customersToRemove);

        return new Solution(newRoutes, solution.getNeighbourhoodIterator(), solution.getAllCustomers());
    }

    private List<Route> addCustomersToRoute(List<Route> routes, List<Customer> customersToAdd) {
        // TODO Implement
        return null;
    }

    private List<Route> removeCustomersFromRoute(List<Route> routes, List<Customer> customersToRemove) {
        // TODO Implement
        return null;
    }

    private List<Customer> pickCustomersToRemove(int numberOfCustomersToRemove) {
        // TODO Implement
        return null;
    }

    private List<Route> copyRoutes() {
        return solution.getRoutes().stream()
                .map(Route::copy)
                .toList();
    }
}
