package helper.tree;
import helper.io.IOHandler;
import helper.methods.CommonMethods;
import helper.struct.Node;

public class BinarySearchTree {
    public Node root;
    static int staticCount;

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

    public void inOrderTraversal(Node node,Node[] nodes){
        if(node != null){
            inOrderTraversal(node.left,nodes);
            nodes[staticCount++] = node;
            inOrderTraversal(node.right,nodes);
        }
    }

    public void printInOrder(Node node,boolean isRoot,boolean isLeft,boolean isRight){
        if(node != null){
            printInOrder(node.left,false,true,false);
            IOHandler.printString("Node Key: %d numberOfInserts=%d isRoot: %b isleft: %b isRight: %b)".formatted(node.key,node.count,isRoot,isLeft,isRight));
            printInOrder(node.right,false,false,true);
        }
    }

    public void printPreOrder(Node node,boolean isRoot,boolean isLeft,boolean isRight){
        if(node != null){
            IOHandler.printString("Node Key: %d numberOfInserts=%d isRoot: %b isleft: %b isRight: %b)".formatted(node.key,node.count,isRoot,isLeft,isRight));
            printInOrder(node.left,false,true,false);
            printInOrder(node.right,false,false,true);
        }
    }

    public void printPostOrder(Node node,boolean isRoot,boolean isLeft,boolean isRight){
        if(node != null){
            printInOrder(node.left,false,true,false);
            printInOrder(node.right,false,false,true);
            IOHandler.printString("Node Key: %d numberOfInserts=%d isRoot: %b isleft: %b isRight: %b)".formatted(node.key,node.count,isRoot,isLeft,isRight));
        }
    }

    public Node[] getSortedNodeList(){
        if(root == null){return null;}
        staticCount = 0;
        int size = countNumberOfNodes(root);
        Node[] nodes = new Node[size];
        inOrderTraversal(root,nodes);
        return nodes;
    }

    public Node searchForNode(int key){
        Node temp;
        temp = root;
        while(temp!= null){
            if(temp.key==key){return temp;}
            else if(key<temp.key){temp = temp.left;}
            else{temp=temp.right;}
        }
        return null;
    }

    Node minValueNode(Node node){
        Node current = node;
        while(current != null && current.left != null){
            current = current.left;
        }
        return current;
    }

    Node maxValueNode(Node node){
        Node current = node;
        while(current != null && current.right != null){
            current = current.right;
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

    Node buildBalancedTree(Node[] nodes,int start,int end){
        if(start>end){return null;}
        int mid = (start+end)/2;
        Node current = nodes[mid];

        current.left = buildBalancedTree(nodes,start,mid-1);
        current.right = buildBalancedTree(nodes,mid+1,end);
        return current;
    }

    void storeNodesToList(Node[] nodes,Node node){
        if(node == null){return;}
        storeNodesToList(nodes,node.left);
        nodes[staticCount++] = node;
        storeNodesToList(nodes,node.right);
    }

    public void balanceTree(){
        Node[] nodes = new Node[countNumberOfNodes(root)];
        staticCount = 0;
        storeNodesToList(nodes,root);
        //root = null;
        int n = nodes.length;
        Node newRoot;
        newRoot = buildBalancedTree(nodes,0,n-1);
        root = null;
        root = newRoot;
    }

    int countNumberOfNodes(Node node){
        if(node == null){return 0;}
        return 1 + countNumberOfNodes(node.left) + countNumberOfNodes(node.right);
    }

    int balancedHeight(Node node){
        if(node == null)return 0;
        int leftSubTreeHeight = balancedHeight(node.left);
        if(leftSubTreeHeight == -1){return -1;}
        int rightSubTreeHeight = balancedHeight(node.right);
        if(rightSubTreeHeight == -1){return -1;}

        if(Math.abs(leftSubTreeHeight-rightSubTreeHeight) > 1){return -1;}

        return (Math.max(leftSubTreeHeight,rightSubTreeHeight)+1);
    }

    public boolean isBalanced(){
        int r;
        String result = (r=balancedHeight(root)) == -1 ? "Tree Returned -1. Its Not Balanced Correctly." : "Tree Returned %d. Its OK.".formatted(r);
        IOHandler.printString(result);
        return r != -1;
    }

    public static void runTest(){
        int size = 10;
        BinarySearchTree tree = new BinarySearchTree();
        int toDelete = 0;
        for(int i = 0;i<size;i++){
            toDelete = CommonMethods.getRand(10000);
            tree.insert(toDelete);
        }
        tree.printInOrder(tree.root,true,false,false);
        IOHandler.printString("Value To Delete: %d".formatted(toDelete));
        tree.delete(toDelete);
        tree.printInOrder(tree.root,true,false,false);
        if(!tree.isBalanced()){
            IOHandler.printString("Balancing Tree..");
            tree.balanceTree();
            tree.isBalanced();
            tree.printInOrder(tree.root,true,false,false);
            Node temp = tree.searchForNode(toDelete);
            if(temp!= null){
                IOHandler.printString("Found Key: %d Sesrched For: %d".formatted(temp.key,toDelete));
            }
        }
    }
}
