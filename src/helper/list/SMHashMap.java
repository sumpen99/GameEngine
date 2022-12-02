package helper.list;
import helper.enums.EntrieType;
import helper.io.IOHandler;
import helper.struct.Entrie;
import static helper.methods.HashHelper.decodeUriComponent;
import static helper.methods.HashHelper.hashKey;



public class SMHashMap {
    public int count;
    public int capacity;
    public float loadFactor;
    public Entrie[] entries;

    public SMHashMap(int capacity,float loadFactor){
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.hashmapInit();
    }

    void hashmapInit(){
        this.entries = new Entrie[capacity];
        for(int i = 0;i<capacity;i++){entries[i] = new Entrie();}
    }

    void expandTable(){
        SMHashMap temp;
        Entrie h1,h2;
        int oldCapacity = capacity;
        capacity*=2;
        temp = new SMHashMap(capacity,loadFactor);
        for(int i = 0;i<oldCapacity;i++){
            if(entries[i].set){
                h1 = entries[i];
                while(h1 != null){
                    int bucket = hashKey(h1.key,capacity);
                    if(temp.entries[bucket].set){
                        h1 = extendCollision(temp.entries[bucket],h1);
                    }
                    else{
                        temp.entries[bucket].setValues(bucket,h1.key,h1.value,h1.eType);
                        h1 = h1.next;
                    }
                }
            }
        }
        entries = temp.entries;
    }

    public void addNewItem(String key,Object value,EntrieType eType){
        assert !containsKey(key) : "ID Already In Use";
        int bucket = hashKey(key,capacity);
        if(!entries[bucket].set){entries[bucket].setValues(bucket,key,value,eType);}
        else{
            Entrie e = new Entrie(key,value,bucket,eType);
            addCollision(entries[bucket],e);
        }
        count++;
        if((float)(count/capacity) > loadFactor)expandTable();

    }

    void addCollision(Entrie base,Entrie item){
        item.next = base.next;
        base.next = item;
    }

    Entrie extendCollision(Entrie base,Entrie item){
        Entrie e = null;
        if(item.next != null){
            e  = new Entrie(item.next.key,item.next.value,item.next.bucket,item.next.eType);
        }
        item.next = base.next;
        base.next = item;
        return e;
    }

    public boolean containsKey(String key){
        Entrie e;
        int bucket =  hashKey(key,capacity);
        if(entries[bucket].set){
            e = entries[bucket];
            while(e != null){
                if(e.key.equals(key)){return true;}
                e = e.next;
            }
        }
        return false;
    }

    public Entrie getObject(String key){
        Entrie e;
        int bucket =  hashKey(key,capacity);
        if(entries[bucket].set){
            e = entries[bucket];
            while(e != null){
                if(e.key.equals(key))return e;
                e = e.next;
            }
        }
        return null;
    }

    public Object getValue(String key){
        Entrie e;
        int bucket = hashKey(key,capacity);
        if(entries[bucket].set){
            //IOHandler.printString("Valid: %s Bucket: %d".formatted(key,bucket));
            e = entries[bucket];
            while(e != null){
                if(e.key.equals(key)){
                    return e.value;
                }
                else{
                    //IOHandler.printString("e.key: %s key: %s".formatted(e.key,key));
                }
                e = e.next;
            }
        }
        //IOHandler.printString("Invalid: %s Bucket: %d".formatted(key,bucket));
        return null;
    }

    public boolean bucketIsSet(String key){
        int bucket =  hashKey(key,capacity);
        return entries[bucket].set;
    }


}
