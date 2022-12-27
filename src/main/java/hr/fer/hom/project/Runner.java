package hr.fer.hom.project;

import hr.fer.hom.project.loader.InstanceLoader;
import hr.fer.hom.project.model.Instance;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author matejc
 * Created on 25.12.2022.
 */

public class Runner {
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Path of file required as argument!");

        Path file = Path.of(args[0]);

        try {
            Instance instance = InstanceLoader.loadInstance(file);

            System.out.println("Instance loaded.");
        } catch (IOException e) {
            System.err.println("Error while loading file.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
