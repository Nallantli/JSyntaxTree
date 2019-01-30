mkdir "bin"
javac SRC/*.java -d bin/
cd "bin/"
jar -cvfe "JSyntaxTree.jar" Main *.class
move "JSyntaxTree.jar" "../"