# Implementation of Layout
# Use # For comments Window Width 800 Window Height 500
# SET OPACITY TO 0 INSIDE LAYOUT TO NOT DRAW
# id:string
# bind:(idToWidgetToBindTo)
# args:(idToWidgetToBindTo),(idToWidgetToBindTo),...
VERTICAL
;
BoxLayout
    id:layout0
    left:0
    top:0
    width:800
    height:500
    valign:false
    color:White
    draw:fill
    opacity:0
    shape:rectangle
;
FlatButton
        left:0
        top:0
        width:800
        height:40
        color:PLUM
        textcolor:Black
        func:shutdown
        draw:fill
        text:Exit      Pan-LeftMouseDrag Zoom-Wheel Erase-RightMouseDrag
        opacity:0
        talign:false
        shape:rectangle
;
    QuadTree
        left:0
        top:10
        width:790
        height:445
        color:WHITE
        objcount:1000000
        draw:fill
        shape:rectangle
        opacity:0
;