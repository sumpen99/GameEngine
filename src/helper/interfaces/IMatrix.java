package helper.interfaces;

import helper.struct.Matrix;
import helper.struct.Vec2d;

public interface IMatrix {
    void identity(Matrix mat);
    void translate(float x,float y);
    void scale(float fscalex,float fscaley);
    void shear(float fscalex,float fscaley);
    void rotate(float ftheta);
    void multiply(Matrix m1,Matrix m2);
    void forward(float px,float py, Vec2d pos);
    void invert(Matrix m);

}
