package helper.tree;
import helper.io.IOHandler;
import helper.struct.Node;


public class BinarySearchTree {
    public Node root;

    Node getNewNode(int key){
        Node n = new Node();
        n.key = key;
        n.count = 1;
        n.left = null;
        n.right = null;
        return n;
    }

    Node insertNewNode(Node node,int key){
        if(node == null)return getNewNode(key);
        if(key == node.key){node.count++;}
        else if(key< node.key){node.left = insertNewNode(node.left,key);}
        else{node.right = insertNewNode(node.right,key);}
        return node;
    }

    public void insert(int key){
        if(root == null)root = getNewNode(key);
        else{insertNewNode(root,key);}
    }

    public void printBinaryTree(Node node,boolean isRoot,boolean isLeft,boolean isRight){
        if(node != null){
            printBinaryTree(node.left,false,true,false);
            IOHandler.printString("Node Key: %d numberOfInserts=%d isRoot: %b isleft: %b isRight: %b)".formatted(node.key,node.count,isRoot,isLeft,isRight));
            printBinaryTree(node.right,false,false,true);
        }
    }
}
