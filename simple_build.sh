#!/bin/bash

java -jar antlr-4.13.2-complete.jar grammar/delphi.g4 -visitor -o gen

javac -cp .:antlr-4.13.2-complete.jar gen/grammar/*.java

#java -cp .:antlr-4.13.2-complete.jar:gen org.antlr.v4.gui.TestRig delphi program -gui
