# Implementation of Layout
# Use # For comments Window Width 800 Window Height 500
# SET OPACITY TO 0 INSIDE LAYOUT TO NOT DRAW
# id:string
# bind:(idToWidgetToBindTo)
# args:(idToWidgetToBindTo),(idToWidgetToBindTo),...
HORIZONTAL
;
BoxLayout
    id:layout0
    left:0
    top:0
    width:800
    height:500
    valign:false
    color:LightGreen
    draw:fill
    opacity:0
    shape:rectangle
;
FlatButton
        left:0
        top:0
        width:80
        height:40
        color:PLUM
        textcolor:black
        text:Exit
        func:shutdown
        draw:fill
        shape:rectangle
;