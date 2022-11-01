package helper.struct;

import helper.enums.Color;

public class Node {
    public int key;
    public Color color;
    public Node left;
    public Node right;
    public Node parent;
    public boolean isTNil;

    public Node(int key){
        this.key = key;
        this.color = Color.RED;
    }

    public Node(boolean tNil){
        this.isTNil = tNil;
    }
}
