import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class Converter {

    public static void main(String[] args) {
        String path = "../Geolife Trajectories 1.3/Data/000/Trajectory/20081023025304.plt";
        File file = new File(path);

        Trajectory t = readTrajectoryFromFile(file);
        System.out.println("Length: " + t.length() + "\n");
        List<Place> places = t.getPlaces();
        for (Place p : places) {
            System.out.println(p);
        }
    }

    public static Trajectory readTrajectoryFromFile(File file) {
        Trajectory t = new Trajectory();

        try (BufferedReader r = new BufferedReader(new FileReader(file));) {
            // skip 6 lines at the top of the file
            for (int i = 0; i < 6; i++) r.readLine();

            String line = r.readLine();
            while (line != null) {
                Place parsedPlace = readPlaceFromLine(line);
                t.add(parsedPlace);

                line = r.readLine();
            }

            return t;
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file at path \"" + file.toPath() + "\".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("An I/O exception occured: " + e.getLocalizedMessage());
            System.exit(1);
        }

        return null;
    }

    public static Place readPlaceFromLine(String line) {
        String[] items = line.split(",");

        double pltX = Double.parseDouble(items[1]);
        double pltY = Double.parseDouble(items[0]);

        int jsonX = (int)(pltX * 10000.0);
        int jsonY = (int)(pltY * 10000.0);

        try {
            String dateString = items[5] + "," + items[6];
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            Date date = dateFormat.parse(dateString);
            int jsonT = (int)(date.getTime() / 1000);  // to the second (UNIX time)

            return new Place(jsonX, jsonY, jsonT);
        } catch (ParseException e) {
            System.err.println("Could not parse date in the following line of a .plt file:\n  " + line);
            System.exit(1);
        }

        return null;
    }

}