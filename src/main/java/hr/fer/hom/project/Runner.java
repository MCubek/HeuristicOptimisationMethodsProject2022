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
import hr.fer.hom.project.timer.Timer;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.function.BiFunction;

/**
 * @author matejc
 * Created on 25.12.2022.
 */

public class Runner {
    private static final int MIN_TO_REMOVE = 1;
    private static final int MAX_TO_REMOVE = 6;


    /**
     * Run and output solution to file.
     * Takes 3 arguments: Instance file path, output file path and minutes to run algorithm.
     *
     * @param args input file path, output file path and minutes to run.
     */
    public static void main(String[] args) {
        if (args.length != 3)
            throw new IllegalArgumentException("Path of input file, output file and runtime in minutes required as argument!");

        Path file = Path.of(args[0]);
        Path output = Path.of(args[1]);

        Duration timerDuration = Duration.ofMinutes(Long.parseLong(args[2]));

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

        IMinimizingSolutionObjectiveFunction objectiveFunction = new SolutionObjectiveFunction(6, 1);

        Timer timer = new Timer(timerDuration);

        IAlgorithm greedy = new GreedyAlgorithm(allCustomers, vehicleInstance);
        Solution initialSolution = greedy.run(null);

        IAlgorithm largeNeighbourhoodSearchAlgorithm = new LargeNeighbourhoodSearchAlgorithm(objectiveFunction,
                iteratorCreatorFunction,
                SolutionConstraintFactory.allConstraints,
                timer
        );

        Solution solution = largeNeighbourhoodSearchAlgorithm.run(initialSolution);

        System.out.println(solution);
        System.out.println(objectiveFunction.stats(solution));
        System.out.printf("Calls to objective function: %d%n", objectiveFunction.numCalls());

        try {
            FileUtil.outputSolutionToFile(solution, output);
        } catch (IOException e) {
            System.err.println("Error while writing to file.");
            System.err.println(e.getMessage());
        }
    }

    private static final BiFunction<Solution, Timer, ISolutionNeighbourhoodIterator> iteratorCreatorFunction = (solution, timer) -> {
        ISolutionNeighbourhoodIterator iteratorNoCheck = new SolutionNeighbourhoodIterator(solution, MIN_TO_REMOVE, MAX_TO_REMOVE);

        return new ValidSolutionNeighbourHoodIterator(iteratorNoCheck,
                SolutionConstraintFactory.allConstraintsWithoutMaxVehicle,
                timer);
    };
}
