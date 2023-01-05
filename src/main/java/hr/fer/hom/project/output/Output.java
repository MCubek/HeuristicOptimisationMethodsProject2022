package hr.fer.hom.project.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import hr.fer.hom.project.model.Solution;

public class Output {
	
	public static void outputSolutionToFile(Solution solution, String fileName) {

	    try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		    String output = solution.toString();
			writer.write(output.substring(0, output.length()-2));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
