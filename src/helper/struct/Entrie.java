package helper.struct;
import helper.enums.EntrieType;

import static helper.methods.HashHelper.hashKey;

public class Entrie {
    public String key;
    public Object value;
    public int bucket;
    public boolean set;
    public Entrie next;
    public EntrieType eType;

    public Entrie(String key,Object value,int capacity,EntrieType eType){
        this.key = key;
        this.value = value;
        this.bucket = hashKey(key,capacity);
        this.set = true;
        this.eType = eType;
        this.next = null;
    }

    public Entrie(){
        this.key = "";
        this.value = null;
        this.next = null;
        this.bucket = 0;
        this.set = false;
        this.eType = null;
    }

    public void setValues(int bucket, String key, Object value,EntrieType eType){
        this.bucket = bucket;
        this.key = key;
        this.value = value;
        this.set = true;
        this.eType = eType;
        this.next = null;

    }

}
