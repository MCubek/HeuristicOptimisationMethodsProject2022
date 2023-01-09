package hr.fer.hom.project.objective;

import hr.fer.hom.project.model.Solution;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * @author matejc
 * Created on 03.01.2023.
 */

@RequiredArgsConstructor
public final class SolutionObjectiveFunction implements IMinimizingSolutionObjectiveFunction {
    private final double alpha;
    private final double beta;

    private long numberOfCalls = 0L;

    @Override
    public double score(Solution solution) {
        numberOfCalls++;
        return Math.pow(solution.getNumberOfVehicles(), alpha) + Math.pow(solution.getAllRoutesTime(), beta);
    }

    @Override
    public String stats(Solution solution) {
        return String.format("Vehicles: %d, Time: %d, Score: %.3f",
                solution.getNumberOfVehicles(),
                solution.getAllRoutesTime(),
                score(solution));
    }

    @Override
    public long numCalls() {
        return numberOfCalls;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SolutionObjectiveFunction) obj;
        return Double.doubleToLongBits(this.alpha) == Double.doubleToLongBits(that.alpha) &&
                Double.doubleToLongBits(this.beta) == Double.doubleToLongBits(that.beta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, beta);
    }

    @Override
    public String toString() {
        return "SolutionObjectiveFunction[" +
                "alpha=" + alpha + ", " +
                "beta=" + beta + ']';
    }

}
