# JSyntaxTree

![Output](/output_image.png)

A small Java program to build syntax trees and morphology trees, according to how my current morphology class prescribes.

First command argument is filename for syntax tree data.

e.g. `java Main "test.txt"`

Syntax:

Generally just bracket notation, with some text effects:

`\n` newline

`_WORDS_` subscript

`*WORDS*` bold

`%WORDS%` italic

`$WORDS$` smaller font

`\`WORDS\`` underline

Of course, all combinations are acceptable.

The program uses the Doulos SIL font (https://software.sil.org/doulos/), so you should probably have that installed. Not entirely sure what Java defaults to.

Will build web interface at some point?