package helper.methods;
import helper.struct.Point;
import static helper.struct.Point.addPoint;
import static helper.struct.Point.subPoint;
import static helper.struct.Point.multPoint;
import static helper.struct.Point.divPoint;
import static helper.struct.Point.addPointPoint;
import helper.enums.Token;
import helper.io.IOHandler;
import helper.struct.*;

import java.util.Arrays;

import static helper.methods.StringToEnum.getIntToColor;

public class CommonMethods{
    public static boolean intBool(int value){
        return !(value==0);
    }

    public static PassedCheck stringIsInt(String s){
        try{
            int num = Integer.parseInt(s);
            return new PassedCheck(true,num);
        }
        catch( NumberFormatException err){
            return new PassedCheck(false);
        }
    }

    public static int getMiddlePoint(int a,int b){
        return a + ((b-a)/2);
    }

    public static boolean inRangeOf(int span,int num1,int num2){
        int diff = Math.abs(num2-num1);
        return (0 <= diff) && (diff <= span);
    }

    public static int[] parsePoints(String values){
        String[] num = values.split(Token.POINT_SPLIT.getValue());
        if(num.length > 0){
            PassedCheck verify;
            int count = 0;
            int[]numbers;
            numbers = new int[num.length];
            for(int i = 0;i < num.length;i++){
                verify = stringIsInt(num[i]);
                if(verify.passed){numbers[count++] = verify.iNum;}
            }
            if(count == num.length)return numbers;
        }
        return null;
    }

    public static boolean pointInRange(int minValue,int maxValue,int point){
        return (point >= minValue && point <= maxValue);
    }

    public static int intReMapValue(float value,float min1,float max1,float min2,float max2){
        return (int)((max2-min2) * (value-min1)/(max1-min1) + min2);
    }

    public static float floatReMapValue(float value,float min1,float max1,float min2,float max2){
        return (max2-min2) * (value-min1)/(max1-min1) + min2;
    }

    public static char offsetString(String str,int offset,char EMPTY_CHAR){
        if(offset >= str.length()){return EMPTY_CHAR;}
        return str.charAt(offset);
    }

    public static void swapAutoWord(AutoWord w1,AutoWord w2){
        int index = w1.index,edits = w1.edits;
        String word = w1.word;
        w1.swapValues(w2.index,w2.edits,w2.word);
        w2.swapValues(index,edits,word);
    }

    public static void swapIntArrayObject(int[] arr,int i1,int i2){
        int temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
    }

    public static void getRandomInt(FVec2d pos,int bound_x, int bound_y){
        pos.x = (float)((Math.random()*10000) % bound_x);
        pos.y = (float)((Math.random()*10000) % bound_y);
    }

    public static boolean lineLineIntersect(Vec2d P0,Vec2d P1,Vec2d Q0,Vec2d Q1,Vec2d ptr){
        int d = (P1.x-P0.x) * (Q1.y-Q0.y) + (P1.y-P0.y) * (Q0.x-Q1.x);
        if(d != 0){
            float t = (float)((Q0.x-P0.x) * (Q1.y-Q0.y) + (Q0.y-P0.y) * (Q0.x-Q1.x)) / d;
            float u = (float)((Q0.x-P0.x) * (P1.y-P0.y) + (Q0.y-P0.y) * (P0.x-P1.x)) / d;
            if(fInRange(0.0f,t,1.0f) && fInRange(0.0f,u,1.0f)){
                ptr.x = Math.round((float)P1.x * t + (float)P0.x * (1-t));
                ptr.y = Math.round((float)P1.y * t + (float)P0.y * (1-t));
                return true;
            }
        }
        return false;
    }

    public static boolean intersect(int l1, int t1, int r1, int b1,int l2, int t2, int r2, int b2){
        return l2 <= r1 && r2 >= l1 && t2 <= b1 && b2 >= t1;
    }

    public static boolean fInRange(float a,float b,float c){
        return (b > a && b <= c);
    }

    public static String intColorToHex(int color){
        String name = getIntToColor(color);
        return "%s 0x%08X".formatted(name,color);
    }

    public static Point[] intArrToPointArr(int[] p){
        assert p.length%2 == 0;
        int size = p.length,i = 0,cnt = 0;
        Point[] points = new Point[size/2];
        for(;i<size;i+=2){
            points[cnt++] = new Point(p[i],p[i+1]);
        }
        return points;
    }

    public static Point[] buildPolygonShape(Point[] points){
        int size = points.length,cnt,i;
        Point[] l1 = new Point[size],l2 = new Point[size];
        offsetLine(points,l1,l2,10);
        points = Arrays.copyOf(l1,size*2+1);
        cnt = size-1;
        for(i = size;i<size*2;i++){
            points[i] = l2[cnt--];
        }
        points[i+1] = new Point(points[0].x,points[0].y);
        return points;
    }

    public static void offsetLine(Point[] points,Point[] l1,Point[] l2,float offset){
        int size = points.length,i = 0;
        Point tangent,n,normal,p1,p2,n1,n2;
        HalfLine prevsegment,nextsegment;

        for(;i<size;i++){
            if(i == 0 || i == size-1){
                tangent = i == 0 ? subPoint(points[i+1],points[i]) : subPoint(points[i],points[i-1]);
                n = new Point(-tangent.y,tangent.x);
                normal = divPoint(n,n.length);
                p1 = addPointPoint(points[i],multPoint(normal,offset));
                p2 = addPointPoint(points[i],multPoint(normal,-offset));
                l1[i] = new Point(p1.x,p1.y);
                l2[i] = new Point(p2.x,p2.y);
            }
            else {
                prevsegment = new HalfLine(points[i - 1],points[i]);
                nextsegment = new HalfLine(points[i],points[i + 1]);

                n1 = prevsegment.offset(offset).intersect(nextsegment.offset(offset));
                n2 = prevsegment.offset(-offset).intersect(nextsegment.offset(-offset));

                if (n1 != null){
                    l1[i] = new Point(n1.x,n1.y);
                }
                else{
                    p1 = addPointPoint(points[i],multPoint(prevsegment.n, offset));
                    l1[i] = new Point(p1.x, p1.y);

                }
                if(n2 != null){
                    l2[i] = new Point(n2.x,n2.y);
                }
                else{
                    p2 = addPointPoint(points[i],multPoint(prevsegment.n, -offset));
                    l2[i] = new Point(p2.x, p2.y);
                }
            }
        }
    }
}
