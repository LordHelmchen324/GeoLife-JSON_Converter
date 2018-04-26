import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    private File readDirectory;

    public Parser(String readPath) {
        this.readDirectory = new File(readPath);
        System.out.println("Created Parser for path \"" + this.readDirectory.getAbsolutePath() + "\"\n");
    }

    public Dataset parseDataset() {
        System.out.println("Beginning to parse at path \"" + this.readDirectory.getAbsolutePath() + "\"");

        System.out.print("Listing all files ... ");

        List<File> allFiles = listAllFiles(this.readDirectory);

        List<File> pltFiles = new LinkedList<File>();
        for (File f : allFiles) {
            if (f.getName().endsWith(".plt")) pltFiles.add(f);
        }

        System.out.print(pltFiles.size() + " .plt files found\n");

        Dataset d = new Dataset();
        for (File f : pltFiles) d.add(parseTrajectoryFromFile(f));

        return d;
    }

    private Trajectory parseTrajectoryFromFile(File file) {
        System.out.println("Parsing Trajectory from file \"" + file.getAbsolutePath() + "\"");

        Trajectory t = new Trajectory();

        try (BufferedReader r = new BufferedReader(new FileReader(file));) {
            // skip 6 lines at the top of the file
            for (int i = 0; i < 6; i++) r.readLine();

            String line = r.readLine();
            while (line != null) {
                Place parsedPlace = parsePlaceFromLine(line);
                t.add(parsedPlace);

                line = r.readLine();
            }

            System.out.println("Found Trajectory of length " + t.length());

            return t;
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file at path \"" + file.getName() + "\".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("An I/O exception occured: " + e.getLocalizedMessage());
            System.exit(1);
        }

        return null;
    }

    private Place parsePlaceFromLine(String line) {
        String[] items = line.split(",");

        double pltX = Double.parseDouble(items[1]);
        double pltY = Double.parseDouble(items[0]);

        int jsonX = (int)(pltX * 10000.0);
        int jsonY = (int)(pltY * 10000.0);

        try {
            String dateString = items[5] + "," + items[6];
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            Date date = dateFormat.parse(dateString);
            int jsonT = (int)(date.getTime() / 100);  // to the thenth of a second (UNIX time * 10)

            return new Place(jsonX, jsonY, jsonT);
        } catch (ParseException e) {
            System.err.println("Could not parse date in the following line of a .plt file:\n  " + line);
            System.exit(1);
        }

        return null;
    }

    private List<File> listAllFiles(File directory) {
        File[] fileArray = directory.listFiles();
        List<File> fileList = new LinkedList<File>();
        for (File f : fileArray) fileList.add(f);

        List<File> moreFiles = new LinkedList<File>();
        for (File f : fileList) {
            if (f.isDirectory()) moreFiles.addAll(listAllFiles(f));
        }

        fileList.addAll(moreFiles);

        return fileList;
    }
    
}