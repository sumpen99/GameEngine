package helper.struct;

import static helper.methods.CommonMethods.buildInvertPointArr;

//https://handmade.network/forums/articles/t/7330-implementing_a_font_reader_and_rasterizer_from_scratch%252C_part_1__ttf_font_reader.
public class Glyf {
    public int numberOfContours,xMin,yMin,xMax,yMax,instructionLength;
    public short[] endPtsOfContours;
    public int[] xCoordinates,yCoordinates;
    public byte[] instructions,flags;
    public Point[][] pointList;
    public Glyf(short contours,short xmin,short ymin,short xmax,short ymax){
        numberOfContours = contours;
        xMin = xmin;
        yMin = ymin;
        xMax = xmax;
        yMax = ymax;
    }

    public void splitCoordinates(){
        int i = 0,startIdx=0,endIdx;
        pointList = new Point[numberOfContours][];
        while(i<numberOfContours){
            endIdx = endPtsOfContours[i];
            pointList[i] = buildInvertPointArr(xCoordinates,yCoordinates,0,yMin,startIdx,endIdx);
            startIdx = endIdx+1;
            i++;
        }
        xCoordinates = null;
        yCoordinates = null;
    }

    // Bit 1: if set it means this point is on the Glyphs curve, otherwise the point is off curve.
    // Bit 2: if set the corrosponding x coordiante is 1 byte otherwise its 2 bytes.
    // Bit 3: if set the corrosponding y coordinate is 1 byte otherwise its 2 bytes.
    // Bit 4: (repeat) if set the next byte specifies how many timet his flag repeats. this is a small way to compress the flags array.
    // Bit 5, 6: these both relate to bit 1 and 2 respectivily and are better explained in a table.
    // Bit 7, 8: those are reserved and set to zero in TrueType.

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
