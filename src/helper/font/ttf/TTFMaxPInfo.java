package helper.font.ttf;
import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;
import static helper.methods.CommonMethods.bytesToInt;

public class TTFMaxPInfo implements ITTFTableInfo {
    public short numGlyphs,maxPoints,maxContours,maxCompositePoints,
            maxCompositeContours,maxZones,maxTwilightPoints,maxStorage,
            maxFunctionDefs,maxInstructionDefs,maxStackElements,maxSizeOfInstructions,maxComponentElements,maxComponentDepth;
    public int version;

    @Override
    public void setValues(byte[] buf){
        version = bytesToInt(buf,0,4,false) / (1 << 16);          // read = 4
        numGlyphs = (short)bytesToInt(buf,4,2,false);             // read = 6
        maxPoints = (short)bytesToInt(buf,6,2,false);             // read = 8
        maxContours = (short)bytesToInt(buf,8,2,false);           // read = 10
        maxCompositePoints = (short)bytesToInt(buf,10,2,false);   // read = 12
        maxCompositeContours = (short)bytesToInt(buf,12,2,false); // read = 14
        maxZones = (short)bytesToInt(buf,14,2,false);             // read = 16
        maxTwilightPoints = (short)bytesToInt(buf,16,2,false);    // read = 18
        maxStorage = (short)bytesToInt(buf,18,2,false);           // read = 20
        maxFunctionDefs = (short)bytesToInt(buf,20,2,false);      // read = 22
        maxInstructionDefs = (short)bytesToInt(buf,22,2,false);   // read = 24
        maxStackElements = (short)bytesToInt(buf,24,2,false);     // read = 26
        maxSizeOfInstructions = (short)bytesToInt(buf,26,2,false);// read = 28
        maxComponentElements = (short)bytesToInt(buf,28,2,false); // read = 30
        maxComponentDepth = (short)bytesToInt(buf,30,2,false);    // read = 32
    }

    @Override
    public void dumpValues(){
        IOHandler.printString("version %d".formatted(version));
        IOHandler.printString("numGlyphs %d".formatted(numGlyphs));
        IOHandler.printString("maxPoints %d".formatted(maxPoints));
        IOHandler.printString("maxContours %d".formatted(maxContours));
        IOHandler.printString("maxCompositePoints %d".formatted(maxCompositePoints));
        IOHandler.printString("maxCompositeContours %d".formatted(maxCompositeContours));
        IOHandler.printString("maxZones %d".formatted(maxZones));
        IOHandler.printString("maxTwilightPoints %s".formatted(maxTwilightPoints));
        IOHandler.printString("maxStorage %s".formatted(maxStorage));
        IOHandler.printString("maxFunctionDefs %d".formatted(maxFunctionDefs));
        IOHandler.printString("maxInstructionDefs %d".formatted(maxInstructionDefs));
        IOHandler.printString("maxStackElements %d".formatted(maxStackElements));
        IOHandler.printString("maxSizeOfInstructions %d".formatted(maxSizeOfInstructions));
        IOHandler.printString("maxComponentElements %d".formatted(maxComponentElements));
        IOHandler.printString("maxComponentDepth %d".formatted(maxComponentDepth));
    }
}
