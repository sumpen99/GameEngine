package helper.tree;
import helper.io.IOHandler;
import helper.struct.KDNode;
import helper.struct.Node;

import java.util.Arrays;
import java.util.UUID;
import static helper.methods.CommonMethods.distanceSquaredKDNod;
import static helper.methods.CommonMethods.getRandomFloat;
import static helper.sort.QuickSort.sortKDNodes;


public class KDTree {
    private KDNode[] nodeList;
    public KDNode testNode;
    public KDNode root;
    float MIN_DISTANCE = 20.0f;

    public void setTestNodes(){
        nodeList = getTestNodes();
        testNode = nodeList[nodeList.length-1];
    }

    public void setTreeStructure(int k){
        root = buildKDTree(nodeList,0,k);
    }

    public KDNode search(KDNode key,int k){
        KDNode found = traverseTree(root,key,0,k);
        IOHandler.printString("Searched For -> ");
        IOHandler.printKDNode(key);
        IOHandler.printString("Found -> ");
        IOHandler.printKDNode(found);
        return found;
    }

    public KDNode[] getTestNodes(){
        int size = 10,pow=1000,i=0;
        KDNode[] nodes = new KDNode[size];
        while(i<size){
            String nodeID = UUID.randomUUID().toString();
            nodes[i] = new KDNode(getRandomFloat(pow),getRandomFloat(pow),nodeID);
            i++;
        }
        return nodes;
    }

    public double distSqrt(KDNode n1,KDNode n2){
        return distanceSquaredKDNod(n1,n2);
    }

    public KDNode checkDistance(KDNode pivot,KDNode p1,KDNode p2){
        if(p1 == null){return p2;}
        if(p2 == null){return p1;}

        if(p1.distance < MIN_DISTANCE){return p1;}
        if(p2.distance < MIN_DISTANCE){return p2;}

        p1.distance = distSqrt(pivot,p1);
        p2.distance = distSqrt(pivot,p2);

        if(p1.distance<p2.distance){return p1;}
        return p2;
    }

    public KDNode buildKDTree(KDNode[]points,int depth,int k){
        int n = points.length;
        if(n==0){return null;}

        int axis = depth%k;
        sortKDNodes(points,0,points.length-1,axis);
        int floorDiv = Math.floorDiv(n,2);
        KDNode node = points[floorDiv];
        node.left = buildKDTree(Arrays.copyOfRange(points,0,floorDiv),depth+1,k);
        node.right = buildKDTree(Arrays.copyOfRange(points,floorDiv+1,points.length),depth+1,k);
        return node;
    }

    public KDNode traverseTree(KDNode root,KDNode pivot,int depth,int k){
        if(root == null){return null;}
        if(root.distance<MIN_DISTANCE){return root;}

        int axis = depth%k;

        KDNode nextBranch;
        KDNode oppositeBranch;

        if(pivot.compare(axis)<root.compare(axis)){
            nextBranch = root.left;
            oppositeBranch = root.right;
        }
        else{
            nextBranch = root.right;
            oppositeBranch = root.left;
        }

        KDNode best = checkDistance(pivot,traverseTree(nextBranch,pivot,depth+1,k),root);
        if(distSqrt(pivot,best) > Math.pow(pivot.compare(axis) - root.compare(axis),2)){
            best = checkDistance(pivot,traverseTree(oppositeBranch,pivot,depth+1,k),best);
        }
        return best;
    }

    public void printInOrder(KDNode node){
        if(node != null){
            printInOrder(node.left);
            IOHandler.printKDNode(node);
            printInOrder(node.right);
        }
    }



}
