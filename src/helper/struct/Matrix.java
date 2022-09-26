package helper.struct;
import helper.interfaces.IMatrix;

public abstract class Matrix implements IMatrix {
    public int size,rows,columns;
    public float[] m;
    public Matrix(int type){
        rows = type;
        columns = type;
        size = type*type;
        m = new float[size];
    }

    protected float getValue(int row,int col){
        return m[getIndex(row,col)];
    }

    protected void setValue(int row,int col,float value){
        m[getIndex(row,col)] = value;
    }

    protected int getIndex(int row,int col){
        return (row*columns)+col;
    }

}
