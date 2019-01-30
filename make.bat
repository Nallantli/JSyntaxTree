@echo off
echo Generating *.class files...
mkdir "bin"
javac SRC/*.java -d bin/
cd "bin/"
echo Generating JSyntaxTree.jar...
jar -cvfe "JSyntaxTree.jar" JSyntaxTree *.class
echo Generating JSyntaxTreeGUI.jar...
jar -cvfe "JSyntaxTreeGUI.jar" GUI *.class
echo Moving *.jar files to main index...
move "JSyntaxTree.jar" "../"
move "JSyntaxTreeGUI.jar" "../"
echo Done.