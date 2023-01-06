package hr.fer.hom.project.neighbourhood;

import hr.fer.hom.project.constraints.ISolutionConstraint;
import hr.fer.hom.project.model.Solution;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

/**
 * @author matejc
 * Created on 06.01.2023.
 */

@RequiredArgsConstructor
public class ValidSolutionNeighbourHoodIterator implements ISolutionNeighbourhoodIterator {

    private final ISolutionNeighbourhoodIterator sometimesValidSolutionNeighbourhoodIterator;
    private final ISolutionConstraint solutionConstraint;
    private final int maxIterations;

    private int iteration = 0;

    @Override
    public boolean hasNext() {
        return sometimesValidSolutionNeighbourhoodIterator.hasNext()
                && iteration <= maxIterations;
    }

    @Override
    public Solution next() {
        if (!hasNext()) throw new NoSuchElementException();

        Solution nextValidSolution;
        do {
            nextValidSolution = sometimesValidSolutionNeighbourhoodIterator.next();
            iteration += 1;
        } while (!nextValidSolution.satisfiesConstraint(solutionConstraint));
        return nextValidSolution;
    }
}
