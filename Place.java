import java.util.Comparator;

class Place {

    private long x, y, t;

    public Place(long x, long y, long t) {
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

    public long getX() { return this.x; }
    public long getY() { return this.y; }
    public long getT() { return this.t; }

    public static class TXYComparator implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            int compT = (int)(o1.getT() - o2.getT());
            if (compT != 0) return compT;

            int compX = (int)(o1.getX() - o2.getX());
            if (compX != 0) return compX;

            int compY = (int)(o1.getY() - o2.getY());
            if (compY != 0) return compY;

            return 0;
        }
    }

}