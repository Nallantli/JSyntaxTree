mkdir "bin"
javac SRC/*.java -d bin/
cd "bin/"
jar -cvfe "JSyntaxTree.jar" JSyntaxTree *.class
jar -cvfe "JSyntaxTreeGUI.jar" GUI *.class
move "JSyntaxTree.jar" "../"
move "JSyntaxTreeGUI.jar" "../"