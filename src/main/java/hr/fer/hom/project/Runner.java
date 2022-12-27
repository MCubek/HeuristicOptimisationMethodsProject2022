package hr.fer.hom.project;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import hr.fer.hom.project.loader.InstanceLoader;
import hr.fer.hom.project.model.Customer;
import hr.fer.hom.project.model.Instance;
import hr.fer.hom.project.model.Route;

/**
 * @author matejc
 * Created on 25.12.2022.
 */

public class Runner {
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Path of file required as argument!");

        Path file = Path.of(args[0]);
        Instance instance = null;

        try {
            instance = InstanceLoader.loadInstance(file);

            System.out.println("Instance loaded.");
        } catch (IOException e) {
            System.err.println("Error while loading file.");
            e.printStackTrace();
            System.exit(1);
        }
        
        Route testRoute = new Route();
        List<Customer> customers = instance.customers();
        
        System.out.println();
        System.out.println("Distance between\n" + customers.get(1) + "\nand\n" + customers.get(2) + "\nis " + customers.get(1).calculateDistance(customers.get(2)));
        System.out.println();
        
        for(int i = 1; i < 10; i++) 
        	testRoute.addCustomer(customers.get(i));
        
        System.out.println("Route demand = " + testRoute.calculateRouteDemand());
        
    }
}
