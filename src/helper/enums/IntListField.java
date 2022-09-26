package helper.enums;

public enum IntListField {
    // ----------------------------------------------------------------------------------------
    // Element node fields:
    // ----------------------------------------------------------------------------------------
    ENODE_NUM(2),
    // Points to the next element in the leaf node. A value of -1
    // indicates the end of the list.
    ENODE_IDX_NEXT(0),
    // Stores the element index.
    ENODE_IDX_ELT(1),
    // ----------------------------------------------------------------------------------------
    // Element fields:
    // ----------------------------------------------------------------------------------------
    ELT_NUM(6),
    // Stores the rectangle encompassing the element.
    ELT_IDX_LFT(0),ELT_IDX_TOP(1),ELT_IDX_RGT(2),ELT_IDX_BTM(3),
    // Stores the ID of the element.
    ELT_IDX_ID(4),
    // Stores the COLOR of the element.
    ELT_IDX_CLR(5),
    // ----------------------------------------------------------------------------------------
    // Node fields:
    // ----------------------------------------------------------------------------------------
    NODE_NUM(2),
    // Points to the first child if this node is a branch or the first element
    // if this node is a leaf.
    ND_IDX_FC(0),
    // Stores the number of elements in the node or -1 if it is not a leaf.
    NODE_IDX_NUM(1),
    // ----------------------------------------------------------------------------------------
    // Node data fields:
    // ----------------------------------------------------------------------------------------
    ND_NUM(6),
    // Stores the extents of the node using a centered rectangle and half-size.
    ND_IDX_MX(0),ND_IDX_MY(1),ND_IDX_SX(2),ND_IDX_SY(3),
    // Stores the index of the node.
    ND_IDX_INDEX(4),
    // Stores the depth of the node.
    ND_IDX_DEPTH(5);
    private final int value;
    IntListField(int value){this.value = value;}
    public int getValue(){return this.value;}
}
