package hr.fer.hom.project;

import hr.fer.hom.project.algorithm.GreedyAlgorithm;
import hr.fer.hom.project.algorithm.IAlgorithm;
import hr.fer.hom.project.constraints.SolutionConstraintFactory;
import hr.fer.hom.project.loader.InstanceLoader;
import hr.fer.hom.project.model.Instance;
import hr.fer.hom.project.model.Solution;
import hr.fer.hom.project.objective.IMinimizingSolutionObjectiveFunction;
import hr.fer.hom.project.objective.SolutionObjectiveFunction;

import java.io.IOException;
import java.nio.file.Path;

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

        var allCustomers = instance.customers();
        var vehicleInstance = instance.vehicleInstance();

        IMinimizingSolutionObjectiveFunction objectiveFunction = new SolutionObjectiveFunction(3, 1);
        IAlgorithm greedy = new GreedyAlgorithm(allCustomers, vehicleInstance, null);


        Solution testRoute = greedy.run(null);

        System.out.println(testRoute);
        System.out.println(objectiveFunction.stats(testRoute));
        
        // Checking all constrains
        System.out.println("All constrains " + SolutionConstraintFactory.allConstrains.checkConstraint(testRoute));
        System.out.println("eachCustomerForOneRoutes " + SolutionConstraintFactory.eachCustomerForOneRoute.checkConstraint(testRoute));
        System.out.println("amountsMatchDemands " + SolutionConstraintFactory.amountsMatchDemands.checkConstraint(testRoute));
        System.out.println("arrivalInterval " + SolutionConstraintFactory.arrivalInterval.checkConstraint(testRoute));
        System.out.println("depotStartAndEnd " + SolutionConstraintFactory.depotStartAndEnd.checkConstraint(testRoute));
        System.out.println("maximumNumberOfRoutes " + SolutionConstraintFactory.maximumNumberOfRoutes.checkConstraint(testRoute));
    }
}
