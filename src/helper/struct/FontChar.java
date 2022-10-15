package helper.struct;

public class FontChar {
    public short x,y,width,height,lsb,rsb;
    public char charValue;

    public FontChar(char ch,short xMin,short xMax,short yMin,short yMax,short leftSideBearing,short advanceWidth){
        charValue = ch;
        x = xMin;
        y = yMin;
        width = (short)(xMax-xMin);
        height = (short)(yMax-yMin);
        lsb = leftSideBearing;
        rsb = (short)(advanceWidth - leftSideBearing - (xMax-xMin));
    }
}
