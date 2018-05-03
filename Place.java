import java.util.Comparator;

class Place {

    private long x, y;

    public Place(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public Place(Place original) {
        this(original.x, original.y);
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Place) {
            Place p = (Place)o;
            return (this.x == p.x) && (this.y == p.y);
        } else return false;
    }

    public long getX() { return this.x; }
    public long getY() { return this.y; }

    public static class XComparator implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            return (int)(o1.getX() - o2.getX());
        }
    }

    public static class YComparator implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            return (int)(o1.getY() - o2.getY());
        }
    }

}