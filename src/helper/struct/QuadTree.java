package helper.struct;
import helper.enums.Color;
import java.util.Arrays;
import static helper.enums.IntListField.*;
import static helper.enums.QuadGlobal.QUAD_HEIGHT;
import static helper.enums.QuadGlobal.QUAD_WIDTH;
import static helper.methods.CommonMethods.intersect;

//user dragon-energy stackoverflow
//https://stackoverflow.com/questions/41946007/efficient-and-well-explained-implementation-of-a-quadtree-for-2d-collision-det

public class QuadTree {
    final Color[] colors = Color.values();
    public IntList nodes,elts,enodes;
    public int objCount;
    int rootMx,rootMy,rootSx,rootSy;
    int maxElements;
    int maxDepth;
    int[] temp;
    int tempSize;
    public QuadTree(int objcnt){
        objCount = objcnt;
        nodes = new IntList();
        elts = new IntList();
        enodes = new IntList();
        initSelf();
    }

    void initSelf(){
        QuadObj qObj = new QuadObj();
        create(QUAD_WIDTH.getValue(),QUAD_HEIGHT.getValue()-1,4,8);
        for(int i = 0;i<objCount;i++){
            double val = Math.random()*1000;
            qObj.setValues(colors[(int)val%colors.length].getValue());
            insert(i,qObj.vPos.x,qObj.vPos.y,qObj.vPos.x+qObj.vSize.x,qObj.vPos.y+qObj.vSize.y,qObj.color);
        }
    }

    void create(int width, int height, int max_elements, int max_depth){
        maxElements = max_elements;
        maxDepth = max_depth;
        temp = new int[0];
        tempSize = 0;
        nodes.create(NODE_NUM);
        elts.create(ELT_NUM);
        enodes.create(ENODE_NUM);

        // Insert the root node to the qt.
        nodes.insert();
        nodes.set(0,ND_IDX_FC,-1);
        nodes.set(0, NODE_IDX_NUM, 0);

        // Set the extents of the root node.
        rootMx = width >> 1;
        rootMy = height >> 1;
        rootSx = rootMx;
        rootSy = rootMy;
    }

    void destroy(){
        nodes.destroy();
        elts.destroy();
        enodes.destroy();
        temp = null;
        //free(qt->temp);
    }

    void nodeInsert(int index, int depth, int mx, int my, int sx, int sy, int element){
        // Find the leaves and insert the element to all the leaves found.
        int j = 0;
        IntList leaves = new IntList();

        final int lft = elts.get(element, ELT_IDX_LFT);
        final int top = elts.get(element, ELT_IDX_TOP);
        final int rgt = elts.get(element, ELT_IDX_RGT);
        final int btm = elts.get(element, ELT_IDX_BTM);

        leaves.create(ND_NUM);
        findLeaves(leaves,index, depth, mx, my, sx, sy, lft, top, rgt, btm);
        for (j=0; j < leaves.size(); ++j) {
            final int nd_mx = leaves.get(j, ND_IDX_MX);
            final int nd_my = leaves.get(j, ND_IDX_MY);
            final int nd_sx = leaves.get(j, ND_IDX_SX);
            final int nd_sy = leaves.get(j, ND_IDX_SY);
            final int nd_index = leaves.get(j, ND_IDX_INDEX);
            final int nd_depth = leaves.get(j, ND_IDX_DEPTH);
            leafInsert(nd_index, nd_depth, nd_mx, nd_my, nd_sx, nd_sy, element);
        }
        leaves.destroy();
    }

    void findLeaves(IntList out,int node, int depth, int mx, int my, int sx, int sy,int lft, int top, int rgt, int btm){
        IntList to_process = new IntList();
        to_process.create(ND_NUM);
        pushNode(to_process, node, depth, mx, my, sx, sy);

        while (to_process.size() > 0){
            final int back_idx = to_process.size() - 1;
            final int nd_mx = to_process.get(back_idx, ND_IDX_MX);
            final int nd_my = to_process.get(back_idx, ND_IDX_MY);
            final int nd_sx = to_process.get(back_idx, ND_IDX_SX);
            final int nd_sy = to_process.get(back_idx, ND_IDX_SY);
            final int nd_index = to_process.get(back_idx, ND_IDX_INDEX);
            final int nd_depth = to_process.get(back_idx, ND_IDX_DEPTH);
            to_process.popBack();

            // If this node is a leaf, insert it to the list.
            if (nodes.get(nd_index, NODE_IDX_NUM) != -1){
                pushNode(out, nd_index, nd_depth, nd_mx, nd_my, nd_sx, nd_sy);
            }
            else{
                // Otherwise push the children that intersect the rectangle.
                final int fc = nodes.get(nd_index, ND_IDX_FC);
                final int hx = nd_sx >> 1, hy = nd_sy >> 1;
                final int l = nd_mx-hx, t = nd_my-hy, r = nd_mx+hx, b = nd_my+hy;

                if (top <= nd_my) {
                    if (lft <= nd_mx){
                        pushNode(to_process, fc+0, nd_depth+1, l,t,hx,hy);
                    }
                    if (rgt > nd_mx){
                        pushNode(to_process, fc+1, nd_depth+1, r,t,hx,hy);
                    }
                }
                if (btm > nd_my) {
                    if (lft <= nd_mx){
                        pushNode(to_process, fc+2, nd_depth+1, l,b,hx,hy);
                    }
                    if (rgt > nd_mx){
                        pushNode(to_process, fc+3, nd_depth+1, r,b,hx,hy);
                    }
                }
            }
        }
        to_process.destroy();
    }

    void leafInsert(int node, int depth, int mx, int my, int sx, int sy, int element){
        // Insert the element node to the leaf.
        final int nd_fc = nodes.get(node, ND_IDX_FC);
        nodes.set(node, ND_IDX_FC, enodes.insert());
        enodes.set(nodes.get(node, ND_IDX_FC), ENODE_IDX_NEXT, nd_fc);
        enodes.set(nodes.get(node, ND_IDX_FC), ENODE_IDX_ELT, element);

        // If the leaf is full, split it.
        if (nodes.get(node, NODE_IDX_NUM) == maxElements && depth < maxDepth){
            int fc = 0, j = 0;
            IntList elts = new IntList();
            elts.create(ENODE_IDX_ELT);

            // Transfer elements from the leaf node to a list of elements.
            while (nodes.get(node, ND_IDX_FC) != -1) {
                final int index = nodes.get(node, ND_IDX_FC);
                final int next_index = enodes.get(index, ENODE_IDX_NEXT);
                final int elt = enodes.get(index, ENODE_IDX_ELT);

                // Pop off the element node from the leaf and remove it from the qt.
                nodes.set(node, ND_IDX_FC, next_index);
                enodes.erase(index);

                // Insert element to the list.
                elts.set(elts.pushBack(), ENODE_IDX_NEXT, elt);
            }

            // Start by allocating 4 child nodes.
            fc = nodes.insert();
            nodes.insert();
            nodes.insert();
            nodes.insert();
            nodes.set(node, ND_IDX_FC, fc);

            // Initialize the new child nodes.
            for (j=0; j < 4; ++j) {
                nodes.set(fc+j, ND_IDX_FC, -1);
                nodes.set(fc+j, NODE_IDX_NUM, 0);
            }

            // Transfer the elements in the former leaf node to its new children.
            nodes.set(node, NODE_IDX_NUM, -1);
            for (j=0; j < elts.size(); ++j){
                nodeInsert(node, depth, mx, my, sx, sy, elts.get(j, ENODE_IDX_NEXT));
            }
            elts.destroy();
        }
        else {
            // Increment the leaf element count.
            nodes.set(node, NODE_IDX_NUM, nodes.get(node, NODE_IDX_NUM) + 1);
        }
    }

    void pushNode(IntList nodes, int nd_index, int nd_depth, int nd_mx, int nd_my, int nd_sx, int nd_sy){
        final int back_idx = nodes.pushBack();
        nodes.set(back_idx, ND_IDX_MX, nd_mx);
        nodes.set(back_idx, ND_IDX_MY, nd_my);
        nodes.set(back_idx, ND_IDX_SX, nd_sx);
        nodes.set(back_idx, ND_IDX_SY, nd_sy);
        nodes.set(back_idx, ND_IDX_INDEX, nd_index);
        nodes.set(back_idx, ND_IDX_DEPTH, nd_depth);
    }

    int insert(int id, float x1, float y1, float x2, float y2,int color){
        // Insert a new element.
        final int new_element = elts.insert();

        // Set the fields of the new element.
        elts.set(new_element, ELT_IDX_LFT, (int)x1);
        elts.set(new_element, ELT_IDX_TOP, (int)y1);
        elts.set(new_element, ELT_IDX_RGT, (int)x2);
        elts.set(new_element, ELT_IDX_BTM, (int)y2);
        elts.set(new_element, ELT_IDX_ID, id);
        elts.set(new_element, ELT_IDX_CLR, color);

        // Insert the element to the appropriate leaf node(s).
        nodeInsert(0, 0,rootMx,rootMy,rootSx,rootSy,new_element);
        return new_element;
    }

    public void remove(int element){
        // Find the leaves.
        int j = 0;
        IntList leaves = new IntList();

        final int lft = elts.get(element, ELT_IDX_LFT);
        final int top = elts.get(element, ELT_IDX_TOP);
        final int rgt = elts.get(element, ELT_IDX_RGT);
        final int btm = elts.get(element, ELT_IDX_BTM);

        leaves.create(ND_NUM);
        findLeaves(leaves,0, 0,rootMx,rootMy,rootSx,rootSy, lft, top, rgt, btm);

        // For each leaf node, remove the element node.
        for (j=0; j < leaves.size(); ++j) {
            final int nd_index = leaves.get(j, ND_IDX_INDEX);

            // Walk the list until we find the element node.
            int node_index = nodes.get(nd_index, ND_IDX_FC);
            int prev_index = -1;
            while (node_index != -1 && enodes.get(node_index, ENODE_IDX_ELT) != element) {
                prev_index = node_index;
                node_index = enodes.get(node_index, ENODE_IDX_NEXT);
            }

            if (node_index != -1) {
                // Remove the element node.
                final int next_index = enodes.get(node_index, ENODE_IDX_NEXT);
                if (prev_index == -1) {
                    nodes.set(nd_index, ND_IDX_FC, next_index);
                }
                else {
                    enodes.set(prev_index, ENODE_IDX_NEXT, next_index);
                    enodes.erase(node_index);

                    // Decrement the leaf element count.
                    nodes.set(nd_index, NODE_IDX_NUM, nodes.get(nd_index, NODE_IDX_NUM) - 1);
                }
            }
        }
        leaves.destroy();

        // Remove the element.
        elts.erase(element);
    }

    public void query(IntList out, float x1, float y1, float x2, float y2){
        // Find the leaves that intersect the specified query rectangle.
        int j = 0;
        IntList leaves = new IntList();
        final int elt_cap = elts.size();
        final int qlft = (int)x1;
        final int qtop = (int)y1;
        final int qrgt = (int)x2;
        final int qbtm = (int)y2;

        if(tempSize < elt_cap) {
            tempSize = elt_cap;
            temp = Arrays.copyOf(temp,tempSize);
            //qt->temp = realloc(qt->temp, qt->temp_size * sizeof(*qt->temp));
            Arrays.fill(temp,0);
            //memset(qt->temp, 0, qt->temp_size * sizeof(*qt->temp));
        }

        // For each leaf node, look for elements that intersect.
        leaves.create(ND_NUM);
        findLeaves(leaves,0, 0,rootMx,rootMy,rootSx,rootSy, qlft, qtop, qrgt, qbtm);

        out.clear();
        for (j=0; j < leaves.size();++j){
            final int nd_index = leaves.get(j, ND_IDX_INDEX);

            // Walk the list and add elements that intersect.
            int elt_node_index = nodes.get(nd_index, ND_IDX_FC);
            while (elt_node_index != -1) {
                final int element = enodes.get(elt_node_index, ENODE_IDX_ELT);
                final int lft = elts.get(element, ELT_IDX_LFT);
                final int top = elts.get(element, ELT_IDX_TOP);
                final int rgt = elts.get(element, ELT_IDX_RGT);
                final int btm = elts.get(element, ELT_IDX_BTM);

                if(temp[element] == 0 && intersect(qlft,qtop,qrgt,qbtm, lft,top,rgt,btm)) {
                    out.set(out.pushBack(), ENODE_IDX_NEXT, element);
                    temp[element] = 1;
                }
                elt_node_index = enodes.get(elt_node_index, ENODE_IDX_NEXT);
            }
        }
        leaves.destroy();

        // Unmark the elements that were inserted.
        for (j=0; j < out.size(); ++j){
            temp[out.get(j, ENODE_IDX_NEXT)] = 0;
        }
    }

    void cleanup(){
        IntList to_process = new IntList();
        to_process.create(ENODE_IDX_ELT);

        // Only process the root if it's not a leaf.
        if (nodes.get(0, NODE_IDX_NUM) == -1){
            // Push the root index to the stack.
            to_process.set(to_process.pushBack(), ENODE_IDX_NEXT, 0);
        }

        while (to_process.size() > 0){
            // Pop a node from the stack.
            final int node = to_process.get(to_process.size()-1,ENODE_IDX_NEXT);
            final int fc = nodes.get(node, ND_IDX_FC);
            int num_empty_leaves = 0;
            int j = 0;
            to_process.popBack();

            // Loop through the children.
            for (j=0; j < 4; ++j) {
                final int child = fc + j;

                // Increment empty leaf count if the child is an empty
                // leaf. Otherwise if the child is a branch, add it to
                // the stack to be processed in the next iteration.
                if (nodes.get(child, NODE_IDX_NUM) == 0){
                    ++num_empty_leaves;
                }
                else if (nodes.get(child, NODE_IDX_NUM) == -1) {
                        // Push the child index to the stack.
                        to_process.set(to_process.pushBack(), ENODE_IDX_NEXT, child);
                    }
            }

            // If all the children were empty leaves, remove them and
            // make this node the new empty leaf.
            if (num_empty_leaves == 4) {
                // Remove all 4 children in reverse order so that they
                // can be reclaimed on subsequent insertions in proper
                // order.
                nodes.erase(fc + 3);
                nodes.erase(fc + 2);
                nodes.erase(fc + 1);
                nodes.erase(fc);

                // Make this node the new empty leaf.
                nodes.set(node, ND_IDX_FC, -1);
                nodes.set(node, NODE_IDX_NUM, 0);
            }
        }
        to_process.destroy();
    }

    /*void traverse(void *user_data, QtNodeFunc *branch, QtNodeFunc *leaf){
        IntList to_process = new IntList();
        to_process.create(ND_NUM);
        pushNode(to_process, 0, 0,rootMx,rootMy,rootSx,rootSy);

        while (to_process.size() > 0) {
            final int back_idx = to_process.size() - 1;
            final int nd_mx = to_process.get(back_idx, ND_IDX_MX);
            final int nd_my = to_process.get(back_idx, ND_IDX_MY);
            final int nd_sx = to_process.get(back_idx, ND_IDX_SX);
            final int nd_sy = to_process.get(back_idx, ND_IDX_SY);
            final int nd_index = to_process.get(back_idx, ND_IDX_INDEX);
            final int nd_depth = to_process.get(back_idx, ND_IDX_DEPTH);
            final int fc = to_process.get(nd_index, ND_IDX_FC);
            to_process.popBack();

            if (nodes.get(nd_index, NODE_IDX_NUM) == -1){
                // Push the children of the branch to the stack.
                final int hx = nd_sx >> 1, hy = nd_sy >> 1;
                final int l = nd_mx-hx, t = nd_my-hy, r = nd_mx+hx, b = nd_my+hy;
                pushNode(to_process, fc+0, nd_depth+1, l,t, hx,hy);
                pushNode(to_process, fc+1, nd_depth+1, r,t, hx,hy);
                pushNode(to_process, fc+2, nd_depth+1, l,b, hx,hy);
                pushNode(to_process, fc+3, nd_depth+1, r,b, hx,hy);
                if (branch){
                    branch(qt, user_data, nd_index, nd_depth, nd_mx, nd_my, nd_sx, nd_sy);
                }
            }
            else if (leaf){
                leaf(qt, user_data, nd_index, nd_depth, nd_mx, nd_my, nd_sx, nd_sy);
                }
        }
        to_process.destroy();
    }*/

}
