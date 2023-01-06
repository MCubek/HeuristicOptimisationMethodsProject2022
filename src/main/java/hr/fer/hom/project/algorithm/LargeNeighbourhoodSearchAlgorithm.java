package hr.fer.hom.project.algorithm;

import hr.fer.hom.project.constraints.ISolutionConstraint;
import hr.fer.hom.project.model.Solution;
import hr.fer.hom.project.neighbourhood.ISolutionNeighbourhoodIterator;
import hr.fer.hom.project.objective.IMinimizingSolutionObjectiveFunction;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

/**
 * @author matejc
 * Created on 06.01.2023.
 */

@RequiredArgsConstructor
public class LargeNeighbourhoodSearchAlgorithm implements IAlgorithm {

    private final IMinimizingSolutionObjectiveFunction objectiveFunction;
    private final Function<Solution, ISolutionNeighbourhoodIterator> neighbourhoodIteratorCreator;
    private final ISolutionConstraint constraint;
    private final int iterations;

    @Override
    public Solution run(Solution initalSolution) {

        Solution incumbent = initalSolution;
        double incumbentScore = objectiveFunction.score(incumbent);

        for (int i = 0; i < iterations; i++) {
            ISolutionNeighbourhoodIterator neighbourhoodIterator = neighbourhoodIteratorCreator.apply(incumbent);

            while (neighbourhoodIterator.hasNext()) {
                Solution neighbour = neighbourhoodIterator.next();
                double neighbourScore = objectiveFunction.score(neighbour);

                if (neighbourScore < incumbentScore) {
                    incumbent = neighbour;
                    incumbentScore = neighbourScore;
                    break;
                }
            }
            // Reached max iterations on iterator
            if (!neighbourhoodIterator.hasNext()) {
                System.out.printf("Iteration %d, Reached maximum iterator iterations.%n", i);
            }

            if (i % 10 == 0) {
                System.out.format("Iteration %d, %s%n", i, objectiveFunction.stats(incumbent));
            }
        }

        if (!constraint.checkConstraint(incumbent)) {
            throw new IllegalStateException("Incumbent score does not satisfy all constraints.");
        }
        return incumbent;
    }
}
