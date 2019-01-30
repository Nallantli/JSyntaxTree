mkdir "bin"
javac SRC/*.java -d bin/
cd "bin/"
jar -cvfe "JSyntaxTree.jar" JSyntaxTree *.class
move "JSyntaxTree.jar" "../"