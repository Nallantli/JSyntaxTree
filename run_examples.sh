rm -rf Examples-RESULTS

for i in Examples/*.txt; do
    java -jar bin/JSyntaxTree.jar -i $i -q
done

for i in Examples/*.uni; do
    java -jar bin/JSyntaxTree.jar -i $i -q -f "Unifont"
done

mkdir Examples-RESULTS
mv Examples/*.png Examples-RESULTS/