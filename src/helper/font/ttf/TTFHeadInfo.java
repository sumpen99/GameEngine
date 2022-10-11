package helper.font.ttf;

import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;

public class TTFHeadInfo implements ITTFTableInfo {
    public short majorVersion,minorVersion,flags,unitsPerEM,
            macStyle,lowRecPPEM,indexToLocalFormat,glyphDataFormat,
            fontDirectionHint;
    public short xMin,yMin,xMax,yMax;
    public String created,modified;
    public int fontRevision;
    public int checkSumAdjustment,magicNumber;

    @Override
    public void dumpValues(){
        IOHandler.printString("majorVersion %d".formatted(majorVersion));
        IOHandler.printString("minorVersion %d".formatted(minorVersion));
        IOHandler.printString("fontRevision %d".formatted(fontRevision));
        IOHandler.printString("checkSumAdjustment %d".formatted(checkSumAdjustment));
        IOHandler.printString("magicNumber %d".formatted(magicNumber));
        IOHandler.printString("flags %d".formatted(flags));
        IOHandler.printString("unitsPerEM %d".formatted(unitsPerEM));
        IOHandler.printString("created %s".formatted(created));
        IOHandler.printString("modified %s".formatted(modified));
        IOHandler.printString("xMin %d".formatted(xMin));
        IOHandler.printString("yMin %d".formatted(yMin));
        IOHandler.printString("xMax %d".formatted(xMax));
        IOHandler.printString("yMax %d".formatted(yMax));
        IOHandler.printString("macStyle %d".formatted(macStyle));
        IOHandler.printString("lowRecPPEM %d".formatted(lowRecPPEM));
        IOHandler.printString("fontDirectionHint %d".formatted(fontDirectionHint));
        IOHandler.printString("indexToLocalFormat %d".formatted(indexToLocalFormat));
        IOHandler.printString("glyphDataFormat %d".formatted(glyphDataFormat));
    }
}
