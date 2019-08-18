@echo off
rmdir Examples-RESULTS /s /q
cd bin

for %%i in (../Examples/*.txt) do (
	echo Generating %%i...
    .\JSyntaxTree.jar -i ../Examples/%%i -q
)

for %%i in (../Examples/*.uni) do (
	echo Generating %%i...
    .\JSyntaxTree.jar -i ../Examples/%%i -q -f "Unifont"
)

cd ..
mkdir Examples-RESULTS
cd bin
move ../Examples/*.png ../Examples-RESULTS
echo Done.