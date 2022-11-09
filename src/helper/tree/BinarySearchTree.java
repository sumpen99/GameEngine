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

    public void inOrderTraversal(Node node,boolean isRoot,boolean isLeft,boolean isRight){
        if(node != null){
            inOrderTraversal(node.left,false,true,false);
            IOHandler.printString("Node Key: %d numberOfInserts=%d isRoot: %b isleft: %b isRight: %b)".formatted(node.key,node.count,isRoot,isLeft,isRight));
            inOrderTraversal(node.right,false,false,true);
        }
    }

    Node minValueNode(Node node){
        Node current = node;
        while(current != null && current.left != null){
            current = current.left;
        }
        return current;
    }

    Node deleteNode(Node node,int key){
        if(node == null)return null;

        if(key<node.key){
            node.left = deleteNode(node.left,key);
        }
        else if(key>node.key){
            node.right = deleteNode(node.right,key);
        }

        else{
            Node temp;
            if(node.left == null){
                temp = node.right;
                node = null;
                return temp;
            }
            else if(node.right == null){
                temp = node.left;
                node = null;
                return temp;
            }
            temp = minValueNode(node);
            node.key = temp.key;
            node.right = deleteNode(node.right,temp.key);
        }

        return node;
    }

    public void delete(int key){
        if(root == null)return;
        deleteNode(root,key);
    }
}
