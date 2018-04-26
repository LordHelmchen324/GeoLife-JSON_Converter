import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Trajectory {

    private List<Place> places = new ArrayList<Place>();

    public Trajectory() { }

    @Override
    public String toString() {
        String s = "[";
        Iterator<Place> i = this.places.iterator();
        while (i.hasNext()) {
            Place p = i.next();
            s += p.toString();
            if (i.hasNext()) s += ", ";
        }
        s += "]";
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Trajectory) {
            Trajectory t = (Trajectory)o;
            if (this.length() != t.length()) return false;
            for (int i = 0; i < this.length(); i++) {
                if (!this.places.get(i).equals(t.places.get(i))) return false;
            }
            return true;
        } else return false;
    }

    public int length() {
        return this.places.size();
    }

    public Place getPlaceAtIndex(int i) {
        return places.get(i);
    }

    public List<Place> getPlaces() {
        return this.places;
    }

    public void add(Place p) {
        if (this.length() == 0) {
            this.places.add(p);
            return;
        }

        int i = 0;
        while (i < this.length() && this.getPlaceAtIndex(i).getT() < p.getT()) i++;

        if (i == this.length()) {
            this.places.add(p);
        } else if (this.getPlaceAtIndex(i).getT() == p.getT()) {
            System.err.println("Illegaly attempted to add Place " + p + " with same t as the Place at index " + i + " to a Trajectory!");
            System.exit(1);
        } else {
            this.places.add(i, p);
        }
    }

}