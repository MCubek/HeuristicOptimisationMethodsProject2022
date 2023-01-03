package hr.fer.hom.project.algorithm;

import hr.fer.hom.project.model.Solution;

/**
 * @author matejc
 * Created on 03.01.2023.
 */

@FunctionalInterface
public interface IAlgorithm {
    Solution run(Solution initalSolution);
}
