package helper.struct;
import helper.io.IOHandler;

import static helper.methods.CommonMethods.buildPointArr;


//https://github.com/OneLoneCoder/Javidx9/blob/master/ConsoleGameEngine/BiggerProjects/Splines/OneLoneCoder_Splines2.cpp
public class Spline {
    public Point[] points;
    public Point[] splinePoints;
    public Spline leftSpline,rightSpline;
    public float totalSplineLength;
    boolean looped;

    public Spline(){
    }

    public Spline(Point[] line,boolean isLooped){
        points = line;
        looped = isLooped;
    }

    public Spline(int size,boolean isLooped){
        points = new Point[size];
        looped = isLooped;
        initPointList();
    }

    void initPointList(){
        int i=0;
        while(i<points.length){points[i++] = new Point(0,0);}
    }

    void doubleFirstAndLast(){
        int numPoints = points.length-1;
        assert numPoints>=1 : "Spline Points Contains < 1 Points";
        points[0] = new Point(points[1].x,points[1].y);
        points[numPoints] = new Point(points[numPoints-1].x,points[numPoints-1].y);
    }

    void addDoubleFirstAndLast(){
        int size = points.length,newSize = points.length+2;
        Point[] newList = new Point[size+2];
        Point first = points[0],last = points[size-1];
        System.arraycopy(points,1,newList,0,size);
        points = newList;
        points[0] = first;
        points[newSize-1] = last;
    }

    Point getSplinePoint(float t){
        int p0,p1,p2,p3;
        if(!looped){
            p1 = (int)t+1;
            p2 = p1+1;
            p3 = p2+1;
            p0 = p1-1;
        }
        else{
            p1 = (int)t;
            p2 = (p1+1) % points.length;
            p3 = (p2+1) % points.length;
            p0 = p1 >= 1 ? p1-1 : points.length-1;
        }

        t = t - (int)t;
        float tt = t*t;
        float ttt = tt*t;

        float q1 = -ttt + 2.0f*tt-t;
        float q2 = 3.0f*ttt - 5.0f*tt + 2.0f;
        float q3 = -3.0f*ttt + 4.0f*tt + t;
        float q4 = ttt - tt;

        float tx = 0.5f * (points[p0].x * q1 + points[p1].x * q2 + points[p2].x * q3 + points[p3].x * q4);
        float ty = 0.5f * (points[p0].y * q1 + points[p1].y * q2 + points[p2].y * q3 + points[p3].y * q4);

        return new Point(tx,ty);
    }

    Point getSplineGradient(float t){
        int p0,p1,p2,p3;
        if(!looped){
            p1 = (int)t+1;
            p2 = p1+1;
            p3 = p2+1;
            p0 = p1-1;
        }
        else{
            p1 = (int)t;
            p2 = (p1+1) % points.length;
            p3 = (p2+1) % points.length;
            p0 = p1 >= 1 ? p1-1 : points.length-1;
        }

        t = t - (int)t;
        float tt = t*t;
        float ttt = tt*t;

        float q1 = -3.f * tt + 4.0f*t - 1;
        float q2 = 9.0f*tt - 10.0f*t;
        float q3 = -9.0f*tt + 8.0f*t + 1.0f;
        float q4 = 3.0f*tt - 2.0f*t;

        float tx = 0.5f * (points[p0].x * q1 + points[p1].x * q2 + points[p2].x * q3 + points[p3].x * q4);
        float ty = 0.5f * (points[p0].y * q1 + points[p1].y * q2 + points[p2].y * q3 + points[p3].y * q4);

        return new Point(tx,ty);
    }

    float calculateSegmentLength(int node){
        float fLength = 0.0f;
        float fStepSize = 0.005f;

        Point oldPoint,newPoint;
        oldPoint = getSplinePoint((float)node);

        for (float t = 0; t < 1.0f; t += fStepSize){
            newPoint = getSplinePoint((float)node + t);
            fLength += Math.sqrt((newPoint.x - oldPoint.x)*(newPoint.x - oldPoint.x)
                    + (newPoint.y - oldPoint.y)*(newPoint.y - oldPoint.y));
            oldPoint = newPoint;
        }

        return fLength;
    }

    float getFractionalOffset(float p){
        int i = 0;
        while(p>points[i].length){
            p-=points[i].length;
            i++;
        }
        return (float)i + (p/points[i].length);
    }

    void updateSplineProperties(){
        totalSplineLength = 0.0f;
        int i=0;
        if(looped){
            while(i<points.length-1){
                points[i].length = calculateSegmentLength(i);
                totalSplineLength+=points[i].length;
            }
        }
        else{
            while(i<points.length-2){
                points[i].length = calculateSegmentLength(i);
                totalSplineLength+=points[i].length;
            }
        }
    }

    void setSplinePoints(boolean dbl){
        if(dbl){addDoubleFirstAndLast();}
        float t = 0.0f,frac = 0.1f;
        int size,cnt=0;
        if(looped){
            size = (int)(((float)points.length-1.0f)/frac);
            splinePoints = new Point[size+1];
            while(t<(float)points.length-1.0f){
                splinePoints[cnt++] = getSplinePoint(t);
                t+=frac;
            }
        }
        else{
            size = (int)(((float)points.length-3.0f)/frac);
            splinePoints = new Point[size+1];
            while(t<(float)points.length-3.0f){
                splinePoints[cnt++] = getSplinePoint(t);
                t+=frac;
            }
        }
    }

    public static Spline buildMultipleSplines(Point[] line,int offset){
        return buildSplineWithOffset(line,offset);
    }

    public static Spline buildSingleSpline(Point[] line){
        return buildSpline(line);
    }

    static Spline buildSplineWithOffset(Point[] line,float offset){
        int numPoints = line.length,i=1;
        float glen;
        Point p1,g1;
        Spline mainSpline = new Spline(line,true);
        mainSpline.leftSpline = new Spline(line.length,true);
        mainSpline.rightSpline = new Spline(line.length,true);

        while(i<numPoints){
            p1 = mainSpline.getSplinePoint((float)i);
            g1 = mainSpline.getSplineGradient((float)i);

            glen = (float)Math.sqrt((g1.x*g1.x)+(g1.y*g1.y));

            mainSpline.leftSpline.points[i].x = p1.x + offset * (-g1.y / glen);
            mainSpline.leftSpline.points[i].y = p1.y + offset * ( g1.x / glen);
            mainSpline.rightSpline.points[i].x = p1.x - offset * (-g1.y / glen);
            mainSpline.rightSpline.points[i].y = p1.y - offset * ( g1.x / glen);
            i++;
        }
        mainSpline.setSplinePoints(false);
        mainSpline.leftSpline.setSplinePoints(false);
        mainSpline.rightSpline.setSplinePoints(false);

        return mainSpline;
    }

    static Spline buildSpline(Point[] line){
        Spline mainSpline = new Spline(line,true);
        mainSpline.setSplinePoints(false);
        return mainSpline;
    }
}
