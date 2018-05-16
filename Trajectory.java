import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Trajectory {

    private Map<Long, Place> places = new HashMap<Long, Place>();
    public int id = -1;

    public Trajectory() { }

    public Trajectory(Trajectory original) {
        for (Map.Entry<Long, Place> entry : original.places.entrySet()) {
            long t = entry.getKey();
            Place p = new Place(entry.getValue());
            this.places.put(t, p);
        }
        this.id = original.id;
    }

    @Override
    public String toString() {
        return "Trajectory " + this.id + ": " + this.places.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Trajectory) {
            Trajectory r = (Trajectory)o;
            return this.places.equals(r.places);
        } else return false;
    }

    public int length() {
        return this.places.size();
    }

    public Place getPlaceAtTime(long t) {
        return this.places.get(t);
    }

    public Set<Long> getTimestamps() {
        return this.places.keySet();
    }

    public List<Place> getPlaces() {
        return new ArrayList<Place>(this.places.values());
    }

    public void add(long t, Place p) {
        if (t < 0) {
            System.err.println("Illegaly attempted to add Place (" + p.getX() + "," + p.getY() + ") with t = " + t + " < 0 !");
        }
        this.places.put(t, p);
    }

    // Autocorrelation

    public double averageX() {
        int sum = 0;
        for (Place p : this.places.values()) {
            sum += p.getX();
        }
    
        return (double)sum / (double)this.length();
    }

    public double averageY() {
        int sum = 0;
        for (Place p : this.places.values()) {
            sum += p.getY();
        }
    
        return (double)sum / (double)this.length();
    }

    private double gammaPrimeA(double h) {
        int iterCount = this.length() - (int)Math.abs(h);
        List<Place> placeValues = new ArrayList<Place>(this.places.values());

        double result = 0.0;
        for (int i = 0; i < iterCount; i++) {
            Place place = placeValues.get(i);
            Place shiftedPlace = placeValues.get(i + (int)Math.abs(h));
            
            result += ((double)shiftedPlace.getX() - this.averageX()) * ((double)place.getX() - this.averageX()) + ((double)shiftedPlace.getY() - this.averageY()) * ((double)place.getY() - this.averageY());
        }
    
        return result / (double)this.length();
    }

    public double autocorrelation(double h) {
        return this.gammaPrimeA(h) / this.gammaPrimeA(0);
    }


    // others

    public void lengthenToEqualLengthAs(Trajectory other) {
        if (other.length() == this.length()) return;
        if (this.length() > other.length()) {
            System.err.println("Attempted to lengthen Trajectory to length of shorter one!");
            return;
        }

        int missingCount = other.length() - this.length();

        for ( ; missingCount > 0; missingCount--) {
            if ((missingCount & 1) == 0) {  // even: add one at the end
                long lastT = Collections.max(this.places.keySet());
                Place lastPlace = this.places.get(lastT);
                this.add(lastT + 1, new Place(lastPlace));
            } else {                        // odd: add at the start if possible, end otherwise
                long firstT = Collections.min(this.places.keySet());
                if (firstT < 1) {
                    long lastT = Collections.max(this.places.keySet());
                    Place lastPlace = this.places.get(lastT);
                    this.add(lastT + 1, new Place(lastPlace));
                } else {
                    Place firstPlace = this.places.get(firstT);
                    this.add(firstT - 1, new Place(firstPlace));
                }
            }
        }
    }

}