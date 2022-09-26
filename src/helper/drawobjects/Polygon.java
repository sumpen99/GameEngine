package helper.drawobjects;

import helper.canvas.CanvasHandler;
import helper.enums.Color;
import helper.enums.DrawMode;
import helper.enums.WidgetShape;
import helper.io.IOHandler;
import helper.sort.BubbleSort;
import helper.struct.BaseLine;
import helper.struct.IntersectPoint;
import helper.struct.Point;
import helper.struct.Vec2d;

import static helper.methods.CommonMethods.*;
import static helper.methods.CommonMethods.buildPolygonShape;

public class Polygon extends DrawObject{
    Point[] vertices;
    float[] constant,multiple;
    boolean isect;
    int vertCount,iSectCount,vertSize;
    IntersectPoint[] iSectPoints;
    BaseLine[] lines;
    public Polygon(int[] verts,int color, int opacity, DrawMode draw){
        super(WidgetShape.SM_POLYGON,draw,color,opacity);
        assert verts.length%2 == 0 : "Polygon Vertices Not Acurate";
        initVertices(verts);
        pos = new Vec2d(0,0);
        size = new Vec2d(0,0);
        center = new Vec2d(0,0);
        constant = new float[vertSize];
        multiple = new float[vertSize];
        lines = new BaseLine[vertSize/2];
    }

    void initVertices(int[] verts){
        Point[] points = intArrToPointArr(verts);
        vertices = buildPolygonShape(points);
        vertSize = vertices.length;
    }

    void adjustVertices(Vec2d offset){
        int x = (int)vertices[0].x + offset.x,y = (int)vertices[0].y + offset.y;
        int min_x = x,min_y = y,max_x = x,max_y = y;
        for(int i = 0;i<=vertSize-2;i+=2){
            vertices[i].x += offset.x;
            vertices[i].y += offset.y;
            min_x = (int)Math.min(min_x,vertices[i].x);
            min_y = (int)Math.min(min_y,vertices[i].y);
            max_x = (int)Math.max(max_x,vertices[i].x);
            max_y = (int)Math.max(max_y,vertices[i].y);
        }
        pos.x = min_x;
        pos.y = min_y;
        size.x = max_x-min_x;
        size.y = max_y-min_y;
        center.x = getMiddlePoint(min_x,max_x);
        center.y = getMiddlePoint(min_y,max_y);

    }

    void preCalculateValues(){
        int i, j=vertCount-1;
        for(i=0; i<vertCount; i++) {
            if(vertices[j].y == vertices[i].y){
                constant[i]= vertices[i].x;
                multiple[i]= 0.0f;
            }
            else{
                constant[i]=vertices[i].x-(vertices[i].y*vertices[j].x)/(vertices[j].y-vertices[i].y)+(vertices[i].y*vertices[i].x)/(vertices[j].y-vertices[i].y);
                multiple[i]=(vertices[j].x-vertices[i].x)/(vertices[j].y-vertices[i].y);
            }
            j=i;
        }
    }

    void buildLines(){
        for(int i = 1;i < vertCount;i++){
            lines[i-1] = new BaseLine(vertices[i-1],vertices[i]);
        }
    }

    void selfIntersect(){
        Vec2d ptr = new Vec2d(0,0);
        for(int i = 0;i < vertCount-1;i++){
            for(int j = i;j < vertCount-1;j++){
                int p0x = (int)lines[i].p1.x,p0y = (int)lines[i].p1.y;
                int p1x = (int)lines[i].p2.x,p1y = (int)lines[i].p2.y;
                int q0x = (int)lines[j].p1.x,q0y = (int)lines[j].p1.y;
                int q1x = (int)lines[j].p2.x,q1y = (int)lines[j].p2.y;
                if(!(p0x == q0x && p0y == q0y) ||
                (p0x == q1x && p0y == q1y) ||
                (p1x == q0x && p1y == q0y) ||
                (p1x == q1x && p1y == q1y)){
                    if(lineLineIntersect(
                            new Vec2d(p0x,p0y),new Vec2d(p1x,p1y),
                            new Vec2d(q0x,q0y),new Vec2d(q1x,q1y),ptr)){
                        iSectPoints[iSectCount] = new IntersectPoint();
                        iSectPoints[iSectCount].indexes[0] = i;
                        iSectPoints[iSectCount].indexes[1] = j;
                        iSectPoints[iSectCount].point = ptr;
                        iSectCount++;
                    }
                }
            }
        }
        isect = (iSectCount > 0);
    }

    void collectIntersectPoints(){
        if(isect){
            for(int t= 0;t < iSectCount;t++){
                int[] points = {iSectPoints[t].point.x,iSectPoints[t].point.y};
                int[] idx = {iSectPoints[t].indexes[0],iSectPoints[t].indexes[1]};
                IOHandler.logToFile("IntersectionIndex %s %s".formatted(idx[0],idx[1]));
                IOHandler.logToFile("IntersectionPoint %s %s".formatted(points[0],points[1]));
            }
        }
    }

    @Override
    public void rePosition(Vec2d offset){
        adjustVertices(offset);
        preCalculateValues();
        buildLines();
        selfIntersect();
        collectIntersectPoints();
        this.pos.x += offset.x;
        this.pos.y += offset.y;
    }

    @Override
    public void setCenter(){
        if(this.center == null){this.center = new Vec2d(0,0);}
        this.center.x = this.pos.x+(this.size.x/2);
        this.center.y = this.pos.y+(this.size.y/2);
    }

    @Override
    public Object getValues(){
        Object[] obj = new Object[3];
        obj[0] = vertices;
        obj[1] = constant;
        obj[2] = multiple;
        return obj;
    }

    @Override
    public void draw(){
        if(this.draw == DrawMode.FILL){this.drawFilledPolygon();}
        else if(this.draw == DrawMode.OUTLINE){this.drawOutLinedPolygon();}

        if(isect){
            fillPolygonIsect();
            //fillPolygonVert(p,points,TP_BUF_WIDTH,WHITE);
        }
    }

    void fillPolygonIsect(){
        int color = Color.FIREBRICK.getValue();
        for(int i = 0;i<iSectCount;i++){
            int radie = 2,x =iSectPoints[i].point.x,y = iSectPoints[i].point.y;
            int x0 = 0,y0 = radie,d = 3 - 2*radie;
            if(radie > 0){
                while(y0 >= x0)
                {
                    for(int j = x-x0;j < x+x0;j++)
                        CanvasHandler.setPixel(j,y-y0,color);
                    for(int k = x-y0;k < x+y0;k++)
                        CanvasHandler.setPixel(k,y-x0,color);
                    for(int l = x-x0;l < x+x0;l++)
                        CanvasHandler.setPixel(l,y+y0,color);
                    for(int m = x-y0;m < x+y0;m++)
                        CanvasHandler.setPixel(m,y+x0,color);
                    if(d < 0)
                        d+= 4*x0++ + 6;
                    else
                        d+=4*(x0++ - y0--) + 10;

                }
            }
        }
    }

    void fillPolygonVert(){
        int color = Color.CHARTREUSE.getValue();
        for(int i = 0;i<vertCount;i++){
            int radie = 2,x = (int)vertices[i].x,y = (int)vertices[i].y;
            int x0 = 0,y0 = radie,d = 3 - 2*radie;
            if(radie > 0){
                while(y0 >= x0)
                {
                    for(int j = x-x0;j < x+x0;j++)
                        CanvasHandler.setPixel(j,y-y0,color);
                    for(int k = x-y0;k < x+y0;k++)
                        CanvasHandler.setPixel(k,y-x0,color);
                    for(int l = x-x0;l < x+x0;l++)
                        CanvasHandler.setPixel(l,y+y0,color);
                    for(int m = x-y0;m < x+y0;m++)
                        CanvasHandler.setPixel(m,y+x0,color);
                    if(d < 0)
                        d+= 4*x0++ + 6;
                    else
                        d+=4*(x0++ - y0--) + 10;

                }
            }
        }
    }

    void drawOutLinedPolygon(){
        for(int i = 1;i<vertCount;i++){
            Line.drawLine((int)vertices[i-1].x,(int)vertices[i-1].y,(int)vertices[i].x,(int)vertices[i].y,color);
        }
    }

    void drawFilledPolygon(){
        // CODE FROM : https://alienryderflex.com/polygon_fill/
        int IMAGE_TOP = pos.y;
        int IMAGE_BOT = pos.y+size.y;
        int IMAGE_LEFT = pos.x;
        int IMAGE_RIGHT = pos.x+size.x;
        int polycorners = vertCount;
        int nodes,pixelX,pixelY,i,j;
        int[] nodeX = new int[polycorners];
        for(pixelY = IMAGE_TOP;pixelY<IMAGE_BOT;pixelY++){
            nodes = 0;
            j = polycorners-1;
            for(i = 0;i<polycorners;i++){
                float ipolyX = vertices[i].x,ipolyY = vertices[i].y,jpolyX = vertices[j].x,jpolyY = vertices[j].y;
                if((ipolyY < (float)pixelY && jpolyY >= (float)pixelY) ||
                (jpolyY < (float)pixelY && ipolyY >= (float)pixelY)){
                    nodeX[nodes++] = (int)(ipolyX + (pixelY-ipolyY) / (jpolyY-ipolyY) * (jpolyX - ipolyX));
                }
                j = i;
            }

            BubbleSort.minBubbleSort(nodeX,nodes);
            for(i = 0;i<nodes;i+=2){
                if(nodeX[i]>=IMAGE_RIGHT)break;
                if(nodeX[i+1] > IMAGE_LEFT){
                    if(nodeX[i] < IMAGE_LEFT)nodeX[i] = IMAGE_LEFT;
                    if(nodeX[i+1] > IMAGE_RIGHT)nodeX[i+1] = IMAGE_RIGHT;
                    for(pixelX = nodeX[i];pixelX<nodeX[i+1];pixelX++){
                        CanvasHandler.setPixel(pixelX,pixelY,color);
                    }
                }
            }

        }
    }

}
