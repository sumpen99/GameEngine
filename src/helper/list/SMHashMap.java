package helper.list;
import helper.enums.EntrieType;
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
                    if(temp.entries[bucket].set){addCollision(temp.entries[bucket],h1);}
                    else{temp.entries[bucket].setValues(bucket,h1.key,h1.value,h1.eType);}
                    h1 = h1.next;
                }
            }
        }
        entries = null;
        count = 0;
        hashmapInit();
        for(int i = 0;i<capacity;i++){
            if(temp.entries[i].set){
                h2 = temp.entries[i];
                while(h2 != null){
                    if(entries[h2.bucket].set){addCollision(entries[h2.bucket],h2);}
                    else{entries[h2.bucket].setValues(h2.bucket,h2.key,h2.value,h2.eType);}
                    h2 = h2.next;
                }
            }
        }
    }

    public void addNewItem(String key,Object value,EntrieType eType){
        assert !containsKey(key) : "ID Already In Use";
        int bucket = hashKey(key,capacity);
        if(!entries[bucket].set){entries[bucket].setValues(bucket,key,value,eType);}
        else{
            Entrie e = new Entrie(key,value,capacity,eType);
            addCollision(entries[bucket],e);
        }
        count++;
        if((float)(count/capacity) > loadFactor)expandTable();

    }

    void addCollision(Entrie base,Entrie item){
        Entrie t;
        t = base;
        while(t.next != null){t = t.next;}
        t.next = item;
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
        int bucket =  hashKey(key,capacity);
        if(entries[bucket].set){
            e = entries[bucket];
            while(e != null){
                if(e.key.equals(key))return e.value;
                e = e.next;
            }
        }
        return null;
    }

    public boolean bucketIsSet(String key){
        int bucket =  hashKey(key,capacity);
        return entries[bucket].set;
    }


}
