package helper.struct;
import helper.enums.WaveBits;
import helper.enums.WaveFormatType;
import helper.io.IOHandler;

import java.io.DataInputStream;

import static helper.io.IOHandler.*;
import static helper.methods.CommonMethods.byteBufToString;
import static helper.methods.CommonMethods.littleEndianToBigEndian;
import static helper.enums.WaveBits.RIFF;
import static helper.enums.WaveBits.FMT_CHUNK_MARKER;
import static helper.enums.WaveBits.DATA_CHUNK_HEADER;
import static helper.enums.WaveBits.WAVE;
import static helper.struct.SMDateTime.secondsToTime;

//unsigned char data_chunk_header [4] DATA string or FLLR string
//unsigned char fmt_chunk_marker[4]  fmt string with trailing null char
//unsigned char riff[4]              RIFF string
//unsigned char wave[4]              WAVE string
//unsigned int overall_size          overall size of file in bytes
//unsigned int length_of_fmt         length of the format data
//unsigned int format_type           format type. 1-PCM, 3- IEEE float, 6 - 8bit A law, 7 - 8bit mu law
//unsigned int channels              no.of channels
//unsigned int sample_rate           sampling rate (blocks per second)
//unsigned int byterate              SampleRate * NumChannels * BitsPerSample/8
//unsigned int block_align           NumChannels * BitsPerSample/8
//unsigned int bits_per_sample       bits per sample, 8- 8bits, 16- 16 bits etc
//unsigned int data_size;             NumSamples * NumChannels * BitsPerSample/8 - size of the next chunk that will be read

//1 – 4	“RIFF”	Marks the file as a riff file. Characters are each 1 byte long.
//5 – 8	File size (integer)	Size of the overall file – 8 bytes, in bytes (32-bit integer). Typically, you’d fill this in after creation.
//9 -12	“WAVE”	File Type Header. For our purposes, it always equals “WAVE”.
//13-16	“fmt “	Format chunk marker. Includes trailing null
//17-20	16	Length of format data as listed above
//21-22	1	Type of format (1 is PCM) – 2 byte integer
//23-24	2	Number of Channels – 2 byte integer
//25-28	44100	Sample Rate – 32 byte integer. Common values are 44100 (CD), 48000 (DAT). Sample Rate = Number of Samples per second, or Hertz.
//29-32	176400	(Sample Rate * BitsPerSample * Channels) / 8.
//33-34	4	(BitsPerSample * Channels) / 8.1 – 8 bit mono2 – 8 bit stereo/16 bit mono4 – 16 bit stereo
//35-36	16	Bits per sample
//37-40	“data”	“data” chunk header. Marks the beginning of the data section.
//41-44	File size (data)	Size of the data section.

//http://truelogic.org/wordpress/2015/09/04/parsing-a-wav-file-in-c/
public class WaveFile {
    public byte[] riff,wave,fmtChunkMarker,dataChunkHeader;
    public long numSamples,sizeOfEachSample,lowLimit,highLimit;
    public int overallSize,lengthOfFmt,formatType,channels,sampleRate,byteRate,blockAlign,bitsPerSample,dataSize;
    public final int BUF_OFFSET = 44,SAMPLE_CHUNK = 256;
    public WaveFormatType format;
    public String path;
    public String[] fileInfo;
    public SamplePair[] sampleDataPairs;
    PassedCheck operation;
    public WaveFile(String dir){
        path = dir;
        operation = new PassedCheck();
        riff = new byte[RIFF.getValue()];
        fmtChunkMarker = new byte[FMT_CHUNK_MARKER.getValue()];
        dataChunkHeader = new byte[DATA_CHUNK_HEADER.getValue()];
        wave = new byte[WAVE.getValue()];
    }

    public boolean readFile(){
        parseWaveFile(this,operation);
        if(!operation.passed){
            IOHandler.logToFile(operation.message);
            return false;
        }
        setFileInfo();
        return readSampleData();
    }

    boolean readSampleData(){
        readWaveSampleData(this,false,operation);
        if(!operation.passed){
            IOHandler.logToFile(operation.message);
            return false;
        }

        return true;
        //else printFileInfo();
    }

    void getSampleChunks(){
        //sampleDataPairs = new SamplePair[(int)numSamples/SAMPLE_CHUNK];
        // for each chunk compute min max value
        // draw lines between
    }

    public void printFileInfo(){
        printWaveFileInfo(getFileInfo());
    }

    public String[] getFileInfo(){
        return fileInfo;
    }

    void setFileInfo(){
        fileInfo = new String[18];
        float duration_in_seconds = (float) overallSize / byteRate;
        fileInfo[0] = ("(1-4)   %s".formatted(byteBufToString(riff)));
        fileInfo[1] = ("(5-8)   Overall size: bytes:%d, Kb:%d".formatted(overallSize,overallSize/1024));
        fileInfo[2] = ("(9-12)  Wave marker: %s".formatted(byteBufToString(wave)));
        fileInfo[3] = ("(13-16) Fmt marker: %s".formatted(byteBufToString(fmtChunkMarker)));
        fileInfo[4] = ("(17-20) Length of Fmt header: %d".formatted(lengthOfFmt));
        fileInfo[5] = ("(21-22) Format type: %d %s".formatted(formatType,format.getName()));
        fileInfo[6] = ("(23-24) Channels: %d".formatted(channels));
        fileInfo[7] = ("(25-28) Sample rate: %d".formatted(sampleRate));
        fileInfo[8] = ("(29-32) Byte Rate: %d , Bit Rate:%d".formatted(byteRate,byteRate*8));
        fileInfo[9] = ("(33-34) Block Alignment: %d".formatted(blockAlign));
        fileInfo[10] = ("(35-36) Bits per sample: %d".formatted(bitsPerSample));
        fileInfo[11] = ("(37-40) Data Marker: %s".formatted(byteBufToString(dataChunkHeader)));
        fileInfo[12] = ("(41-44) Size of data chunk: %d".formatted(dataSize));
        fileInfo[13] = ("Number of samples:%d".formatted(numSamples));
        fileInfo[14] = ("Size of each sample:%d bytes".formatted(sizeOfEachSample));
        fileInfo[15] = ("Approx.Duration in seconds=%f".formatted(duration_in_seconds));
        fileInfo[16] = ("Approx.Duration in h:m:s=%s".formatted(secondsToTime(duration_in_seconds)));
        fileInfo[17] = ("Valid range for data values : %d to %d".formatted(lowLimit,highLimit));
    }

    public void convertToSize(WaveBits dst, byte[] buf){
        switch(dst){
            case OVERALL_SIZE:{
                overallSize = littleEndianToBigEndian(buf,0,4);
                break;
            }
            case DATA_SIZE:{
                dataSize = littleEndianToBigEndian(buf,0,4);
                break;
            }
            case LENGTH_OF_FMT:{
                lengthOfFmt = littleEndianToBigEndian(buf,0,4);
                break;
            }
            case SAMPLE_RATE:{
                sampleRate = littleEndianToBigEndian(buf,0,4);
                break;
            }
            case BYTE_RATE:{
                byteRate = littleEndianToBigEndian(buf,0,4);
                break;
            }
            case FORMAT_TYPE:{
                formatType = littleEndianToBigEndian(buf,0,2);
                break;
            }
            case CHANNELS:{
                channels = littleEndianToBigEndian(buf,0,2);
                break;
            }
            case BLOCK_ALIGN:{
                blockAlign = littleEndianToBigEndian(buf,0,2);
                break;
            }
            case BITS_PER_SAMPLE:{
                bitsPerSample = littleEndianToBigEndian(buf,0,2);
                break;
            }
        }
    }

    public void setLimits(){
        switch (bitsPerSample) {
            case 8:
                lowLimit = -128;
                highLimit = 127;
                break;
            case 16:
                lowLimit = -32768;
                highLimit = 32767;
                break;
            case 32:
                lowLimit = -2147483648;
                highLimit = 2147483647;
                break;
        }
    }

    public void getSampleSize(){
        numSamples = ((long)8 * dataSize) / ((long)channels * bitsPerSample);
        sizeOfEachSample = ((long)channels * bitsPerSample) / 8;
    }

    public void formatTypeToName(){
        if(formatType == 1)format = WaveFormatType.PCM;
        else if(formatType == 6)format = WaveFormatType.ALAW;
        else if(formatType == 7)format = WaveFormatType.MULAW;
    }

}
