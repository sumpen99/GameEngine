package helper.struct;
import helper.enums.IntListField;
import java.util.Arrays;
import static helper.enums.QuadGlobal.IL_FIXED_CAP;

//user dragon-energy stackoverflow
//https://stackoverflow.com/questions/41946007/efficient-and-well-explained-implementation-of-a-quadtree-for-2d-collision-det
public class IntList {
    public final int IL_FIXED = IL_FIXED_CAP.getValue();
    public int[] fixed = new int [IL_FIXED];
    public int[] data;
    public int numFields,num,cap,freeElement;

    public void create(IntListField field){
        data = fixed;
        num = 0;
        cap = IL_FIXED;
        numFields = field.getValue();
        freeElement = -1;
    }

    public void destroy(){
        // Free the buffer only if it was heap allocated.
        //data = null;
        if(data != fixed)data = null;
            //free(il->data);
    }

    void clear(){
        num = 0;
        freeElement = -1;
    }

    public int size(){
        return num;
    }

    public int get(int n, IntListField field){
        assert(n >= 0 && n < num);
        return data[n*numFields + field.getValue()];
    }

    void set(int n,IntListField field, int val){
        assert(n >= 0 && n < num);
        data[n*numFields + field.getValue()] = val;
    }

    int pushBack(){
        final int new_pos = (num+1) * numFields;
        // If the list is full, we need to reallocate the buffer to make room
        // for the new element.
        if (new_pos > cap) {
            // Use double the size for the new capacity.
            final int new_cap = new_pos * 2;

            // If we're pointing to the fixed buffer, allocate a new array on the
            // heap and copy the fixed buffer contents to it.
            if (cap == IL_FIXED) {
                data = new int[new_cap];
                System.arraycopy(fixed,0,data,0,fixed.length);
                //memcpy(il->data, il->fixed, sizeof(il->fixed));
            }
            else {
                // Otherwise reallocate the heap buffer to the new size.
                data = Arrays.copyOf(data,new_cap);
                //data = realloc(il->data, new_cap * sizeof(*il->data));
            }
            // Set the old capacity to the new capacity.
            cap = new_cap;
        }
        return num++;
    }

    void popBack(){
        // Just decrement the list size.
        assert(num > 0);
        --num;
    }

    int insert(){
        // If there's a free index in the free list, pop that and use it.
        if (freeElement != -1) {
            final int index = freeElement;
            final int pos = index * numFields;
            // Set the free index to the next free index.
            freeElement = data[pos];
          // Return the free index.
            return index;
        }
        // Otherwise insert to the back of the array.
        return pushBack();
    }

    void erase(int n){
        // Push the element to the free list.
        final int pos = n * numFields;
        data[pos] = freeElement;
        freeElement = n;
    }

}
