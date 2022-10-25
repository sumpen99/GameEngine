package helper.struct;

import helper.io.IOHandler;

import static helper.methods.CommonMethods.buildInvertPointArr;

//https://handmade.network/forums/articles/t/7330-implementing_a_font_reader_and_rasterizer_from_scratch%252C_part_1__ttf_font_reader.
public class Glyf {
    public int numberOfContours,xMin,yMin,xMax,yMax,instructionLength;
    public short[] endPtsOfContours;
    public int[] xCoordinates,yCoordinates;
    public byte[] instructions,flags;
    public Point[][] pointList;
    public static int globalCounter;
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

    public void generatePoints(char c){
        assert (xCoordinates.length == yCoordinates.length) && xCoordinates.length >0 : "MisMatch Between XPoints And YPoints";
        int i = 0,startIdx=0,endIdx;
        pointList = new Point[numberOfContours][];
        while(i<numberOfContours){
            int contourStartIndex = startIdx;
            int genereatedPointsStartIndex = 0;
            int contourStart = 1;
            int contourStartedOff = 0;
            int cnt = 0;
            int contourLen = endPtsOfContours[i] - contourStartIndex + 1;
            endIdx = endPtsOfContours[i];
            Point[] points = new Point[128];
            while(startIdx<=endIdx){
                float x = xCoordinates[startIdx];
                float y = yMin-yCoordinates[startIdx];

                int nextIndex = (startIdx+1 - contourStartIndex)%contourLen+contourStartIndex;

                if(getOnCurve(startIdx)){
                    points[cnt++] = new Point(x,y);
                }
                else{
                    if(contourStart != 0){
                        contourStartedOff = 1;
                        if(getOnCurve(nextIndex)){
                            points[cnt++] = new Point(xCoordinates[nextIndex],yMin-yCoordinates[nextIndex]);
                            startIdx+=2;
                            continue;
                        }
                        x = x + (xCoordinates[nextIndex] - x) / 2.0f;
                        y = y + ((yMin-yCoordinates[nextIndex] - y)) / 2.0f;
                        points[cnt++] = new Point(x,y);
                    }
                    Point p0 = points[cnt-1];
                    Point p1 = new Point(x,y);
                    Point p2 = new Point(xCoordinates[nextIndex],yMin-yCoordinates[nextIndex]);

                    if(!getOnCurve(nextIndex)){
                        p2.x = p1.x + (p2.x-p1.x)/2.0f;
                        p2.y = p1.y + (p2.y-p1.y)/2.0f;
                    }
                    else{
                        startIdx++;
                    }
                    tessellateBezier(points,cnt-1,p0,p1,p2);
                    cnt+=globalCounter;
                }
                contourStart = 0;
                startIdx++;
            }

            int idx = Math.min(startIdx-1,flags.length-1);
            if(getOnCurve(idx)){
                points[cnt++] = new Point(points[genereatedPointsStartIndex].x,points[genereatedPointsStartIndex].y);
            }
            if(contourStartedOff != 0){
                Point p0 = points[cnt-1];
                Point p1 = new Point(xCoordinates[contourStartIndex],yMin-yCoordinates[contourStartIndex]);
                Point p2 = points[genereatedPointsStartIndex];
                tessellateBezier(points,cnt-1,p0,p1,p2);
            }
            pointList[i] = points;
            startIdx = endIdx+1;
            i++;
        }
        xCoordinates = null;
        yCoordinates = null;
    }

    public void tessellateBezier(Point[] output,int currentIndex,Point p0,Point p1,Point p2){
        int subDivInto = 2,i=1;
        float stepPerIter = 1.0f/(float)subDivInto;
        while(i<=subDivInto){
            float t = i*stepPerIter;
            float t1 = (1.0f-t);
            float t2 = t*t;
            float x = t1*t1*p0.x + 2*t1*t*p1.x + t2*p2.x;
            float y = t1*t1*p0.y + 2*t1*t*p1.y + t2*p2.y;
            output[currentIndex+i] = new Point(x,y);
            i++;
        }
        globalCounter = i-1;
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
