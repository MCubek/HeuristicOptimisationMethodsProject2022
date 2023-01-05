package hr.fer.hom.project.output;

import hr.fer.hom.project.model.Solution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    private FileUtil() {
    }

    public static void outputSolutionToFile(Solution solution, String fileName) throws IOException {
        try (var writer = Files.newBufferedWriter(Path.of(fileName))) {
            String output = solution.toString();
            writer.write(output.substring(0, output.length() - 2));
        }
    }

}
