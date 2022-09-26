package helper.methods;
import helper.drawobjects.TextWriter;
import helper.enums.Token;
import helper.struct.AutoWord;
import helper.struct.Edit;
import helper.struct.PriorityQueue;
import static helper.sort.QuickSort.sortPriorityQueue;
import static helper.methods.CommonMethods.offsetString;

public class Levenstein {
    //https://rosettagit.org/drafts/levenshtein-distance-alignment/ C
    // Creel https://www.youtube.com/watch?v=Cu7Tl7FGigQ&t=981s
    public static boolean checkForSuggestion(String strIn,String[] wordsOut) {
        char EMPTY_CHAR = Token.EMPTY_CHAR.getChar();
        int col,row,la,lb,i,j;
        if(strIn == null || (la = strIn.length()) <= 1)return false;
        Edit[] tbl;
        strIn = strIn.toLowerCase();
        PriorityQueue pQueue = new PriorityQueue(TextWriter.autoWords.count);
        for (int k = 0; k < TextWriter.autoWords.count; k++){
            String strOut = TextWriter.autoWords.GetNextWord(k);
            lb = strOut.length();
            row = la+2;
            col = lb+2;
            tbl = new Edit[row*col];
            Edit.setRowCol(tbl,row,col);
            for(i = la;i >=0;i--){
                char aa = offsetString(strIn,i,EMPTY_CHAR);
                for(j = lb;j>=0;j--){
                    char bb = offsetString(strOut,j,EMPTY_CHAR);
                    if(aa == EMPTY_CHAR && bb == EMPTY_CHAR)continue;
                    Edit e = tbl[Edit.getIndex(i,j)];
                    Edit repl = tbl[Edit.getIndex(i+1,j+1)];
                    Edit dela = tbl[Edit.getIndex(i+1,j)];
                    Edit delb = tbl[Edit.getIndex(i,j+1)];
                    e.c1 = aa;
                    e.c2 = bb;

                    if(aa == EMPTY_CHAR){
                        e.next = delb;
                        e.n = e.next.n + 1;
                        continue;
                    }
                    if(bb == EMPTY_CHAR){
                        e.next = dela;
                        e.n = e.next.n + 1;
                        continue;
                    }
                    e.next = repl;
                    if((aa == bb)){
                        e.n = e.next.n;
                        continue;
                    }
                    if(e.next.n > delb.n){
                        e.next = delb;
                        e.c1 = EMPTY_CHAR;
                    }
                    if(e.next.n > dela.n){
                        e.next = dela;
                        e.c1 = aa;
                        e.c2 = EMPTY_CHAR;
                    }
                    e.n = e.next.n + 1;

                }
            }

            if (tbl[0].n == 0) {
                return false;
            }

            pQueue.insertWord(new AutoWord(k,tbl[0].n,strOut));
        }
        sortPriorityQueue(pQueue,0,pQueue.wordCount-1);
        for(int l = 0;l < wordsOut.length;l++){
            wordsOut[l] = pQueue.bufWords[l].word;
        }
        return true;
    }

}
