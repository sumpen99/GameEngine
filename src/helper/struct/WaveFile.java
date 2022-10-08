package helper.struct;
import helper.enums.WaveBits;
import helper.enums.WaveFormatType;
import helper.io.IOHandler;
import static helper.methods.CommonMethods.littleEndianToBigEndian;
import static helper.enums.WaveBits.RIFF;
import static helper.enums.WaveBits.FMT_CHUNK_MARKER;
import static helper.enums.WaveBits.DATA_CHUNK_HEADER;
import static helper.enums.WaveBits.WAVE;
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
public class WaveFile {
    public byte[] riff,wave,fmtChunkMarker,dataChunkHeader;
    public long numSamples,sizeOfEachSample;
    public int overallSize,lengthOfFmt,formatType,channels,sampleRate,byteRate,blockAlign,bitsPerSample,dataSize;
    public WaveFormatType format;
    public WaveFile(){
        riff = new byte[RIFF.getValue()];
        fmtChunkMarker = new byte[FMT_CHUNK_MARKER.getValue()];
        dataChunkHeader = new byte[DATA_CHUNK_HEADER.getValue()];
        wave = new byte[WAVE.getValue()];
    }

    public void readFile(String path){
        IOHandler.parseWaveFile(path,this);
    }

    public void convertToSize(WaveBits dst, byte[] buf){
        switch(dst){
            case OVERALL_SIZE:{
                //IOHandler.printCharBuf(buf,true);
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
