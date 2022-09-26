package helper.interfaces;
import helper.struct.Vec2d;

public interface IDrawObject {
   Object getValues();
   Vec2d getCenter();
   Vec2d getPos();
   Vec2d getSize();
   int alignAxisX(int width);
   int alignAxisY(int height);
   void setNewPosition(int x,int y);
   void setNewPositionY(int y);
   void setNewPositionX(int x);
   void rePosition(Vec2d offset);
   void setCenter();
   void draw();
}
