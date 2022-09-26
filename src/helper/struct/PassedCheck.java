package helper.struct;

public class PassedCheck {
    public boolean passed;
    public int iNum;
    public float fNum;
    public String message;

    public PassedCheck(boolean error){
        this.passed = error;
    }

    public PassedCheck(boolean error,int num){
        this.passed = error;
        this.iNum = num;
    }

    public PassedCheck(boolean error,String message){
        this.passed = error;
        this.message = message;
    }

    public PassedCheck(boolean error,float num){
        this.passed = error;
        this.fNum = num;
    }

    public PassedCheck(){}
}
