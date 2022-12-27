package hr.fer.hom.project.model;

/**
 * @author matejc
 * Created on 27.12.2022.
 */


public record Customer(int customerNumber, int xCoord, int yCoord, int demand, int readyTime, int dueDate,
                       int serviceTime) {

}
