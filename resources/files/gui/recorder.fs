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
    height:60
    valign:false
    color:LightGreen
    draw:fill
    opacity:0
    shape:rectangle
;
    RoundedButton
        left:80
        top:30
        radiex:60
        radiey:25
        color:PLUM
        textcolor:black
        func:shutdown
        draw:fill
        text:Exit
        shape:rectangle
;
BoxLayout
    id:layout1
    left:350
    top:0
    width:100
    height:100
    valign:false
    color:LightGreen
    draw:fill
    opacity:0
    shape:rectangle
;
    Recorder
        id:soundrecorder
        left:0
        top:0
        width:100
        height:100
        color:Plum
        opacity:1
        path:rec.png
        func:recordaudio
        draw:fill
;
BoxLayout
    id:layout2
    left:0
    top:40
    width:800
    height:300
    valign:false
    color:LightGreen
    draw:fill
    opacity:1
    shape:rectangle
;



