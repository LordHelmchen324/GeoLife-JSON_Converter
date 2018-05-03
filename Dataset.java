import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class Dataset {

    private List<Trajectory> trajectories = new LinkedList<Trajectory>();

    public Dataset() { }

    public Dataset(Dataset original) {
        for (Trajectory t : original.trajectories) this.add(new Trajectory(t));
    }

    @Override
    public String toString() {
        final String lineSeperator = "--------------";
        String s = "" + lineSeperator;
        for (Trajectory t : this.trajectories) s += "\n" + t.toString();
        s += "\n" + lineSeperator;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Dataset) {
            Dataset d = (Dataset)o;
            if (this.size() != d.size()) return false;

            List<Trajectory> trajectoriesCopy = new LinkedList<Trajectory>(this.trajectories);
            for (int i = 0; i < d.size(); i++) {
                Trajectory t = d.getTrajectories().get(i);
                boolean found = false;
                for (int j = 0; j < trajectoriesCopy.size(); j++) {
                    if (trajectoriesCopy.get(j).equals(t)) {
                        trajectoriesCopy.remove(j);
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void add(Trajectory t) {
        this.trajectories.add(t);
    }

    public void remove(Trajectory t) {
        this.trajectories.remove(t);
    }

    public int size() {
        return this.trajectories.size();
    }

    public List<Trajectory> getTrajectories() {
        return this.trajectories;
    }

}