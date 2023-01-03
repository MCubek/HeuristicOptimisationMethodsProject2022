package hr.fer.hom.project.model;

/**
 * @author matejc
 * Created on 27.12.2022.
 */


public record Customer(int customerNumber, int xCoord, int yCoord, int demand, int readyTime, int dueDate,
                       int serviceTime) {

    public double calculateDistance(Customer otherCustomer) {
        return Math.sqrt(Math.pow(1.0 * xCoord - otherCustomer.xCoord, 2) + Math.pow(1.0 * yCoord - otherCustomer.yCoord, 2));
    }

    public int calculateDistanceCeil(Customer otherCustomer) {
        return (int) Math.ceil(calculateDistance(otherCustomer));
    }

}
