@echo off
set /p fn="Input Filename: "
set /p of="Output Filename: "
java -jar JSyntaxTree.jar -i %fn% -o %of%