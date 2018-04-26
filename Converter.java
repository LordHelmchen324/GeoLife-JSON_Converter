public class Converter {

    public static void main(String[] args) {
        Parser p = new Parser("../Geolife Trajectories 1.3/Data/");
        Dataset d = p.parseDataset();

        System.out.println("Size of the dataset = " + d.size());
    }

}