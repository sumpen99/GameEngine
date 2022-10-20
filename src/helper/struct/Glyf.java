package helper.struct;

public class Glyf {
    public int numberOfContours,xMin,yMin,xMax,yMax,instructionLength;
    public short[] endPtsOfContours;
    public int[] xCoordinates,yCoordinates;
    public byte[] instructions,flags;
    public Glyf(short contours,short xmin,short ymin,short xmax,short ymax){
        numberOfContours = contours;
        xMin = xmin;
        yMin = ymin;
        xMax = xmax;
        yMax = ymax;
    }

    public boolean getOnCurve(int index){
        return (0x01 & flags[index]) != 0;
    }

    public boolean getXShort(int index){
        return (0x02 & flags[index]) != 0;
    }

    public boolean getYShort(int index){
        return (0x04 & flags[index]) != 0;
    }

    public boolean getRepeat(int index){
        return (0x08 & flags[index]) != 0;
    }

    public boolean getXShortPos(int index){
        return (0x10 & flags[index]) != 0;
    }

    public boolean getYShortPos(int index){
        return (0x20 & flags[index]) != 0;
    }

    public boolean getReservedOne(int index){
        return (0x40 & flags[index]) != 0;
    }

    public boolean getReservedTwo(int index){
        return (0x80 & flags[index]) != 0;
    }

    public byte onCurve(int index){
        return (byte)(getOnCurve(index) ? 1 : 0);
    }

    public byte xShort(int index){
        return (byte)(getXShort(index) ? 1 : 0);
    }

    public byte yShort(int index){
        return (byte)(getYShort(index) ? 1 : 0);
    }

    public byte repeat(int index){
        return (byte)(getRepeat(index) ? 1 : 0);
    }

    public byte xShortPos(int index){
        return (byte)(getXShortPos(index) ? 1 : 0);
    }

    public byte yShortPos(int index){
        return (byte)(getYShortPos(index) ? 1 : 0);
    }

    public byte reservedOne(int index){
        return (byte)(getReservedOne(index) ? 1 : 0);
    }

    public byte reservedTwo(int index){
        return (byte)(getReservedTwo(index) ? 1 : 0);
    }
}
