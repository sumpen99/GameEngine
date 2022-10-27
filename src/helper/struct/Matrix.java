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

    public void flipVertical(){
        int c,r=0,j;
        while(r<rows/2) {
            c = rows - 1 - r;
            j = 0;
            while (j < columns) {
                float temp = m[r * columns + j];
                m[r * columns + j] = m[c * columns + j];
                m[c * columns + j] = temp;
                j++;
            }
            r++;
        }
    }

   int getColFromIndex(int index){
        return index%columns;
    }

   int getRowFromIndex(int index){
        return index/columns;
    }

}
