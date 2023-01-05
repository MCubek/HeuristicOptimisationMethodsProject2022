package hr.fer.hom.project.output;

import java.io.IOException;
import java.nio.file.Path;

import hr.fer.hom.project.algorithm.GreedyAlgorithm;
import hr.fer.hom.project.algorithm.IAlgorithm;
import hr.fer.hom.project.loader.InstanceLoader;
import hr.fer.hom.project.model.Instance;
import hr.fer.hom.project.model.Solution;
import hr.fer.hom.project.objective.IMinimizingSolutionObjectiveFunction;
import hr.fer.hom.project.objective.SolutionObjectiveFunction;

public class Example {
	
	public static void main(String[] args) {
		if (args.length != 2) throw new IllegalArgumentException("Path of input and output files are required as argument!");

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
        
        Output.outputSolutionToFile(testRoute, args[1]);
	}

}