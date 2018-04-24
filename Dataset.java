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

    // MDAV

    public Trajectory closestTrajectoryTo(Trajectory t, DistanceMeasure dM) {
        if (this.trajectories.isEmpty()) {
            System.err.println("Cannot return clostest Trajectory to t = " + t + " from within empty Dataset!");
            System.exit(1);
        }

        double minDistance = Double.MAX_VALUE;
        Trajectory closest = null;
        for (Trajectory t2 : this.trajectories) {
            double distance = dM.computeDistance(t, t2);
            if (distance < minDistance || closest == null) {
                minDistance = distance;
                closest = t2;
            }
        }

        return closest;
    }

    public Trajectory furthestTrajectoryTo(Trajectory t, DistanceMeasure dM) {
        if (this.trajectories.isEmpty()) {
            System.err.println("Cannot return furthest Trajectory to t = " + t + " from within empty Dataset!");
            System.exit(1);
        }

        double maxDistance = 0.0;
        Trajectory furthest = null;
        for (Trajectory t2 : this.trajectories) {
            double distance = dM.computeDistance(t, t2);
            if (distance > maxDistance || furthest == null) {
                maxDistance = distance;
                furthest = t2;
            }
        }

        return furthest;
    }

    public List<Trajectory> removeClusterAround(Trajectory t, int size, DistanceMeasure dM) {
        List<Trajectory> cluster = new LinkedList<Trajectory>();
        cluster.add(t);
        this.remove(t);

        for (int i = 0; i < size - 1; i++) {
            Trajectory closest = this.closestTrajectoryTo(t, dM);
            cluster.add(closest);
            this.remove(closest);
        }

        return cluster;
    }

    public void fillUpToEqualLength() {
        if (this.trajectories.isEmpty()) {
            System.err.println("Tried to fill up empty Dataset!");
            return;
        }

        // find longest
        Iterator<Trajectory> i = this.trajectories.iterator();
        Trajectory longest = i.next();
        while (i.hasNext()) {
            Trajectory t = i.next();
            if (t.length() > longest.length()) longest = t;
        }

        // fill up all the trajectories to the length of the longest
        for (Trajectory t : this.trajectories) t.lengthenToEqualLengthAs(longest);
    }

    public Dataset protectedByMDAV(int k, DistanceMeasure dM, MedianStrategy mS) {
        Dataset temp = new Dataset(this);
        List<List<Trajectory>> clusters = new LinkedList<List<Trajectory>>();

        while (temp.size() > k) {
            Trajectory avrg = mS.computeMedian(temp.trajectories);
            Trajectory furthest = temp.furthestTrajectoryTo(avrg, dM);
            clusters.add(temp.removeClusterAround(furthest, k, dM));

            if (temp.size() > k) {
                Trajectory furthest2 = temp.furthestTrajectoryTo(avrg, dM);
                clusters.add(temp.removeClusterAround(furthest2, k, dM));
            }
        }

        List<Trajectory> lastCluster = new LinkedList<Trajectory>();
        Iterator<Trajectory> it = temp.trajectories.iterator();
        while (it.hasNext()) {
            Trajectory t = it.next();
            lastCluster.add(t);
            it.remove();
        }
        clusters.add(lastCluster);

        Dataset result = new Dataset();
        for (List<Trajectory> c : clusters) {
            Trajectory clusterMedian = mS.computeMedian(c);
            for (int i = 0; i < c.size(); i++) result.add(clusterMedian);
        }

        return result;
    }

}