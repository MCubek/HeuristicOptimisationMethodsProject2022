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

    @Override
    public boolean hasNext() {
        return sometimesValidSolutionNeighbourhoodIterator.hasNext();
    }

    @Override
    public Solution next() {
        if (!hasNext()) throw new NoSuchElementException();

        Solution nextValidSolution;
        do {
            nextValidSolution = sometimesValidSolutionNeighbourhoodIterator.next();
        } while (!nextValidSolution.satisfiesConstraint(solutionConstraint));
        return nextValidSolution;
    }
}
