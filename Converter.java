import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Converter {

    public static void main(String[] args) {
        String path = "../Geolife Trajectories 1.3/Data/000/Trajectory/20081023025304.plt";
        File file = new File(path);

        try (BufferedReader r = new BufferedReader(new FileReader(file));) {
            String line = r.readLine();
            while (line != null) {
                System.out.println(line);
                line = r.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file at path \"" + path + "\".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("An I/O exception occured: " + e.getLocalizedMessage());
            System.exit(1);
        }
    }

}