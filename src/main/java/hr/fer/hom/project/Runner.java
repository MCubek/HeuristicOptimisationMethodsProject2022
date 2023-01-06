package hr.fer.hom.project;

import hr.fer.hom.project.algorithm.GreedyAlgorithm;
import hr.fer.hom.project.algorithm.IAlgorithm;
import hr.fer.hom.project.algorithm.LargeNeighbourhoodSearchAlgorithm;
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
import java.util.function.Function;

/**
 * @author matejc
 * Created on 25.12.2022.
 */

public class Runner {

    private static final int ITERATIONS = 500;
    private static final int ITERATOR_ITERATIONS = 100_000;
    private static final int MIN_TO_REMOVE = 2;
    private static final int MAX_TO_REMOVE = 6;


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
        IAlgorithm greedy = new GreedyAlgorithm(allCustomers, vehicleInstance);


        Solution initialSolution = greedy.run(null);

        IAlgorithm largeNeighbourhoodSearchAlgorithm = new LargeNeighbourhoodSearchAlgorithm(objectiveFunction,
                iteratorCreatorFunction,
                SolutionConstraintFactory.allConstraints,
                ITERATIONS);

        Solution solution = largeNeighbourhoodSearchAlgorithm.run(initialSolution);

        System.out.println(solution);
        System.out.println(objectiveFunction.stats(solution));

        try {
            FileUtil.outputSolutionToFile(solution, args[1]);
        } catch (IOException e) {
            System.err.println("Error while writing to file.");
            System.err.println(e.getMessage());
        }
    }

    private static final Function<Solution, ISolutionNeighbourhoodIterator> iteratorCreatorFunction = solution -> {
        ISolutionNeighbourhoodIterator iteratorNoCheck = new SolutionNeighbourhoodIterator(solution, MIN_TO_REMOVE, MAX_TO_REMOVE);

        return new ValidSolutionNeighbourHoodIterator(iteratorNoCheck,
                SolutionConstraintFactory.allConstraintsWithoutMaxVehicle,
                ITERATOR_ITERATIONS);
    };
}
