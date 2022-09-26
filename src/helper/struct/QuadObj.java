package helper.struct;

import static helper.enums.QuadGlobal.*;
import static helper.methods.CommonMethods.getRandomInt;

public class QuadObj {
    FVec2d vPos,vSize,vel;
    int color;
    QuadObj(){
        vPos = new FVec2d();
        vSize = new FVec2d();
        vel = new FVec2d();
    }
    void setValues(int clr){
        color = clr;
        getRandomInt(vPos,QUAD_WIDTH.getValue(),QUAD_HEIGHT.getValue());
        vSize.x = QUAD_ITEM_SIZE.getValue();
        vSize.y = QUAD_ITEM_SIZE.getValue();
    }
}
