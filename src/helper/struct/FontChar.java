package helper.struct;

public class FontChar {
    public int x,y,width,height,lsb,rsb;
    public char charValue;
    public Spline[] splines;

    public FontChar(char ch,int xMin,int xMax,int yMin,int yMax,int leftSideBearing,int advanceWidth,Spline[] sps){
        charValue = ch;
        x = xMin;
        y = yMin;
        width = xMax-xMin;
        height = yMax-yMin;
        lsb = leftSideBearing;
        rsb = advanceWidth - (leftSideBearing + xMax - xMin);
        splines = sps;
    }
}
