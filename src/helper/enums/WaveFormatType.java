package helper.enums;

public enum WaveFormatType {
    PCM(1,"PCM"),
    ALAW(6,"A-law"),
    MULAW(7,"Mu-law");

    private final int value;
    private final String name;
    WaveFormatType(int value,String name){this.value = value;this.name=name;}
    public int getValue(){return this.value;}
    public String getName(){return this.name;}

}
