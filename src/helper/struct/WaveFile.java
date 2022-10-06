package helper.struct;
import helper.enums.WAVEBITS;
import helper.io.IOHandler;

import static helper.enums.WAVEBITS.RIFF;
import static helper.enums.WAVEBITS.FMT_CHUNK_MARKER;
import static helper.enums.WAVEBITS.DATA_CHUNK_HEADER;
import static helper.enums.WAVEBITS.WAVE;
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
    public char[] riff,wave,fmtChunkMarker,dataChunkHeader;
    public int overallSize,lengthOfFmt,formatType,channels,sampleRate,byteRate,blockAlign,bitsPerSample,dataSize;
    public String formatName;
    public WaveFile(){
        riff = new char[RIFF.getValue()];
        fmtChunkMarker = new char[FMT_CHUNK_MARKER.getValue()];
        dataChunkHeader = new char[DATA_CHUNK_HEADER.getValue()];
        wave = new char[WAVE.getValue()];
    }

    public void readFile(String path){
        IOHandler.parseWaveFile(path,this);
    }

    public void littleEndianToBigEndian(WAVEBITS dst,char[] buf){
        switch(dst){
            case OVERALL_SIZE:{
                overallSize = buf[0] | (buf[1] << 8) | (buf[2] << 16) | (buf[3] << 24);
                break;
            }
            case DATA_SIZE:{
                dataSize = buf[0] | (buf[1] << 8) | (buf[2] << 16) | (buf[3] << 24);
                break;
            }
            case LENGTH_OF_FMT:{
                lengthOfFmt = buf[0] | (buf[1] << 8) | (buf[2] << 16) | (buf[3] << 24);
                break;
            }
            case SAMPLE_RATE:{
                sampleRate = buf[0] | (buf[1] << 8) | (buf[2] << 16) | (buf[3] << 24);
                break;
            }
            case BYTE_RATE:{
                byteRate = buf[0] | (buf[1] << 8) | (buf[2] << 16) | (buf[3] << 24);
                break;
            }
            case FORMAT_TYPE:{
                formatType = buf[0] | (buf[1] << 8);
                break;
            }
            case CHANNELS:{
                channels = buf[0] | (buf[1] << 8);
                break;
            }
            case BLOCK_ALIGN:{
                blockAlign = buf[0] | (buf[1] << 8);
                break;
            }
            case BITS_PER_SAMPLE:{
                bitsPerSample = buf[0] | (buf[1] << 8);
                break;
            }
        }
    }

    public void formatTypeToName(){
        if(formatType == 1)formatName = "PCM";
        else if(formatType == 6)formatName = "A-law";
        else if(formatType == 7)formatName = "Mu-law";
    }

}
