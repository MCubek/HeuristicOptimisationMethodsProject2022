package hr.fer.hom.project.objective;

import hr.fer.hom.project.model.Solution;

/**
 * @author matejc
 * Created on 03.01.2023.
 */

public record SolutionObjectiveFunction(double alpha, double beta) implements IMinimizingSolutionObjectiveFunction {

    @Override
    public double score(Solution solution) {
        return Math.pow(solution.getNumberOfVehicles(), alpha) + Math.pow(solution.getAllRoutesTime(), beta);
    }

    @Override
    public String stats(Solution solution) {
        return String.format("Vehicles: %d, Time: %d, Score: %.3f",
                solution.getNumberOfVehicles(),
                solution.getAllRoutesTime(),
                score(solution));
    }
}
