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
    height:150
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
        args:wavebox,recorderinfo
        path:rec.png
        func:recordaudio
        draw:fill
;
    FlatLabel
        id:recorderinfo
        left:0
        top:0
        width:100
        height:50
        color:seashell
        textcolor:Black
        draw:fill
        opacity:0
        text:Press To Record
        shape:rectangle
;
BoxLayout
    id:layout2
    left:0
    top:40
    width:800
    height:250
    valign:false
    color:LightGreen
    draw:fill
    opacity:1
    shape:rectangle
;
    FlatLabel
        id:wavebox
        left:0
        top:0
        width:800
        height:250
        color:Black
        textcolor:Black
        draw:fill
        opacity:0
        text:HERE WE ARE GOING TO OUTPUT SOUNDWAVES DURING RECORDING
        shape:rectangle



