package helper.struct;
import helper.enums.Token;
import helper.io.IOHandler;
import helper.list.SMHashMap;

import static helper.enums.EntrieType.*;
import static helper.enums.Token.*;
import static helper.methods.StringToEnum.getJsonToken;


//{"detail":"Invalid token."}
//{"token":"79ae81c534a3fcb903d3d4598bac4c99e22474ce"}
//[{"id":2,"name":"Fredrik","score":100},{"id":3,"name":"Johan","score":99}]
//{"id":2,"name":"Fredrik","score":100}
public class JsonObject {
    public SMHashMap objMap;
    public JsonList objList;
    public int objCount,listCount,dataSize;
    String rawData;


    public JsonObject(String data){
        rawData = data;
        dataSize = data.length();
        objMap = new SMHashMap(100,0.75f);
        objCount = 0;
        listCount = 0;
        parseData();
        rawData = null;
    }

    public void getHighScoreValues(String[] table){
        if(listCount > 0){
            for(int i = 0;i < objList.objCount;i++){
                SMHashMap objMap = objList.objMaps[i];
                String valueName = (String)objMap.getValue("name");
                String valueScore = (String)objMap.getValue("score");
                if(i < table.length){table[i] = "%d. %s %s".formatted(i+1,valueName,valueScore);}
                else break;
                // update args and we should be good to go
                //IOHandler.printStringMessage(valueName,valueScore);
            }
        }
        else if(objCount > 0){
            String valueName = (String)objMap.getValue("name");
            String valueScore = (String)objMap.getValue("score");
            table[0] = "1. %s %s".formatted(valueName,valueScore);
            //IOHandler.printStringMessage(valueName,valueScore);
        }
    }

    public void getServerResponse(){
        if(objCount > 0){
            String response = (String)objMap.getValue("message");
            IOHandler.printStringMessage("Server said",response);
        }
    }

    void parseData(){
        GlobalCounter i = new GlobalCounter();
        char token;
        for(;i.index < dataSize;i.index++){
            token = rawData.charAt(i.index);
            if(getJsonToken(token) == JSON_OPEN_DIC){
                objCount++;
                parseDictionary(i,objMap);
            }
            else if(getJsonToken(token) == JSON_OPEN_LIST){
                int objCount = getEntrieCount(i.index);
                objList = new JsonList(objCount);
                listCount++;
                parseListObject(i,objList);
            }
        }
    }

    void parseDictionary(GlobalCounter j,SMHashMap hashMap){
        int objCount;
        String value = "";
        GlobalCounter next = new GlobalCounter();
        next.index = j.index;
        Token token;
        while(validIndex(next.index) && getJsonToken(rawData.charAt(next.index)) != JSON_CLOSE_DIC){
            while(getJsonToken(rawData.charAt(next.index)) != JSON_OPEN_STRING){next.index++;}
            String root = parseStringObject(next);
            //IOHandler.printString(root);
            ++next.index;
            token = getJsonToken(rawData.charAt(next.index));
            if(token == JSON_OPEN_LIST){
                objCount = getEntrieCount(next.index);
                JsonList jsonList = new JsonList(objCount);
                hashMap.addNewItem(root,jsonList,ENTRIE_JSON_LIST);
                parseListObject(next,jsonList);
            }
            else if(token == JSON_OPEN_STRING){
                value = parseStringObject(next);
                //IOHandler.printString(value);
                hashMap.addNewItem(root, value,ENTRIE_JSON_STRING);
            }
            else if(token == JSON_NUMBER_VALUE){
                value = parseStringNumbers(next);
                //IOHandler.printString(value);
                hashMap.addNewItem(root, value,ENTRIE_JSON_STRING);
            }
            else if(token == JSON_STRING_VALUE){
                value = parseStringNumbers(next);
                hashMap.addNewItem(root, value,ENTRIE_JSON_STRING);
            }
            next.index++;
        }
        j.index = next.index;
    }

    void parseListObject(GlobalCounter j,JsonList list){
        GlobalCounter next = new GlobalCounter();
        next.index = j.index+1;
        Token token;
        while((token = getJsonToken(rawData.charAt(next.index))) != JSON_CLOSE_LIST){
            if(token == JSON_OPEN_DIC){parseDictionary(next,list.objMaps[list.objCount++]);}
            next.index++;
        }
        j.index = next.index;
    }

    String parseStringObject(GlobalCounter j){
        String strBuf = "";
        int skip = j.index+1;
        while(getJsonToken(rawData.charAt(skip)) != JSON_OPEN_STRING){strBuf+=rawData.charAt(skip++);}
        j.index = skip+1;
        return strBuf;
    }

    String parseStringNumbers(GlobalCounter j){
        String strBuf = "";
        int skip = j.index;
        while(getJsonToken(rawData.charAt(skip)) == JSON_NUMBER_VALUE){strBuf+=rawData.charAt(skip++);}
        j.index = skip-1;
        return strBuf;
    }

    int getEntrieCount(int j){
        int skip,obj_cnt;
        skip = j;
        obj_cnt = 0;
        Token token;
        while((token = getJsonToken(rawData.charAt(skip++))) != JSON_CLOSE_LIST){
            if(token == JSON_CLOSE_DIC){obj_cnt++;}
        }
        return obj_cnt;
    }

    boolean validIndex(int index){
        return index<rawData.length();
    }
}
