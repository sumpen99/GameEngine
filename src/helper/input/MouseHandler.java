package helper.input;
import helper.io.IOHandler;
import helper.struct.FVec2d;
import helper.struct.Vec2d;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelListener;
import static helper.enums.MouseState.*;

public class MouseHandler extends MouseAdapter implements MouseWheelListener{
    private static MouseHandler self = null;
    private static boolean isSet = false;
    private int mouseState;
    private long leftLastClick,rightLastClick,wheelMotion,releaseTime;
    private final int CLICK_RATIO = 150;
    private final int WHEEL_RATIO = 150;
    private final int RELEASE_RATIO = 50;
    private float fOffsetX,fOffsetY,fScaleX,fScaleY,fStartPanX,fStartPanY;
    public Vec2d pos;

    public MouseHandler(){
        assert !MouseHandler.isSet :"MouseHandler is already set!";
        MouseHandler.setInstance();
    }

    private static void setInstance(){
        MouseHandler.isSet = true;
    }

    public static void initMouseHandler(){
        if(self == null){
            self = new MouseHandler();
            self.mouseState = 0;
            self.pos = new Vec2d(0,0);
            self.fScaleX = 1.0f;
            self.fScaleY = 1.0f;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){}

    @Override
    public void mousePressed(MouseEvent e) {
        initPan(e);
        if(e.getButton() == MouseEvent.BUTTON1){
            setMouseBit(SM_MOUSE_LEFT_DOWN.getValue());
            if(System.currentTimeMillis()-leftLastClick <= CLICK_RATIO){
                clearMouseBits();
                setMouseBit(SM_MOUSE_LEFT_DOUBLE_CLICK.getValue());
            }
            else{setMouseBit(SM_MOUSE_LEFT_DOWN.getValue());}
        }
        else if(e.getButton() == MouseEvent.BUTTON3){
            if(System.currentTimeMillis()-rightLastClick <= CLICK_RATIO){
                clearMouseBits();
                setMouseBit(SM_MOUSE_RIGHT_DOUBLE_CLICK.getValue());
            }
            else{setMouseBit(SM_MOUSE_RIGHT_DOWN.getValue());}
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setReleaseTimer();
        if(e.getButton() == MouseEvent.BUTTON1){leftLastClick = System.currentTimeMillis();}
        else if(e.getButton() == MouseEvent.BUTTON3){rightLastClick = System.currentTimeMillis();}
        clearMouseBits();
        setMouseBit(SM_MOUSE_RELEASE_TOUCH.getValue());
    }

    @Override
    public void mouseEntered(MouseEvent e){}

    @Override
    public void mouseExited(MouseEvent e){}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        detectZoom();
        setWheelTimer();
        setMouseBit(SM_MOUSE_WHEEL.getValue());
        if(e.getWheelRotation() < 0){setMouseBit(SM_MOUSE_SCROLL_UP.getValue());}
        else {setMouseBit(SM_MOUSE_SCROLL_DOWN.getValue());}
        //detectZoom();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(getMouseBitSet(SM_MOUSE_LEFT_DOWN.getIndex())){
            detectPan();
            setMouseBit(SM_MOUSE_LEFT_MOVE.getValue());
        }
        else if(getMouseBitSet(SM_MOUSE_RIGHT_DOWN.getIndex())){
            setMouseBit(SM_MOUSE_RIGHT_MOVE.getValue());
        }
        pos.x = e.getX();
        pos.y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        pos.x = e.getX();
        pos.y = e.getY();
    }

    void initPan(MouseEvent e){
        pos.x = e.getX();
        pos.y = e.getY();
        fStartPanX = pos.x;
        fStartPanY = pos.y;
    }

    public static void screenerInsideWorld(float x,float y,FVec2d screen){
        screen.x = (x / self.fScaleX) + self.fOffsetX;
        screen.y = (y / self.fScaleY) + self.fOffsetY;
    }

    public static void worldToScreen(float worldX,float worldY, Vec2d screen){
        screen.x = (int)((worldX - self.fOffsetX) * self.fScaleX);
        screen.y = (int)((worldY - self.fOffsetY) * self.fScaleY);
    }

    public static void screenToWorld(float screenX,float screenY, FVec2d world){
        world.x = (screenX / self.fScaleX) + self.fOffsetX;
        world.y = (screenY / self.fScaleY) + self.fOffsetY;
    }

    public static void detectPan(){
        if(self.getMouseBitSet(SM_MOUSE_LEFT_DOWN.getIndex())){
            float f_mouse_x = (float)self.pos.x;
            float f_mouse_y = (float)self.pos.y;
            self.fOffsetX -= (f_mouse_x - self.fStartPanX) / self.fScaleX;
            self.fOffsetY -= (f_mouse_y - self.fStartPanY) / self.fScaleY;

            self.fStartPanX = f_mouse_x;
            self.fStartPanY = f_mouse_y;
        }
    }

    public static void detectZoom(){
        if(self.getMouseBitSet(SM_MOUSE_SCROLL_UP.getIndex())){
            self.fScaleX *= 1.01f;
            self.fScaleY *= 1.01f;
        }
        else if(self.getMouseBitSet(SM_MOUSE_SCROLL_DOWN.getIndex())){
            self.fScaleX *= 0.99f;
            self.fScaleY *= 0.99f;
        }
    }

    public static MouseHandler getSelf(){return self;}

    public static Vec2d getPos(){return self.pos;}

    private void setMouseBit(int value){
        //mouseState = 0;
        mouseState |= value;
    }

    private void clearMouseBit(int pos){
        mouseState &= ~(1<<pos);
    }

    private void clearMouseBits(){
        mouseState = 0;
    }

    private boolean getMouseBitSet(int bitSet){
        return ((mouseState & (1 << bitSet)) != 0);
    }

    private void setWheelTimer(){
        wheelMotion = System.currentTimeMillis();
    }

    private void setReleaseTimer(){releaseTime = System.currentTimeMillis();}

    private boolean wheelInMotion(){
        return (mouseBitSet(SM_MOUSE_SCROLL_UP.getIndex()) ||
                mouseBitSet(SM_MOUSE_SCROLL_DOWN.getIndex()) ||
                mouseBitSet(SM_MOUSE_WHEEL.getIndex()));
    }

    private void stopWheelInMotion(){
        clearMouseBit(SM_MOUSE_SCROLL_UP.getIndex());
        clearMouseBit(SM_MOUSE_SCROLL_DOWN.getIndex());
        clearMouseBit(SM_MOUSE_WHEEL.getIndex());
    }

    public static boolean mouseBitSet(int bitSet){return self.getMouseBitSet(bitSet);}

    public static void checkStatusOnTimer(){
        self.checkWheelStatus();
        self.checkReleaseStatus();
    }

    private void checkWheelStatus(){
        if(wheelInMotion()){
            if(System.currentTimeMillis() - wheelMotion >= WHEEL_RATIO){
                stopWheelInMotion();
            }
        }
    }

    private void checkReleaseStatus(){
        if(getMouseBitSet(SM_MOUSE_RELEASE_TOUCH.getIndex())){
            if(System.currentTimeMillis() - releaseTime >= RELEASE_RATIO){
                clearMouseBit(SM_MOUSE_RELEASE_TOUCH.getIndex());
            }
        }
    }

    public static void printSelf(){
        IOHandler.printString("Mouse scaleX: %f scaleY: %f offsetX: %f offsetY: %f".formatted(self.fScaleX,self.fScaleY,self.fOffsetX,self.fOffsetY));
    }

}
