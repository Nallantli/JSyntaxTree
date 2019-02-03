# JSyntaxTree

![Output](/Examples/cliche.png)

[Download Here](https://github.com/Agilulfulus/JSyntaxTree/releases)

A small Java program to build syntax trees and morphology trees, according to how my current morphology class prescribes.

Inspired by [yohasebe](https://github.com/yohasebe)'s [RSyntaxTree](https://github.com/yohasebe/rsyntaxtree), which came into frequent use, however I unfortunately was pressed to make something new as the features required for my class surpassed the capabilities his program.

_[Legge qui per informazione italiana](DOCS/LEGGEMI.md)_

# CLI Options:

```
Syntax		Default		Desc
-i <STRING>	-		input file path (required)
-o <STRING>	OUTPUT.png	output file path (include the .png, .jpg, etc.)
-f <STRING>	Doulos SIL	font name
-fs <INT>	48		font size
-l <FLOAT>	3.0		stroke size (for the lines)
-sx <INT>	50		spacing between adjacent nodes horizontally
-sy <INT>	150		spacing between adjacent nodes vertically
-c		-		color the tree
-q		-		quits right after generation - no preview window
-b <INT>	50		adds padding around the tree
-a		-		auto-subscript - will add a numerical subscript to node types
```

# Movement Syntax

Movement is fairly simple: it always "moves" in the left direction, however by using a negative number you may switch the arrow.

The main syntax follows:

```
...
[TYPE^1 value]
...
```

The `^` operator (no spaces) indicates that movement stems from that node, and moves over `1` element to the left. The number can be changed to any number as long as there exists an element for it to move up to. The operator may also be duplicated:

```
...
[TYPE^1^2^3 value]
...
```

This syntax makes three individual movement arrows going `1` element over, `2` elements over, and `3` elements over.

```
...
[TYPE^1,2 value]
...
```

Having two numbers seperated by a comma indicates that the movement occurs at one of the parent nodes. Here with `^1,2` the syntax indicates that the movement goes `1` element to the left, and then rises to the node `2` parents above. In this way you may have end nodes with children (see [Examples/chomsky.txt](Examples/chomsky.txt) for a working example).

As stated previously, negating the movement value will cause the arrow to reverse, so, let's say that one wished to indicate movement right-ward, they would they need to place the `^` operation on the 'end' node and use `-x` steps:

```
...
[TYPE^-1,2 value]
...
```

# Text Syntax

Generally just [bracket notation](DOCS/IntroductionToBracketNotation.md), with some text effects:

`\n` newline

`[TYPE value 1\nvalue 2]`

![](/DOCS/SyntaxDemo/newline.png)

`_WORDS_` subscript

`[TYPE_SUBTYPE_]`

![](/DOCS/SyntaxDemo/subscript.png)

`*WORDS*` bold

`[TYPE *value*]`

![](/DOCS/SyntaxDemo/bold.png)

`%WORDS%` italic

`[TYPE %value%]`

![](/DOCS/SyntaxDemo/italic.png)

`$WORDS$` smaller font

`[TYPE BIG$SMALL$]`

![](/DOCS/SyntaxDemo/small.png)

`@WORDS@` underline

`[TYPE @value@]`

![](/DOCS/SyntaxDemo/underline.png)

`#WORDS#` highlight

`[TYPE #value#]`

![](/DOCS/SyntaxDemo/highlight.png)

At the end of a node, i.e. the `@` in `[N value value@]` you may place these additions to determine what the connecting bar should be:

`^`	triangle

`[TYPE value^]`

![](/DOCS/SyntaxDemo/triangle.png)

`|`	full bar

`[TYPE value|]`

![](/DOCS/SyntaxDemo/bar.png)

If you need to have multiple words in a token, use the ` around the words.

Of course, all combinations are acceptable.

(e.g. `$*%_W_O_R_D_S_%*$`)

The program defaults to the Doulos SIL font (https://software.sil.org/doulos/), so you should probably have that installed. Not entirely sure what Java defaults to automatically.

Will build web interface at some point?

Currently there's a shoddy GUI interface I built for those who don't want to use the command line.

# Examples

1. Danube river steam ship driving company captain cabin door
![danube](/Examples/danube.png)

2. Troiae ab Oris
![troiae_ab_oris](/Examples/troiae_ab_oris.png)

3. Colorless green ideas sleep furiously
![chompy](/Examples/chomsky.png)

4. あなたはユニコードの書物を使える
![あなたはユニコードの書物を使える](/Examples/jpn.png)
If your font has the characters, you can use any unicode characters in data files. (This example uses Unifont)