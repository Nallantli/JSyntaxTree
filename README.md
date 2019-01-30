# JSyntaxTree

![Output](/output_color.png)

A small Java program to build syntax trees and morphology trees, according to how my current morphology class prescribes.

Options:

`-i FILENAME`	input file

OPTIONAL:

`-o FILENAME`	output file (include the .png or .jpg)

`-f FONTNAME`	font

`-fs FONTSIZE`	fontsize

`-l STROKE` 	stroke size (for the lines)

`-sx SPACE`		spacing between adjacent nodes horizontally

`-sy SPACE`		spacing between adjacent nodes vertically

`-c`			DO IT IN COLOR

Syntax:

Generally just bracket notation, with some text effects:

`\n` newline

`_WORDS_` subscript

`*WORDS*` bold

`%WORDS%` italic

`$WORDS$` smaller font

`\`WORDS\`` underline

Of course, all combinations are acceptable.

(e.g. `$*%_W_O_R_D_S_%*$`)

The program uses the Doulos SIL font (https://software.sil.org/doulos/), so you should probably have that installed. Not entirely sure what Java defaults to.

Will build web interface at some point?