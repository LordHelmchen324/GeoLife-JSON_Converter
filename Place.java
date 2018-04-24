import java.util.Comparator;

class Place {

    private int x, y, t;

    public Place(int x, int y, int t) {
        if (t < 0) {
            System.err.println("Illegaly attempted to create Place (" + x + "," + y + "," + t + ") with t < 0 !");
            System.exit(1);
        }

        this.x = x;
        this.y = y;
        this.t = t;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + "," + this.t + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Place) {
            Place p = (Place)o;
            return (this.x == p.x) && (this.y == p.y) && (this.t == p.t);
        } else return false;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getT() { return this.t; }

    public static class TComparator implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            return o1.getT() - o2.getT();
        }
    }

}