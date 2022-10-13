package helper.struct;

public class Glyf {
    public short numberOfContours,xMin,yMin,xMax,yMax;
    public Glyf(short contours,short xmin,short ymin,short xmax,short ymax){
        numberOfContours = contours;
        xMin = xmin;
        yMin = ymin;
        xMax = xmax;
        yMax = ymax;
    }
}
