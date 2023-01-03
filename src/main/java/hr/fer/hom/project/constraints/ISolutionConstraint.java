package hr.fer.hom.project.constraints;

import hr.fer.hom.project.model.Solution;

/**
 * @author matejc
 * Created on 03.01.2023.
 */

@FunctionalInterface
public interface ISolutionConstraint {
    boolean checkConstraint(Solution solution);
}
