cd "../"

java -jar bin/JSyntaxTree.jar -i Examples/cliche.txt -q -c
java -jar bin/JSyntaxTree.jar -i Examples/danube.txt -q
java -jar bin/JSyntaxTree.jar -i Examples/troiae_ab_oris.txt -q
java -jar bin/JSyntaxTree.jar -i Examples/carnie.txt -q
java -jar bin/JSyntaxTree.jar -i Examples/jpn.txt -f "Unifont" -q -c
java -jar bin/JSyntaxTree.jar -i Examples/jpn2.txt -f "Unifont" -q
java -jar bin/JSyntaxTree.jar -i Examples/chomsky.txt -q -a
java -jar bin/JSyntaxTree.jar -i Examples/dante.txt -q