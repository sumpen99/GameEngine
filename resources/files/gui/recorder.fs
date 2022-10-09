# Implementation of Layout
# Use # For comments Window Width 800 Window Height 500
# SET OPACITY TO 0 INSIDE LAYOUT TO NOT DRAW
# id:string
# bind:(idToWidgetToBindTo)
# args:(idToWidgetToBindTo),(idToWidgetToBindTo),...
VERTICAL
;
BoxLayout
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
        color:PALEGOLDENROD
        textcolor:black
        func:shutdown
        draw:fill
        text:Exit
        shape:rectangle
;
    MoveableButton
        left:130
        top:5
        width:260
        height:50
        color:PALEGOLDENROD
        textcolor:black
        draw:fill
        text:|||
        opacity:0
        shape:rectangle
;
BoxLayout
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
    left:0
    top:0
    width:800
    height:40
    valign:false
    color:LightBlue
    draw:fill
    opacity:0
    shape:rectangle
;
    FlatTextbox
        id:boxpath
        left:10
        top:5
        width:400
        height:30
        color:WHITESMOKE
        textcolor:Black
        draw:fill
        shape:rectangle
        hintText:filename (ex soundClip-2.wav)
        opacity:1
        enableAutoCorrect:false
;
    RoundedButton
        id:readfile
        left:70
        top:20
        radiex:50
        radiey:18
        color:PALEGOLDENROD
        textcolor:Black
        args:wavebox,boxpath
        func:readwavefile
        draw:fill
        text:Read
        shape:rectangle
;
    FlatLabel
        left:20
        top:0
        width:150
        height:40
        color:seashell
        textcolor:Black
        draw:fill
        opacity:0
        text:Draw Info?
        shape:rectangle
;
    CheckBox
        left:10
        top:0
        width:40
        height:40
        bind:wavebox
        color:WHITESMOKE
        draw:fill
        shape:rectangle
;
BoxLayout
    left:0
    top:0
    width:800
    height:250
    valign:false
    color:LightGreen
    draw:fill
    opacity:0
    shape:rectangle
;
    WaveViewBox
        id:wavebox
        left:0
        top:0
        width:800
        height:250
        col:1
        row:18
        color:LightGreen
        textcolor:Black
        draw:fill
        opacity:1
        text:HERE WE ARE GOING TO OUTPUT SOUNDWAVES DURING RECORDING
        shape:rectangle
;
