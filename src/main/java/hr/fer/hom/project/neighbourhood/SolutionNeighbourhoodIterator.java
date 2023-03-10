package hr.fer.hom.project.neighbourhood;

import hr.fer.hom.project.model.Customer;
import hr.fer.hom.project.model.Route;
import hr.fer.hom.project.model.Solution;

import java.util.ArrayList;
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

        return new Solution(newRoutes, solution.getAllCustomers(), solution.getMaximumNumberOfVehicles());
    }

    private List<Route> addCustomersToRoute(List<Route> routes, List<Customer> customersToAdd) {
        for (var customer : customersToAdd) {
            Route pickedRoute = routes.get(random.nextInt(routes.size()));

            addCustomerToRandomPlaceInRoute(pickedRoute, customer);
        }
        return routes;
    }

    private void addCustomerToRandomPlaceInRoute(Route pickedRoute, Customer customer) {
        int indexWhereToInsert = random.nextInt(1, pickedRoute.getNumberOfStops() - 1);
        pickedRoute.addCustomerToIndex(customer, indexWhereToInsert);
    }

    private List<Route> removeCustomersFromRoute(List<Route> routes, List<Customer> customersToRemove) {
        List<Route> newRoutes = new ArrayList<>();
        for (Route route : routes) {
            route.getCustomers().removeAll(customersToRemove);
            if (route.getTotalRouteTime() > 0)
                newRoutes.add(route);
        }

        return newRoutes;
    }

    private List<Customer> pickCustomersToRemove(int numberOfCustomersToRemove) {
        List<Customer> customers = solution.getAllCustomers();
        List<Customer> customersToRemove = new ArrayList<>();

        int removed = 0;

        while (removed < numberOfCustomersToRemove) {
            Customer customer = customers.get(random.nextInt(1, customers.size()));
            if (!customersToRemove.contains(customer)) {
                customersToRemove.add(customer);
                removed++;
            }
        }

        return customersToRemove;
    }

    private List<Route> copyRoutes() {
        return solution.getRoutes().stream()
                .map(Route::copy)
                .toList();
    }
}
