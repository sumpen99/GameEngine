package engine;
import helper.enums.*;
import helper.canvas.CanvasHandler;
import helper.drawobjects.*;
import helper.function.FunctionMethods;
import helper.input.KeyboardHandler;
import helper.interfaces.IGameEngine;
import helper.io.IOHandler;
import helper.layout.Layout;
import helper.layout.RootLayout;
import helper.input.MouseHandler;
import helper.list.SMHashMap;
import helper.struct.*;
import helper.widget.Widget;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import javax.swing.*;
import static helper.enums.EntrieType.ENTRIE_LAYOUT;
import static helper.enums.EntrieType.ENTRIE_WIDGET;
import static helper.enums.KeyboardState.*;
import static helper.enums.MouseState.*;
import static helper.enums.MouseState.SM_MOUSE_WHEEL;
import static helper.methods.ParseLayout.*;

class ObjectPanel extends JPanel{
    ObjectPanel(){
        this.setIgnoreRepaint(true);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(!CanvasHandler.frameIsNull()){
            g2.drawImage(CanvasHandler.getFrame(),null,0,0);
            g2.dispose();
            CanvasHandler.clearFrameBuf();
            GameEngine.setRenderStatus(false);
        }
    }
}

public abstract class GameEngine extends JFrame implements IGameEngine{
    protected static int screenWidth,screenHeight;
    protected JPanel mainPanel;
    protected ObjectPanel objPanel;
    protected static boolean quitProgram;
    protected static float FPS;
    public static PassedCheck funcToCheck;
    protected static int FRAMES;
    protected Layout layoutRoot;
    protected Object objCallerClass;
    protected static SMHashMap programID;
    protected static boolean bufferDrawn,drawInfotext;
    protected static String infoText;

    public GameEngine(int screenwidth,int screenheight){
        screenWidth = Math.min(screenwidth, this.getCurrentScreenWidth());
        screenHeight = Math.min(screenheight, this.getCurrentScreenHeight());
        this.objCallerClass = new FunctionMethods();
        this.initialize();
    }

    public boolean parseGuiFile(String pathGui){
        String [] data = new String[2048];
        boolean result = false;
        GlobalCounter counter = new GlobalCounter();
        if(IOHandler.readFromGUIFile(pathGui,data,counter)){
            builUiElements(data,counter);
            collectProgramID();
            bindWidgetByID();
            result = IOHandler.testPathToLogFile();
        }
        return result;
    }

    void builUiElements(String [] data,GlobalCounter counter){
        int j,rows,idx,start;
        j = 0;
        layoutRoot = new RootLayout(0,0,this.screenWidth,this.screenHeight);
        String END_OF_ELEMENT = Token.END_OF_ELEMENT.getValue();
        while(!data[j].equals(Token.END_OF_GUI.getValue())){
            boolean doLayout = false,doWidget = false,topLevel = false;
            String element = data[j].toLowerCase();
            DrawValues dww = new DrawValues();
            if((doLayout=isLayout(element,dww)) || (doWidget=isWidget(element,dww)) || (topLevel = isTopLevel(element,layoutRoot))){
                idx = j;rows = 0;
                start = j;
                dww.lnum = idx;
                while(j < counter.index-1 && !data[++j].equals(END_OF_ELEMENT))rows++;
                if(rows != 0){
                    if(doWidget){
                        Layout l = layoutRoot.getCurrentLayout();
                        parseDrawValues(dww,start,rows,data);
                        Widget w = getNewWidget(this.objCallerClass,dww);
                        w.setID(dww.id);
                        w.setChildID(dww.bind);
                        w.setParameters(dww.args);
                        l.addChild(w);

                    }
                    else if(doLayout){
                        parseDrawValues(dww,start,rows,data);
                        Layout l = getNewLayout(dww);
                        l.setID(dww.id);
                        if(layoutRoot.layoutFitInside(l))
                            layoutRoot.addLayout(l);
                    }

                }
            }
            else if(data[j].length() != 0){
                IOHandler.logToFile("Error at line: %d".formatted(j));
                assert false : "UnExpected UI Element Check Out LogFile";
            }
            j++;
        }
    }

    void collectProgramID(){
        Layout l = this.layoutRoot;
        Widget w;
        String idKey;
        while(l != null){
            w = l.wNext;
            idKey = l.getID();
            if(idKey != null && idKey.length() != 0){programID.addNewItem(idKey,l,ENTRIE_LAYOUT);}
            while(w != null){
                idKey = w.getID();
                if(idKey != null && idKey.length() != 0){programID.addNewItem(idKey,w,ENTRIE_WIDGET);}
                w = w.next;
            }
            l = l.lNext;
        }
    }

    void bindWidgetByID(){
        Layout l = this.layoutRoot;
        Widget w;
        String idKey;
        while(l != null){
            w = l.wNext;
            while(w != null){
                idKey = w.getChildID();
                if(idKey != null && idKey.length() != 0){w.childWidget = getWidgetById(idKey);}
                w = w.next;
            }
            l = l.lNext;
        }
    }

    public static Widget getWidgetById(String id){
        return (Widget) programID.getObject(id).value;
    }

    void initialize(){
        programID = new SMHashMap(100,.75f);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        this.add(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setFocusable(true);
        this.setResizable(false);
        this.setGlobal();
        this.setCurrentPanel();
    }

    void setGlobal(){
        SMDateTime.initSMDateTime();
        MouseHandler.initMouseHandler();
        KeyboardHandler.initKeyboardHandler();
        CanvasHandler.initCanvasHandler(screenWidth,screenHeight);
        TextWriter.initTextWriter();
        addMouseListener(MouseHandler.getSelf());
        addMouseMotionListener(MouseHandler.getSelf());
        addMouseWheelListener(MouseHandler.getSelf());
        addKeyListener(KeyboardHandler.getSelf());
        GameEngine.setRenderStatus(false);
        GameEngine.quitProgram = false;
        GameEngine.FRAMES = 60;
        GameEngine.funcToCheck = new PassedCheck();
    }

    public String getErrorMessage(){
        return GameEngine.funcToCheck.message;
    }

    void setCurrentPanel(){
        this.objPanel = new ObjectPanel();
        this.mainPanel.removeAll();
        this.mainPanel.add(objPanel,BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(this.screenWidth,this.screenHeight));
        pack();
        this.setLocationRelativeTo(null);

    }

    public static float getCurrentFPS(){
        return GameEngine.FPS;
    }

    public static boolean getRenderStatus(){
        return GameEngine.bufferDrawn;
    }

    public static void setRenderStatus(boolean value){
        GameEngine.bufferDrawn = value;
    }

    public static void setInfoTextStatus(boolean value){
        GameEngine.drawInfotext = value;
    }

    public static void setInfoText(String value){
        GameEngine.infoText = value;
    }

    protected int getCurrentScreenWidth(){
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
    }

    protected int getCurrentScreenHeight(){
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
    }

    protected void refreshTexture(){
        this.objPanel.repaint();
    }

    protected void checkPollEvent(){MouseHandler.checkStatusOnTimer();}

    public void runEngineLoop() {
        this.setVisible(true);
        this.engineLoop();
        System.exit(0);
    }

    public static void exitEngineLoop(){
        GameEngine.quitProgram = true;
    }

    void engineLoop() {
        long frameStart,frameEnd,deltaTime;
        deltaTime = 1;
        int pausLoop;
        while(!GameEngine.quitProgram)  {
            frameStart = System.currentTimeMillis();
            checkPollEvent();
            onUserUpdate((float)deltaTime/1000.0f);
            if(!GameEngine.getRenderStatus()){
                GameEngine.setRenderStatus(true);
                drawToBuffer();
                refreshTexture();
            }
            frameEnd = System.currentTimeMillis();
            deltaTime = Math.max(1,frameEnd-frameStart);
            FPS = 1000.0f/deltaTime;
            if(FPS > GameEngine.FRAMES && deltaTime < GameEngine.FRAMES){
                pausLoop = (int)(1000.0/(GameEngine.FRAMES-deltaTime));
                try{
                    Thread.sleep(pausLoop);
                    frameEnd = System.currentTimeMillis();
                    deltaTime = frameEnd-frameStart;
                    FPS = 1000.0f/deltaTime;
                }
                catch(Exception e){
                    IOHandler.logToFile(e.getMessage());
                }
            }
        }
    }

    boolean updateMouseMovements(Widget w,int x,int y){
        if(MouseHandler.mouseBitSet(SM_MOUSE_LEFT_MOVE.getIndex())){return w.onMouseLeftMove(x,y);}
        if(MouseHandler.mouseBitSet(SM_MOUSE_LEFT_DOWN.getIndex())){return w.onMouseLeftDown(x,y);}

        if(MouseHandler.mouseBitSet(SM_MOUSE_RIGHT_MOVE.getIndex())){return w.onMouseRightMove(x,y);}
        if(MouseHandler.mouseBitSet(SM_MOUSE_RIGHT_DOWN.getIndex())){return w.onMouseRightDown(x,y);}

        if(MouseHandler.mouseBitSet(SM_MOUSE_LEFT_DOUBLE_CLICK.getIndex())){return w.onMouseLeftDoubleClick(x,y);}
        if(MouseHandler.mouseBitSet(SM_MOUSE_RIGHT_DOUBLE_CLICK.getIndex())){return w.onMouseRightDoubleClick(x,y);}

        if(MouseHandler.mouseBitSet(SM_MOUSE_RELEASE_TOUCH.getIndex())){return w.onMouseReleaseTouch(x,y);}

        if(MouseHandler.mouseBitSet(SM_MOUSE_SCROLL_UP.getIndex())){return w.onMouseScrollUp(x,y);}
        if(MouseHandler.mouseBitSet(SM_MOUSE_SCROLL_DOWN.getIndex())){return w.onMouseScrollDown(x,y);}
        if(MouseHandler.mouseBitSet(SM_MOUSE_WHEEL.getIndex())){return w.onMouseWheel(x,y);}

        return w.onFrameUpdate(x,y);

    }

    boolean updateKeyboardEvent(Widget w){
        boolean keyHandled = false;
        if(KeyboardHandler.keyboardBitSet(SM_KEY_DOWN.getIndex())){keyHandled = w.onKeyDown();}
        ////if((keyHandled = w.onKeyDown())){KeyboardHandler.releaseKeyPressed();}
        if(keyHandled){KeyboardHandler.releaseKeyPressed();}
        return keyHandled;
    }

    void drawToBuffer(){
        int x = MouseHandler.getPos().x,y = MouseHandler.getPos().y;
        boolean handledMouse = false;
        boolean handledkeyboard = false;
        Layout l = this.layoutRoot;
        Widget w;
        while(l != null){
            w = l.wNext;
            l.draw();
            while(w != null){
                w.pointInside(x,y);
                if(!handledMouse){handledMouse = updateMouseMovements(w,x,y);}
                if(!handledkeyboard){handledkeyboard = updateKeyboardEvent(w);}
                w.draw();
                w = w.next;
            }
            l = l.lNext;
        }

        if(drawInfotext && infoText != null){
            TextWriter.drawText(infoText,0,screenHeight-3,Color.BLACK.getValue());
        }

    }
}
