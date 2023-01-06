package hr.fer.hom.project.algorithm;

import hr.fer.hom.project.constraints.ISolutionConstraint;
import hr.fer.hom.project.model.Solution;
import hr.fer.hom.project.neighbourhood.ISolutionNeighbourhoodIterator;
import hr.fer.hom.project.objective.IMinimizingSolutionObjectiveFunction;
import hr.fer.hom.project.timer.Timer;
import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;

/**
 * @author matejc
 * Created on 06.01.2023.
 */

@RequiredArgsConstructor
public class LargeNeighbourhoodSearchAlgorithm implements IAlgorithm {

    private final IMinimizingSolutionObjectiveFunction objectiveFunction;
    private final BiFunction<Solution, Timer, ISolutionNeighbourhoodIterator> neighbourhoodIteratorCreator;
    private final ISolutionConstraint constraint;
    private final Timer timer;

    @Override
    public Solution run(Solution initalSolution) {

        Solution incumbent = initalSolution;
        double incumbentScore = objectiveFunction.score(incumbent);

        int i = 0;
        while (timer.isActive()) {
            ISolutionNeighbourhoodIterator neighbourhoodIterator = neighbourhoodIteratorCreator.apply(incumbent, timer);

            while (neighbourhoodIterator.hasNext()) {
                Solution neighbour = neighbourhoodIterator.next();
                double neighbourScore = objectiveFunction.score(neighbour);

                if (neighbourScore < incumbentScore) {
                    incumbent = neighbour;
                    incumbentScore = neighbourScore;
                    break;
                }
            }

            System.out.format("Iteration %d, %s%n", i, objectiveFunction.stats(incumbent));
            i++;
        }

        if (!constraint.checkConstraint(incumbent)) {
            throw new IllegalStateException("Incumbent score does not satisfy all constraints.");
        }
        return incumbent;
    }
}
