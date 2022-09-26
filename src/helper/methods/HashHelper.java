package helper.methods;

public class HashHelper {
    public static final long FNV_OFFSET_32 = 2166136261L;
    public static final int UTF8 = 255;

    public static int hashKey(String key,int capacity){
        return Math.abs((int)fnv1a(key)) % capacity;
    }

    public static long fnv1a(String key){
        long hash = FNV_OFFSET_32;
        for(int i = 0;i<key.length();i++) {
            hash ^= (int)key.charAt(i);
            hash += (hash <<1) + (hash <<4) + (hash <<7) + (hash <<8) + (hash <<24);
        }
        return hash;
    }

    public static boolean decodeUriComponent(String str){
        int i = 0,count = 0;
        while(i < str.length()){
            count+=utfCompatible(str.charAt(i++),UTF8);
        }
        return count == str.length();
    }

    public static int utfCompatible(char c,int max_size){
        return (int)c < max_size ? 1 : 0;
    }
}
