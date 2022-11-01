package helper.tree;
import helper.io.IOHandler;
import helper.struct.Node;

import static helper.enums.Color.BLACK;
import static helper.enums.Color.RED;

public class RedBlackTree {
    public Node root;

    public RedBlackTree(){
        root = new Node(true);
    }

    void rotateLeft(Node x){
        Node y = x.right;
        x.right = y.left;
        if(!y.left.isTNil){y.left.parent = x;}
        y.parent = x.parent;
        if(!x.parent.isTNil){root = y;}
        else if(x == x.parent.left){x.parent.left = y;}
        else{x.parent.right = y;}

        y.left = x;
        x.parent = y;
    }

    void rotateRight(Node y){
        Node x = y.left;
        y.left = x.right;
        if(!x.right.isTNil){x.right.parent = y;}
        x.parent = y.parent;
        if(!y.parent.isTNil){root = x;}
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

    void redBlackInsert(int key) {
        Node z =  new Node(key);
        Node y =  new Node(true);
        Node x = root;

        // Find where to Insert new node Z into the binary search tree
        while (!x.isTNil) {
            y = x;
            if(z.key < x.key){x = x.left;}
            else{x = x.right;}
        }

        z.parent = y;
        if (y.isTNil){root = z;}
        else if(z.key < y.key){y.left = z;}
        else{y.right = z;}

        // Init z as a red leaf
        z.left  = new Node(true);
        z.right = new Node(true);
        z.color = RED;

        // Ensure the Red-Black property is maintained
        redBlackInsertFixup(z);
    }

    int getTreeHeight(Node Root) {
        int h = 0;
        if(Root != null){
            if(Root.isTNil)
                h = 1;
            else{
                int leftHeight  = getTreeHeight(Root.left);
                int rightHeight = getTreeHeight(Root.right);
                h = Math.max(leftHeight, rightHeight) + 1;
            }
        }

        return h;
    }


    int getBlackHeight(Node Root) {
        int height = 0;
        while(Root != null){
            if((Root.isTNil) || (Root.color == BLACK))
                height++;
            Root = Root.left;
        }
        return height;
    }

    void printTree(Node Root){
        if(Root != null){
            printTree(Root.left);
            IOHandler.printString("%d%s(height=%d,blackheight=%d)".formatted(Root.key, (Root.color == BLACK ? "BLACK" : "RED"), getTreeHeight(Root), getBlackHeight(Root)));
            printTree(Root.right);
        }
    }

    void printRedBlackTree(Node Root){
        IOHandler.printString("Tree Height=%d, Black-Height=%d".formatted(getTreeHeight(Root),getBlackHeight(Root)));
        printTree(Root);
    }

}
