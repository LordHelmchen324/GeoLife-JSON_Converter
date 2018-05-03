import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class Converter {

    public static void main(String[] args) {
        System.out.println("GeoLife to JSON Converter started ...\n");

        Parser p = new Parser("../Geolife Trajectories 1.3/Data/");
        Dataset d = p.parseDataset();

        System.out.println("\nSize of the dataset = " + d.size() + "\n");

        File outputFile = new File("../Geolife Trajectories 1.3/translated.json");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(outputFile))) {
            System.out.print("Writing Dataset to JSON file at path \"" + outputFile.getAbsolutePath() + "\" ... ");

            Gson gson = new Gson();
            gson.toJson(d, w);

            System.out.print("done!\n\n");
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file at path \"" + outputFile.getName() + "\".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("An I/O exception occured: " + e.getLocalizedMessage());
            System.exit(1);
        }
    }

}