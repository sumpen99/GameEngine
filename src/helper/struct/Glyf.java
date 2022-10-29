package helper.struct;
import helper.io.IOHandler;
import helper.sort.QuickSort;
import java.util.Arrays;

import static helper.methods.CommonMethods.buildInvertPointArr;

//https://handmade.network/forums/articles/t/7330-implementing_a_font_reader_and_rasterizer_from_scratch%252C_part_1__ttf_font_reader.
public class Glyf {
    public int numberOfContours,xMin,yMin,xMax,yMax,instructionLength,edgeCount;
    public float fxMin,fyMin,fxMax,fyMax;
    public short[] endPtsOfContours;
    public int[] xCoordinates,yCoordinates;
    public byte[] instructions,flags;
    public Point[][] pointList;
    public static int globalCounter;
    public final int bitmapWidth = 64,bitmapHeight=64;
    public Edge[] lines;
    public short[] texture;
    public Glyf(short contours,int xmin,int ymin,int xmax,int ymax){
        numberOfContours = contours;
        xMin = xmin;
        yMin = ymin;
        xMax = xmax;
        yMax = ymax;
        texture = new short[bitmapWidth*bitmapHeight];
    }

    public void flipTextureVertical(){
        int c,r=0,j;
        while(r<bitmapHeight/2) {
            c = bitmapHeight - 1 - r;
            j = 0;
            while (j < bitmapWidth) {
                short temp = texture[r * bitmapWidth + j];
                texture[r * bitmapWidth + j] = texture[c * bitmapWidth + j];
                texture[c * bitmapWidth + j] = temp;
                j++;
            }
            r++;
        }
    }

    public void setFloatBoundaries(){
        fxMin = xMin;
        fyMin = yMin;
        fxMax = xMax;
        fyMax = yMax;
        int i = 0,j;
        while(i<numberOfContours){
            j = 0;
            while(j<pointList[i].length){
                if(pointList[i][j].x <= fxMin){fxMin = pointList[i][j].x;}
                if(pointList[i][j].x >= fxMax){fxMax = pointList[i][j].x;}
                if(pointList[i][j].y <= fyMin){fyMin = pointList[i][j].y;}
                if(pointList[i][j].y >= fyMax){fyMax = pointList[i][j].y;}
                j++;
            }
            i++;
        }
    }

    public void coordinatesInsideRange(){
        int i = 0,j;
        while(i<numberOfContours){
            j = 0;
            while(j<pointList[i].length){
                if(pointList[i][j].x < xMin || pointList[i][j].x > xMax){IOHandler.printString("X outside Range: %f  fXmin: %d  fXMax: %d".formatted(pointList[i][j].x,xMin,xMax));}
                if(pointList[i][j].y < yMin || pointList[i][j].y > yMax){IOHandler.printString("Y outside Range: %f  fYmin: %d  fYMax: %d".formatted(pointList[i][j].y,yMin,yMax));}
                j++;
            }
            i++;
        }
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
    }

    public void flipYCoordinate(){
        int i = 0,j;
        while(i<numberOfContours){
            j = 0;
            while(j<pointList[i].length){
                pointList[i][j].y = yMin - pointList[i][j].y;
                j++;
            }
            i++;
        }
    }

    public void clearBuffers(){
        xCoordinates = null;
        yCoordinates = null;
        pointList = null;
        lines = null;
    }

    //https://handmade.network/forums/wip/t/7610-reading_ttf_files_and_rasterizing_them_using_a_handmade_approach,_part_2__rasterization
    public void generatePoints(){
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
                float y = yCoordinates[startIdx];

                int nextIndex = (startIdx+1 - contourStartIndex)%contourLen+contourStartIndex;

                if(getOnCurve(startIdx)){
                    points[cnt++] = new Point(x,y);
                }
                else{
                    if(contourStart != 0){
                        contourStartedOff = 1;
                        if(getOnCurve(nextIndex)){
                            points[cnt++] = new Point(xCoordinates[nextIndex],yCoordinates[nextIndex]);
                            startIdx+=2;
                            continue;
                        }
                        x = x + (xCoordinates[nextIndex] - x) / 2.0f;
                        y = y + (yCoordinates[nextIndex] - y) / 2.0f;
                        points[cnt++] = new Point(x,y);
                    }
                    Point p0 = points[cnt-1];
                    Point p1 = new Point(x,y);
                    Point p2 = new Point(xCoordinates[nextIndex],yCoordinates[nextIndex]);

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
                Point p1 = new Point(xCoordinates[contourStartIndex],yCoordinates[contourStartIndex]);
                Point p2 = points[genereatedPointsStartIndex];
                tessellateBezier(points,cnt-1,p0,p1,p2);
                cnt+=globalCounter;
            }
            pointList[i] = Arrays.copyOf(points,cnt);
            edgeCount+=pointList[i].length;
            startIdx = endIdx+1;
            i++;
        }
        xCoordinates = null;
        yCoordinates = null;
    }

    public void generateEdges(){
        int i=0,j,cnt = 0;
        edgeCount-=Math.max(1,numberOfContours);
        lines = new Edge[edgeCount];
        while(i<numberOfContours){
            j=0;
            while(j<pointList[i].length-1){
                lines[cnt++] = new Edge(pointList[i][j].x,pointList[i][j].y,pointList[i][j+1].x,pointList[i][j+1].y);
                j++;
            }
            i++;
        }
    }

    public void rasterizeSelf(){
        int intersectionCount,scanlineSubDiv = 5;
        float alphaWeight = 255.0f/(float)scanlineSubDiv;
        float stepPerScanline = 1.0f/5.0f;
        for(int y=0;y<bitmapHeight;y++){
            for(int x = 0;x<scanlineSubDiv;x++){
                intersectionCount = 0;
                float scanLine = y + x*stepPerScanline;
                float[] intersections = new float[16];
                for(int j=0;j<edgeCount;j++){
                    Edge edge = lines[j];

                    float biggerY = Math.max(edge.p1.y,edge.p2.y);
                    float smallerY = Math.min(edge.p1.y,edge.p2.y);

                    if(scanLine <= smallerY || scanLine > biggerY)continue;

                    float dx = edge.p2.x - edge.p1.x;
                    float dy = edge.p2.y - edge.p1.y;

                    if(dy == 0)continue;

                    float intersection = -1.0f;
                    if(dx == 0){
                        intersection = edge.p1.x;
                    }
                    else{
                        intersection = (scanLine - edge.p1.y)*(dx/dy) + edge.p1.x;
                        if(intersection<0)intersection = edge.p1.x;
                    }
                    intersections[intersectionCount++] = intersection;
                }

                QuickSort.sortFloatArray(intersections,0,intersectionCount-1);
                if(intersectionCount>1){
                    for(int m = 0;m<intersectionCount;m+=2){
                        float startIntersection = intersections[m];
                        int startIndex = (int)intersections[m];
                        float startCovered = (startIndex+1) - startIntersection;

                        float endIntersection = intersections[m+1];
                        int endIndex = (int)intersections[m+1];
                        float endCovered = endIntersection - endIndex;

                        if(startIndex == endIndex){
                            texture[startIndex + y*bitmapWidth] += alphaWeight*startCovered;
                        }
                        else{
                            texture[startIndex + y*bitmapWidth] += alphaWeight*startCovered;
                            texture[endIndex + y*bitmapWidth] += alphaWeight*endCovered;
                        }

                        for(int index = startIndex+1;index<endIndex;index++){
                            texture[index + y*bitmapWidth] += alphaWeight;
                        }

                    }
                }

            }
        }
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

    public void scalePointsToFitBitmap(){
        int i=0,j;
        float scaleX = getBitMapXScale();
        float scaleY = getBitMapYScale();
        while(i<numberOfContours){
            j=0;
            while(j<pointList[i].length){
                pointList[i][j].x =  (pointList[i][j].x - fxMin)*scaleX;
                pointList[i][j].y =  (pointList[i][j].y - fyMin)*scaleY;
                if(pointList[i][j].x < 0 || pointList[i][j].x > bitmapWidth){IOHandler.printString("[X] outside Range: %f Scale: %f".formatted(pointList[i][j].x,scaleX));}
                if(pointList[i][j].y < 0 || pointList[i][j].y > bitmapHeight){IOHandler.printString("[Y] outside Range: %f Scale: %f".formatted(pointList[i][j].y,scaleY));}
                j++;
            }
            i++;
        }
    }

    public float getBitMapYScale(){
        return (float)bitmapHeight/(fyMax-fyMin);
    }

    public float getBitMapXScale(){
        return (float)bitmapWidth/(fxMax-fxMin);
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
