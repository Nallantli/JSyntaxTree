@echo off
mkdir "temp"
javac src/main/syntax/*.java src/main/window/*.java -d temp/
cd "temp/"
echo Generating JSyntaxTree.jar...
jar -cvfe "../bin/JSyntaxTree.jar" syntax/JSyntaxTree syntax/*.class window/*.class
echo Generating JSyntaxTreeGUI.jar...
jar -cvfe "../bin/JSyntaxTreeGUI.jar" window/GUI syntax/*.class window/*.class
cd ".."
rmdir "temp" /s /q
echo Done. 
 