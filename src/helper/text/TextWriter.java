package helper.text;
import helper.canvas.CanvasHandler;
import helper.drawobjects.Line;
import helper.drawobjects.Polygon;
import helper.drawobjects.Rectangle;
import helper.drawobjects.Triangle;
import helper.enums.TextWriterType;
import helper.enums.Token;
import helper.font.ttf.TTFFile;
import helper.io.IOHandler;
import helper.struct.*;

/*
 *     # # # # # # # #
 *     # # # # # # # # #
 *             #       # #
 *             #         # #
 *             #         # #
 *             #       # #
 *     # # # # # # # # #
 *     # # # # # # # #
 *
 * 0x00 0x00 0xc3 0xc3 0xc3 0xff 0xc3 0xc3 0xc3 0x66 0x3c 0x18 -> A
 * */
public class TextWriter{
    // 94 13
    private static TextWriterType typeOfText;
    private static TextWriter self = null;
    private TTFFile ttf;
    private static boolean isSet = false;
    private float unitsPerEm,fontScale;
    private int fontWidth,fontHeight;
    private final int CHAR_GAP = 0;
    private int CHAR_FONT_GAP = 0;
    private int CHAR_FONT_WIDTH = 0;
    private int CHAR_FONT_HEIGHT = 0;
    private final int CHAR_WIDTH = 13;
    private final int CHAR_HEIGHT = 8;
    private final int CHAR_PAD_DUMMY = 127;
    public final int bitmapWidth = 64,bitmapHeight=64;
    private final char END_OF_BUF = Token.END_OF_BUF.getChar();
    private final char SKIP_CHAR = Token.SKIP_CHAR.getChar();
    private final char NEW_LINE = Token.NEW_LINE.getChar();
    private final char NEW_TAB = Token.NEW_TAB.getChar();
    public static AutoWords autoWords;
    // TODO REMOVE WHEN WRITING FROM TTF IS 100% ACCURATE
    private final char[][] charBuf = {
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},//[        ] = 0
            {0x18, 0x18, 0x00, 0x00, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18},//['!' - 32] = 1
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x36, 0x36, 0x36, 0x36},//['"' - 32] = 2
            {0x66, 0x67, 0x6f, 0x7e, 0xf6, 0xe6, 0x66, 0x67, 0x6f, 0x7e, 0xf6, 0xe6, 0x66},//['#' - 32] = 3
            {0x00, 0x00, 0x18, 0x7e, 0xff, 0x1b, 0x1f, 0x7e, 0xf8, 0xd8, 0xff, 0x7e, 0x18},//['$' - 32] = 4
            {0x00, 0x00, 0x0e, 0x1b, 0xdb, 0x6e, 0x30, 0x18, 0x0c, 0x76, 0xdb, 0xd8, 0x70},//['%' - 32] = 5
            {0x00, 0x00, 0x7f, 0xc6, 0xcf, 0xd8, 0x70, 0x70, 0xd8, 0xcc, 0xcc, 0x6c, 0x38},//['&' - 32] = 6
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x00},//['`' - 32] = 7
            {0x00, 0x00, 0x0c, 0x18, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x18, 0x0c},//['(' - 32] = 8
            {0x00, 0x00, 0x30, 0x18, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x18, 0x30},//[')' - 32] = 9
            {0x00, 0x00, 0x00, 0x00, 0x99, 0x5a, 0x3c, 0xff, 0x3c, 0x5a, 0x99, 0x00, 0x00},//['*' - 32] = 10
            {0x00, 0x00, 0x00, 0x18, 0x18, 0x18, 0xff, 0xff, 0x18, 0x18, 0x18, 0x00, 0x00},//['+' - 32] = 11
            {0x00, 0x00, 0x30, 0x18, 0x1c, 0x1c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},//[',' - 32] = 12
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0xff, 0x00, 0x00, 0x00, 0x00, 0x00},//['-' - 32] = 13
            {0x00, 0x00, 0x00, 0x38, 0x38, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},//['.' - 32] = 14
            {0x60, 0x60, 0x30, 0x30, 0x18, 0x18, 0x0c, 0x0c, 0x06, 0x06, 0x03, 0x03, 0x00},//['/' - 32] = 15
            {0x00, 0x00, 0x3c, 0x66, 0xc3, 0xe3, 0xf3, 0xdb, 0xcf, 0xc7, 0xc3, 0x66, 0x3c},//['0' - 32] = 16
            {0x00, 0x00, 0x7e, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x78, 0x38, 0x18},//['1' - 32] = 17
            {0x00, 0x00, 0xff, 0xc0, 0xc0, 0x60, 0x30, 0x18, 0x0c, 0x06, 0x03, 0xe7, 0x7e},//['2' - 32] = 18
            {0x00, 0x00, 0x7e, 0xe7, 0x03, 0x03, 0x07, 0x7e, 0x07, 0x03, 0x03, 0xe7, 0x7e},//['3' - 32] = 19
            {0x00, 0x00, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0xff, 0xcc, 0x6c, 0x3c, 0x1c, 0x0c},//['4' - 32] = 20
            {0x00, 0x00, 0x7e, 0xe7, 0x03, 0x03, 0x07, 0xfe, 0xc0, 0xc0, 0xc0, 0xc0, 0xff},//['5' - 32] = 21
            {0x00, 0x00, 0x7e, 0xe7, 0xc3, 0xc3, 0xc7, 0xfe, 0xc0, 0xc0, 0xc0, 0xe7, 0x7e},//['6' - 32] = 22
            {0x00, 0x00, 0x30, 0x30, 0x30, 0x30, 0x18, 0x0c, 0x06, 0x03, 0x03, 0x03, 0xff},//['7' - 32] = 23
            {0x00, 0x00, 0x7e, 0xe7, 0xc3, 0xc3, 0xe7, 0x7e, 0xe7, 0xc3, 0xc3, 0xe7, 0x7e},//['8' - 32] = 24
            {0x00, 0x00, 0x7e, 0xe7, 0x03, 0x03, 0x03, 0x7f, 0xe7, 0xc3, 0xc3, 0xe7, 0x7e},//['9' - 32] = 25
            {0x00, 0x00, 0x00, 0x38, 0x38, 0x00, 0x00, 0x38, 0x38, 0x00, 0x00, 0x00, 0x00},//[':' - 32] = 26
            {0x00, 0x00, 0x30, 0x18, 0x1c, 0x1c, 0x00, 0x00, 0x1c, 0x1c, 0x00, 0x00, 0x00},//[';' - 32] = 27
            {0x00, 0x00, 0x3c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x3c},//['<' - 32] = 28 currently wrong symbol
            {0x00, 0x00, 0x00, 0x00, 0xff, 0xff, 0x00, 0xff, 0xff, 0x00, 0x00, 0x00, 0x00},//['=' - 32] = 29
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},//['>' - 32] = 30 currently wrong symbol
            {0x00, 0x00, 0x18, 0x00, 0x00, 0x18, 0x18, 0x0c, 0x06, 0x03, 0xc3, 0xc3, 0x7e},//['?' - 32] = 31
            {0x00, 0x00, 0x3f, 0x60, 0xcf, 0xdb, 0xd3, 0xdd, 0xc3, 0x7e, 0x00, 0x00, 0x00},//['@' - 32] = 32
            {0x00, 0x00, 0xc3, 0xc3, 0xc3, 0xc3, 0xff, 0xc3, 0xc3, 0xc3, 0x66, 0x3c, 0x18},//['A' - 32] = 33
            {0x00, 0x00, 0xfe, 0xc7, 0xc3, 0xc3, 0xc7, 0xfe, 0xc7, 0xc3, 0xc3, 0xc7, 0xfe},//['B' - 32] = 34
            {0x00, 0x00, 0x7e, 0xe7, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xe7, 0x7e},//['C' - 32] = 35
            {0x00, 0x00, 0xfc, 0xce, 0xc7, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc7, 0xce, 0xfc},//['D' - 32] = 36
            {0x00, 0x00, 0xff, 0xc0, 0xc0, 0xc0, 0xc0, 0xfc, 0xc0, 0xc0, 0xc0, 0xc0, 0xff},//['E' - 32] = 37
            {0x00, 0x00, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xfc, 0xc0, 0xc0, 0xc0, 0xff},//['F' - 32] = 38
            {0x00, 0x00, 0x7e, 0xe7, 0xc3, 0xc3, 0xcf, 0xc0, 0xc0, 0xc0, 0xc0, 0xe7, 0x7e},//['G' - 32] = 39
            {0x00, 0x00, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xff, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3},//['H' - 32] = 40
            {0x00, 0x00, 0x7e, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x7e},//['I' - 32] = 41
            {0x00, 0x00, 0x7c, 0xee, 0xc6, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06},//['J' - 32] = 42
            {0x00, 0x00, 0xc3, 0xc6, 0xcc, 0xd8, 0xf0, 0xe0, 0xf0, 0xd8, 0xcc, 0xc6, 0xc3},//['K' - 32] = 43
            {0x00, 0x00, 0xff, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0},//['L' - 32] = 44
            {0x00, 0x00, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xdb, 0xff, 0xff, 0xe7, 0xc3},//['M' - 32] = 45
            {0x00, 0x00, 0xc7, 0xc7, 0xcf, 0xcf, 0xdf, 0xdb, 0xfb, 0xf3, 0xf3, 0xe3, 0xe3},//['N' - 32] = 46
            {0x00, 0x00, 0x7e, 0xe7, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xe7, 0x7e},//['O' - 32] = 47
            {0x00, 0x00, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xfe, 0xc7, 0xc3, 0xc3, 0xc7, 0xfe},//['P' - 32] = 48
            {0x00, 0x00, 0x3f, 0x6e, 0xdf, 0xdb, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0x66, 0x3c},//['Q' - 32] = 49
            {0x00, 0x00, 0xc3, 0xc6, 0xcc, 0xd8, 0xf0, 0xfe, 0xc7, 0xc3, 0xc3, 0xc7, 0xfe},//['R' - 32] = 50
            {0x00, 0x00, 0x7e, 0xe7, 0x03, 0x03, 0x07, 0x7e, 0xe0, 0xc0, 0xc0, 0xe7, 0x7e},//['S' - 32] = 51
            {0x00, 0x00, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0xff},//['T' - 32] = 52
            {0x00, 0x00, 0x7e, 0xe7, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3},//['U' - 32] = 53
            {0x00, 0x00, 0x18, 0x3c, 0x3c, 0x66, 0x66, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3},//['V' - 32] = 54
            {0x00, 0x00, 0xc3, 0xe7, 0xff, 0xff, 0xdb, 0xdb, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3},//['W' - 32] = 55
            {0x00, 0x00, 0xc3, 0x66, 0x66, 0x3c, 0x3c, 0x18, 0x3c, 0x3c, 0x66, 0x66, 0xc3},//['X' - 32] = 56
            {0x00, 0x00, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x3c, 0x3c, 0x66, 0x66, 0xc3},//['Y' - 32] = 57
            {0x00, 0x00, 0xff, 0xc0, 0xc0, 0x60, 0x30, 0x7e, 0x0c, 0x06, 0x03, 0x03, 0xff},//['Z' - 32] = 58
            {0x00, 0x03, 0x03, 0x06, 0x06, 0x0c, 0x0c, 0x18, 0x18, 0x30, 0x30, 0x60, 0x60},//['[' - 32] = 59
            {0x00, 0x03, 0x03, 0x06, 0x06, 0x0c, 0x0c, 0x18, 0x18, 0x30, 0x30, 0x60, 0x60},//['\' - 32] = 60 currently wrong symbol
            {0x00, 0x03, 0x03, 0x06, 0x06, 0x0c, 0x0c, 0x18, 0x18, 0x30, 0x30, 0x60, 0x60},//[']' - 32] = 61
            {0x00, 0x00, 0x3c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x3c},//['^' - 32] = 62
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xc3, 0x66, 0x3c, 0x18},//['_' - 32] = 63
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x00},//['`' - 32] = 64 currently wrong symbol
            {0x00, 0x00, 0x7f, 0xc3, 0xc3, 0x7f, 0x03, 0xc3, 0x7e, 0x00, 0x00, 0x00, 0x00},//['a' - 32] = 65
            {0x00, 0x00, 0xfe, 0xc3, 0xc3, 0xc3, 0xc3, 0xfe, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0},//['b' - 32] = 66
            {0x00, 0x00, 0x7e, 0xc3, 0xc0, 0xc0, 0xc0, 0xc3, 0x7e, 0x00, 0x00, 0x00, 0x00},//['c' - 32] = 67
            {0x00, 0x00, 0x7f, 0xc3, 0xc3, 0xc3, 0xc3, 0x7f, 0x03, 0x03, 0x03, 0x03, 0x03},//['d' - 32] = 68
            {0x00, 0x00, 0x7f, 0xc0, 0xc0, 0xfe, 0xc3, 0xc3, 0x7e, 0x00, 0x00, 0x00, 0x00},//['e' - 32] = 69
            {0x00, 0x00, 0x30, 0x30, 0x30, 0x30, 0x30, 0xfc, 0x30, 0x30, 0x30, 0x33, 0x1e},//['f' - 32] = 70
            {0x7e, 0xc3, 0x03, 0x03, 0x7f, 0xc3, 0xc3, 0xc3, 0x7e, 0x00, 0x00, 0x00, 0x00},//['g' - 32] = 71
            {0x00, 0x00, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xc3, 0xfe, 0xc0, 0xc0, 0xc0, 0xc0},//['h' - 32] = 72
            {0x00, 0x00, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x00, 0x00, 0x18, 0x00},//['i' - 32] = 73
            {0x38, 0x6c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x00, 0x00, 0x0c, 0x00},//['j' - 32] = 74
            {0x00, 0x00, 0xc6, 0xcc, 0xf8, 0xf0, 0xd8, 0xcc, 0xc6, 0xc0, 0xc0, 0xc0, 0xc0},//['k' - 32] = 75
            {0x00, 0x00, 0x7e, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x78},//['l' - 32] = 76
            {0x00, 0x00, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xdb, 0xfe, 0x00, 0x00, 0x00, 0x00},//['m' - 32] = 77
            {0x00, 0x00, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0xfc, 0x00, 0x00, 0x00, 0x00},//['n' - 32] = 78
            {0x00, 0x00, 0x7c, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0x7c, 0x00, 0x00, 0x00, 0x00},//['o' - 32] = 79
            {0xc0, 0xc0, 0xc0, 0xfe, 0xc3, 0xc3, 0xc3, 0xc3, 0xfe, 0x00, 0x00, 0x00, 0x00},//['p' - 32] = 80
            {0x03, 0x03, 0x03, 0x7f, 0xc3, 0xc3, 0xc3, 0xc3, 0x7f, 0x00, 0x00, 0x00, 0x00},//['q' - 32] = 81
            {0x00, 0x00, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xe0, 0xfe, 0x00, 0x00, 0x00, 0x00},//['r' - 32] = 82
            {0x00, 0x00, 0xfe, 0x03, 0x03, 0x7e, 0xc0, 0xc0, 0x7f, 0x00, 0x00, 0x00, 0x00},//['s' - 32] = 83
            {0x00, 0x00, 0x1c, 0x36, 0x30, 0x30, 0x30, 0x30, 0xfc, 0x30, 0x30, 0x30, 0x00},//['t' - 32] = 84
            {0x00, 0x00, 0x7e, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0x00, 0x00, 0x00, 0x00},//['u' - 32] = 85
            {0x00, 0x00, 0x18, 0x3c, 0x3c, 0x66, 0x66, 0xc3, 0xc3, 0x00, 0x00, 0x00, 0x00},//['v' - 32] = 86
            {0x00, 0x00, 0xc3, 0xe7, 0xff, 0xdb, 0xc3, 0xc3, 0xc3, 0x00, 0x00, 0x00, 0x00},//['w' - 32] = 87
            {0x00, 0x00, 0xc3, 0x66, 0x3c, 0x18, 0x3c, 0x66, 0xc3, 0x00, 0x00, 0x00, 0x00},//['x' - 32] = 88
            {0xc0, 0x60, 0x60, 0x30, 0x18, 0x3c, 0x66, 0x66, 0xc3, 0x00, 0x00, 0x00, 0x00},//['y' - 32] = 89
            {0x00, 0x00, 0xff, 0x60, 0x30, 0x18, 0x0c, 0x06, 0xff, 0x00, 0x00, 0x00, 0x00},//['z' - 32] = 90
            {0x00, 0x00, 0x0f, 0x18, 0x18, 0x18, 0x38, 0xf0, 0x38, 0x18, 0x18, 0x18, 0x0f},//['{' - 32] = 91
            {0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18},//['|' - 32] = 92
            {0x00, 0x00, 0xf0, 0x18, 0x18, 0x18, 0x1c, 0x0f, 0x1c, 0x18, 0x18, 0x18, 0xf0},//['}' - 32] = 93
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x8f, 0xf1, 0x60, 0x00, 0x00, 0x00},//['~' - 32] = 94
            {0x00, 0x00, 0x7c, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0x7c, 0x00, 0x00, 0x00, 0x00},//['¤' - 32] = 95 DEL
    };

    public TextWriter(){
        assert !TextWriter.isSet :"TextWriter is already set!";
        TextWriter.setInstance();
        if(!buildAutoCorrect("./resources/files/words/WordsUSA.txt")){TextWriter.autoWords =null;}
    }

    private static void readTTFFile(){
        PassedCheck psc = new PassedCheck();
        self.ttf = new TTFFile("./resources/files/fonts/Quicksand-Bold.ttf");
        IOHandler.parseTTFFile(self.ttf,false,psc);
        self.ttf.setFileInfo();
        self.ttf.setUpFontMap();
        self.ttf.setUpCharMap();
        //self.ttf.clearTable();
        //self.ttf.dumpCharMap();

        //self.ttf.printFileInfo();
        //self.ttf.printTableInfo();
        setUnitsPerEm();
    }

    static void setUnitsPerEm(){
        self.unitsPerEm = self.ttf.getUnitsPerEm();
        self.CHAR_FONT_GAP = self.ttf.getFontGap();
        self.CHAR_FONT_WIDTH = self.ttf.getFontMaxWidth();
        self.CHAR_FONT_HEIGHT = self.ttf.getFontMaxHeigth();
    }

    public boolean buildAutoCorrect(String pathWords){
        TextWriter.autoWords = new AutoWords();
        return IOHandler.readFromWordList(pathWords,TextWriter.autoWords);
    }

    private static void setInstance(){
        TextWriter.isSet = true;
    }

    public static void initTextWriter(TextWriterType typeOfText){
        if(self == null) {
            TextWriter.typeOfText = typeOfText;
            self = new TextWriter();
        }
        if(TextWriter.typeOfText == TextWriterType.FONT_WRITER)readTTFFile();
    }

    public static int getCharWidth(){return self.CHAR_WIDTH;}

    public static int getCharHeight(){return self.CHAR_HEIGHT;}

    public static int getCharPadDummy(){return self.CHAR_PAD_DUMMY;}

    public static void drawText(String text,int x,int y,int color){
        int i = 0,baseX = x;
        char c;
        while(i < text.length()){
            if((c=text.charAt(i++)) == self.NEW_LINE){y+=self.CHAR_HEIGHT*2;x=baseX;}
            else{x += self.drawChar(c, x, y,color);}
        }
    }

    public static void drawTextLine(String text,int x,int y,int maxSize,int color){
        int i = 0,baseX = x;
        char c;
        while(i < text.length()){
            if(x > baseX+(maxSize-self.CHAR_WIDTH) ){y+=self.CHAR_HEIGHT*2;x=baseX;}
            x += self.drawChar(text.charAt(i++), x, y,color);
        }
    }

    public static void drawCharBuffer(char[] buf,int x,int y,int col,int color){
        int i = 0,baseX = x;
        char c;
        while(buf[i] != self.END_OF_BUF){
            c=buf[i];
            if(i % col == 0 && i != 0){
                y+=self.CHAR_HEIGHT*2;
                x=baseX;
            }
            x += self.drawChar(c, x, y,color);
            i++;
        }
    }

    private int drawChar(char c,int x,int y,int color){
        int i, j;
        c = (char)(c & 0x7F); // set max to 127,shift = 127,enter = 10 '\n',backspace = 8
        if(c < ' '){c = 0;}
        else if(c == 127)return 0; // shift
        else{c -= ' ';}      // sub 32
        int index = c;
        char[] chr = self.charBuf[index];
        for (j = 0; j < CHAR_WIDTH; j++) {
            for (i = 0; i < CHAR_HEIGHT; i++){
                if((chr[j] & (1 << i))!=0){
                    CanvasHandler.setPixel(x+(CHAR_HEIGHT-i),y-j,color);
                }
            }
        }
        return CHAR_WIDTH + CHAR_GAP;
    }

    public static void drawFontCharBuffer(char[] buf,int x,int y,int col,int fontSize,int color){
        float scale = self.unitsPerEm*fontSize;
        int i = 0,baseX = x;
        char c;
        while(buf[i] != self.END_OF_BUF){
            c=buf[i];
            if(i % col == 0 && i != 0){
                y+=((self.CHAR_FONT_HEIGHT)*scale);
                x=baseX;
            }
            x += self.fillFontChar(c, x, y,scale,color);
            //x += self.drawFontChar(c, x, y,scale,color);
            i++;
        }
    }

    private int drawFontChar(char c,int x,int y,float scale,int color){
        int i,j=0;
        c = (char)(c & 0x7F);// set max to 127 (125 == 0x7D),shift = 127,enter = 10 '\n',backspace = 8
        if(c < ' '){c = 0;}
        else if(c == 127)return 0; // shift
        else{c -= ' ';} // sub 32
        int index = c;
        FontChar font = self.ttf.getFontCharByIndex(index);
        if(index == 0){return (int)((float)self.CHAR_FONT_WIDTH*scale);}
        while(j<font.splines.length){
            Point[] p = font.splines[j++].splinePoints;
            i=1;
            while(i<p.length){
                int x1 = (int)((p[i-1].x+font.lsb)*scale)+x;
                int x2 = (int)((p[i].x+font.lsb)*scale)+x;
                int y1 = (int)((p[i-1].y)*scale)+y;
                int y2 = (int)((p[i].y)*scale)+y;
                Line.drawLine(x1,y1,x2,y2,color);
                i++;
            }
        }
        return (int)((float)(font.width+font.lsb)*scale);
    }

    private int fillFontChar(char c,int x,int y,float scale,int color){
        c = (char)(c & 0x7F);// set max to 127 (125 == 0x7D),shift = 127,enter = 10 '\n',backspace = 8
        if(c < ' '){c = 0;}
        else if(c == 127)return 0; // shift
        else{c -= ' ';} // sub 32
        int index = c;
        FontChar font = self.ttf.getFontCharByIndex(index);

        if(index == 0){return (int)((float)self.CHAR_FONT_WIDTH*scale);}

        int fy = font.y-font.height;
        int x1 = (int)((font.x+font.lsb)*scale)+x;
        int x2 = (int)((font.x+font.width+font.lsb)*scale)+x;
        int y1 = (int)(fy*scale)+y;
        int y2 = (int)((fy+font.height)*scale)+y;

        int u1 = 0;
        int v1 = 0;
        int u2 = 1;
        int v2 = 1;

        Tri tri1,tri2;

        tri1 = new Tri(x1,y1,u1,v1,x1,y2,u1,v2,x2,y2,u2,v2);
        tri2 = new Tri(x1,y1,u1,v1,x2,y2,u2,v2,x2,y1,u2,v1);
        Triangle.texturedTriangle(tri1,font.texture,bitmapWidth,bitmapHeight,color);
        Triangle.texturedTriangle(tri2,font.texture,bitmapWidth,bitmapHeight,color);

        return (int)((float)(font.width+font.lsb)*scale);
    }
}