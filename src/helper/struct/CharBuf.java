package helper.struct;
import helper.enums.Token;
import helper.io.IOHandler;

public class CharBuf {
    public char[] buf;
    public int maxSize;
    final char END_OF_BUF = Token.END_OF_BUF.getChar();

    final char SKIP_CHAR = Token.SKIP_CHAR.getChar();

    public CharBuf(int col,int row){
        this.buf = new char[(col*row)+1];
        maxSize = col*row;
        initColRow();

    }

    public void initColRow(){
       int cnt = 0;
       while(cnt < maxSize){
           buf[cnt] = SKIP_CHAR;
           cnt++;
       }
       buf[maxSize] = END_OF_BUF;
    }

    public boolean containsText(){
        int count = 0;
        while(count < maxSize){
            if(buf[count++] != SKIP_CHAR)return true;
        }
        return false;
    }

    public void appendCharByIndex(int index,char c){
        if(validIndex(index)){
            // [a,b,c,d,'','','','','']
            // [a,b,c,d,'','','','','']
            // ['new','a','b','c','d','','','','']
            buf[index] = c;
            /*if(inRangeOf(2,index,currentIndex)){
                //peek(index);
                buf[index] = c;
                currentIndex = index;
                //buf[index+1] = '\0';

            }*/
            /*else if(index < currentIndex){
                int pad = count;
                //pad = 4
                //count = 4
                //last = 5
                while(pad > index){
                    buf[pad] = buf[pad-1];
                    pad--;
                }
                buf[index] = c;
            }
            else{
                int seek = currentIndex;
                while(seek < index){
                    if(buf[seek] == END_OF_BUF){
                        buf[seek] = '\n';
                    }
                    seek++;
                }
                buf[index] = c;
                currentIndex = index;
                buf[index+1] = '\0';
            }*/
        }
    }

    public boolean removeCharByIndex(int index){
        if(validIndex(index)){
            this.buf[index] = SKIP_CHAR;
            return true;
        }
        return false;
    }

    public int findCharFromIndex(char token,int index){
        int i = index,cnt = 0;

        while(i>=0){
            if(buf[i] != token && buf[i] != SKIP_CHAR)break;
            i--;
            cnt++;
        }
        return cnt;
    }

    public String getSubStringIndexBackwards(int index,char stop){
        int indexStop = index;
        while(validIndex(index) && buf[index] != stop){
            index--;
        }
        String strOut = "";
        index+=1;
        while(index<=indexStop){strOut+=buf[index++];}
        return strOut;
    }

    public String getSubStringIndexForward(int index,char stop){
        int indexStart = index;
        while(validIndex(index) && buf[index] != stop){
            index++;
        }
        String strOut = "";
        while(indexStart<index){strOut+=buf[indexStart++];}
        return strOut;
    }

    public int insertWordAt(int indexStart,String word,int endIndex){
        int count = 0,base = indexStart;
        while(validIndex(indexStart) && count < word.length()){
            buf[indexStart++] = word.charAt(count++);
        }
        while(indexStart<endIndex){buf[indexStart++] = SKIP_CHAR;}
        return base+count+1;
    }

    private boolean validIndex(int index){return (index >= 0 && index < this.maxSize);}

    public char getCharByIndex(int index){
        if(validIndex(index))return this.buf[index];
        return END_OF_BUF;
    }


}
