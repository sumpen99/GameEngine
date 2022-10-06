package helper.enums;

public enum WAVEBITS {
    DATA_CHUNK_HEADER(4),
    FMT_CHUNK_MARKER(4),
    RIFF(4),
    WAVE(4),
    OVERALL_SIZE(0),
    LENGTH_OF_FMT(0),
    FORMAT_TYPE(0),
    CHANNELS(0),
    SAMPLE_RATE(0),
    BYTE_RATE(0),
    BLOCK_ALIGN(0),
    BITS_PER_SAMPLE(0),
    DATA_SIZE(0);

    private final int value;
    WAVEBITS(int value){this.value = value;}
    public int getValue(){return this.value;}

}
