cd "../"

java -jar JSyntaxTree.jar -i Examples/cliche.txt -o Examples/cliche.png -c -q
java -jar JSyntaxTree.jar -i Examples/danube.txt -o Examples/danube.png -q
java -jar JSyntaxTree.jar -i Examples/troiae_ab_oris.txt -o Examples/troiae_ab_oris.png -q
java -jar JSyntaxTree.jar -i Examples/carnie.txt -o Examples/carnie.png -q
java -jar JSyntaxTree.jar -i Examples/jpn.txt -o Examples/jpn.png -f "Unifont" -q -c
java -jar JSyntaxTree.jar -i Examples/chomsky.txt -o Examples/chomsky.png -q -a