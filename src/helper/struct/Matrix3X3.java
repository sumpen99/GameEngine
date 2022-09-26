package helper.struct;

public class Matrix3X3 extends Matrix {
    public Matrix3X3(){super(3);}

    @Override
    public void identity(Matrix mat){
        mat.m[0] = 1.0f;
        mat.m[1] = 0.0f;
        mat.m[2] = 0.0f;

        mat.m[3] = 0.0f;
        mat.m[4] = 1.0f;
        mat.m[5] = 0.0f;

        mat.m[6] = 0.0f;
        mat.m[7] = 0.0f;
        mat.m[8] = 1.0f;
    }

    @Override
    public void translate(float x,float y){
        m[0] = 1.0f;
        m[1] = 0.0f;
        m[2] = 0.0f;

        m[3] = 0.0f;
        m[4] = 1.0f;
        m[5] = 0.0f;

        m[6] = x;
        m[7] = y;
        m[8] = 1.0f;
    }

    @Override
    public void scale(float fscalex,float fscaley){
        m[0] = fscalex;
        m[1] = 0.0f;
        m[2] = 0.0f;

        m[3] = 0.0f;
        m[4] = fscaley;
        m[5] = 0.0f;

        m[6] = 0.0f;
        m[7] = 0.0f;
        m[8] = 1.0f;
    }

    @Override
    public void shear(float fscalex,float fscaley){
        m[0] = 1.0f;
        m[1]= -fscaley;
        m[2] = 0.0f;

        m[3] = fscalex;
        m[4] = 1.0f;
        m[5] = 0.0f;

        m[6] = 0.0f;
        m[7] = 0.0f;
        m[8] = 1.0f;
    }

    @Override
    public void rotate(float ftheta){
        m[0] = (float)Math.cos(ftheta);
        m[1] = -(float)Math.sin(ftheta);
        m[2] = 0.0f;

        m[3] = (float)Math.sin(ftheta);
        m[4] = (float)Math.cos(ftheta);
        m[5] = 0.0f;

        m[6] = 0.0f;
        m[7] = 0.0f;
        m[8] = 1.0f;
    }

    @Override
    public void multiply(Matrix m1,Matrix m2){
        for(int i = 0;i < 3;i++){
            for(int j = 0;j < 3;j++){
                setValue(i,j,(
                                        m1.getValue(0,j) * m2.getValue(i,0) +
                                        m1.getValue(1,j) * m2.getValue(i,1) +
                                        m1.getValue(2,j) * m2.getValue(i,2)));
            }
        }
    }

    @Override
    public void forward(float px,float py,Vec2d pos){
        pos.x = (int)(px * m[0] + py*m[3] + m[6]);
        pos.y = (int)(px * m[1] + py*m[4] + m[7]);
    }

    @Override
    public void invert(Matrix m1){
        float det = (m1.m[0] * (m1.m[4] * m1.m[8] - m1.m[5] * m1.m[7]) -
                     m1.m[3] * (m1.m[1] * m1.m[8] - m1.m[7] * m1.m[2]) +
                     m1.m[6] * (m1.m[1] * m1.m[5] - m1.m[4] * m1.m[2]));

        float idet = 1.0f/det;
        m[0] = (m1.m[4] * m1.m[8] - m1.m[5] * m1.m[7]) * idet;
        m[3] = (m1.m[6] * m1.m[5] - m1.m[3] * m1.m[8]) * idet;
        m[6] = (m1.m[3] * m1.m[7] - m1.m[6] * m1.m[4]) * idet;

        m[1] = (m1.m[7] * m1.m[2] - m1.m[1] * m1.m[8]) * idet;
        m[4] = (m1.m[0] * m1.m[8] - m1.m[6] * m1.m[2]) * idet;
        m[7] = (m1.m[1] * m1.m[6] - m1.m[0] * m1.m[7]) * idet;

        m[2] = (m1.m[1] * m1.m[5] - m1.m[2] * m1.m[4]) * idet;
        m[5] = (m1.m[2] * m1.m[3] - m1.m[0] * m1.m[5]) * idet;
        m[8] = (m1.m[0] * m1.m[4] - m1.m[1] * m1.m[3]) * idet;
    }


}
