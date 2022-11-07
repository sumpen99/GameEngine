package helper.tree;
import helper.enums.Color;
import helper.io.IOHandler;
import helper.struct.Node;

import static helper.enums.Color.BLACK;
import static helper.enums.Color.RED;

//https://stackoverflow.com/questions/42800462/trying-to-implement-a-red-black-tree-in-c

public class RedBlackTree {
    private Node root;
    private Node TNULL;

    public RedBlackTree(){
        TNULL = new Node();
        TNULL.color = Color.BLACK;
        TNULL.left = null;
        TNULL.right = null;
        TNULL.parent = null;
        root = TNULL;
    }

    void rotateLeft(Node x){
        Node y = x.right;
        x.right = y.left;
        if(y.left != TNULL){y.left.parent = x;}
        y.parent = x.parent;
        if(x.parent == TNULL){root = y;}
        else if(x == x.parent.left){x.parent.left = y;}
        else{x.parent.right = y;}

        y.left = x;
        x.parent = y;
    }

    void rotateRight(Node y){
        Node x = y.left;
        y.left = x.right;
        if(x.right != TNULL){x.right.parent = y;}
        x.parent = y.parent;
        if(y.parent == TNULL){root = x;}
        else if(y == y.parent.right){y.parent.right = x;}
        else{y.parent.left = x;}

        x.right = y;
        y.parent = x;
    }

    void redBlackInsertFixup(Node New){
        Node temp;
        while(New.parent.color == RED) {
            if (New.parent.key == New.parent.parent.key) {
                temp = New.parent.parent.right;
                if (temp.color == RED) {
                    New.parent.color = BLACK;
                    temp.color = BLACK;
                    New.parent.parent.color = RED;
                    New = New.parent.parent;
                } else {
                    if (New.key == New.parent.right.key) {
                        New = New.parent;
                        rotateLeft(New);
                    }
                    New.parent.color = BLACK;
                    New.parent.parent.color = RED;
                    rotateRight(New.parent.parent);
                }
            } else {
                temp = New.parent.parent.left;
                if (temp.color == RED) {
                    New.parent.color = BLACK;
                    temp.color = BLACK;
                    New.parent.parent.color = RED;
                    New = New.parent.parent;
                } else {
                    if (New.key == New.parent.left.key) {
                        New = New.parent;
                        rotateRight(New);
                    }
                    New.parent.color = BLACK;
                    New.parent.parent.color = RED;
                    rotateLeft(New.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    public void searchTree(int key){
        Node temp = root;
        int cnt = 1;
        while(temp != TNULL) {
            if(temp.key == key){
                IOHandler.printString("Found Key: %d Inside Node %s %s [Searched Nodes: %d]".formatted(key,temp.key,temp.color == BLACK ? "BLACK" : "RED",cnt));
                return;
            }
            if(key < temp.key){temp = temp.left;}
            else{temp = temp.right;}
            cnt++;
        }
        IOHandler.printString("Could Not Find Key: %d Inside Tree".formatted(key));
    }

    private Node getNewNode(int key){
        Node n = new Node();
        n.key = key;
        n.color = Color.RED;
        n.parent = null;
        n.left = TNULL;
        n.right = TNULL;
        n.count = 1;
        return n;
    }

    public void redBlackInsert(int key) {
        Node z =  getNewNode(key);
        Node y =  TNULL;
        Node x = root;

        // Find where to Insert new node Z into the binary search tree
        while(x != TNULL) {
            y = x;
            if(x.key == key){x.count++;return;}
            else if(z.key < x.key){x = x.left;}
            else{x = x.right;}
        }

        z.parent = y;
        if(y == TNULL){root = z;}
        else if(z.key < y.key){y.left = z;}
        else{y.right = z;}

        // Init z as a red leaf
        z.left  = TNULL;
        z.right = TNULL;
        z.color = RED;

        // Ensure the Red-Black property is maintained
        redBlackInsertFixup(z);
    }

    int getTreeHeight(Node node) {
        int h = 0;
        if(node != null){
            if(node == TNULL)
                h = 1;
            else{
                int leftHeight  = getTreeHeight(node.left);
                int rightHeight = getTreeHeight(node.right);
                h = Math.max(leftHeight, rightHeight) + 1;
            }
        }
        return h;
    }

    int getBlackHeight(Node node) {
        int height = 0;
        while(node != null){
            if((node == TNULL) || (node.color == BLACK))
                height++;
            node = node.left;
        }
        return height;
    }

    void printTree(Node node){
        if(node != null){
            printTree(node.left);
            IOHandler.printString("%d - %s(height=%d,blackheight=%d,numberOfInserts=%d)".formatted(node.key, (node.color == BLACK ? "BLACK" : "RED"), getTreeHeight(node), getBlackHeight(node),node.count));
            printTree(node.right);
        }
    }

    public void printRedBlackTree(){
        IOHandler.printString("Tree Height=%d, Black-Height=%d".formatted(getTreeHeight(root),getBlackHeight(root)));
        printTree(root);
    }

}
