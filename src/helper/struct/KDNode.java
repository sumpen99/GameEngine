package helper.struct;

public class KDNode {
    public double lat;
    public double lon;
    public double distance;
    public String id;
    public KDNode left;
    public KDNode right;
    public boolean found;

    public KDNode(double lat,double lon,String id,double dist){
        this.lat = lat;
        this.lon = lon;
        this.id = id;
        this.distance = dist;
    }

    public double compare(int axis){
        return axis == 0 ? lat : lon;
    }

    public void swapValues(double lat,double lon,double distance,String id,KDNode left,KDNode right,boolean found){
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
        this.id = id;
        this.left = left;
        this.right = right;
        this.found = found;
    }
}

