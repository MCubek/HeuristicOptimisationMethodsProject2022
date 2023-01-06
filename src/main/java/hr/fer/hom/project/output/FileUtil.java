package hr.fer.hom.project.output;

import hr.fer.hom.project.model.Solution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    private FileUtil() {
    }

    public static void outputSolutionToFile(Solution solution, Path file) throws IOException {
        try (var writer = Files.newBufferedWriter(file)) {
            String output = solution.toString();
            writer.write(output);
        }
    }

}
