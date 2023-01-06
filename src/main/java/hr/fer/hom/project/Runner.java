package hr.fer.hom.project;

import hr.fer.hom.project.algorithm.GreedyAlgorithm;
import hr.fer.hom.project.algorithm.IAlgorithm;
import hr.fer.hom.project.constraints.SolutionConstraintFactory;
import hr.fer.hom.project.loader.InstanceLoader;
import hr.fer.hom.project.model.Instance;
import hr.fer.hom.project.model.Solution;
import hr.fer.hom.project.neighbourhood.ISolutionNeighbourhoodIterator;
import hr.fer.hom.project.neighbourhood.SolutionNeighbourhoodIterator;
import hr.fer.hom.project.neighbourhood.ValidSolutionNeighbourHoodIterator;
import hr.fer.hom.project.objective.IMinimizingSolutionObjectiveFunction;
import hr.fer.hom.project.objective.SolutionObjectiveFunction;
import hr.fer.hom.project.output.FileUtil;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author matejc
 * Created on 25.12.2022.
 */

public class Runner {
    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("Path of input file and output file required as argument!");

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
        ISolutionNeighbourhoodIterator iteratorNoCheck = new SolutionNeighbourhoodIterator(testRoute, 2, 6);
        ISolutionNeighbourhoodIterator iteratorCheck = new ValidSolutionNeighbourHoodIterator(iteratorNoCheck, SolutionConstraintFactory.allConstraintsWithoutMaxVehicle);

        for (int i = 0; i < 10; i++) {
            System.out.println(iteratorCheck.next());
        }

        System.out.println(testRoute);
        System.out.println(objectiveFunction.stats(testRoute));

        try {
            FileUtil.outputSolutionToFile(testRoute, args[1]);
        } catch (IOException e) {
            System.err.println("Error while writing to file.");
            System.err.println(e.getMessage());
        }
    }
}
