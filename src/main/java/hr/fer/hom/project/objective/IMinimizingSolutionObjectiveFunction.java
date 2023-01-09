package hr.fer.hom.project.objective;

import hr.fer.hom.project.model.Solution;

/**
 * @author matejc
 * Created on 03.01.2023.
 */

public interface IMinimizingSolutionObjectiveFunction {
    double score(Solution solution);

    String stats(Solution solution);

    long numCalls();
}
