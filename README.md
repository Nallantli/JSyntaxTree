# JSyntaxTree

![Output](/Examples/cliche.png)

A small Java program to build syntax trees and morphology trees, according to how my current morphology class prescribes.

Inspired by [yohasebe](https://github.com/yohasebe)'s [RSyntaxTree](https://github.com/yohasebe/rsyntaxtree), which came into frequent use, however I unfortunately was pressed to make something new as the features required for my class surpassed the capabilities his program.

A basic means to run it can be found with `RUNGUI.bat`.

# CLI Options:
```
Syntax		Default		Desc
-i <STRING>	-		input file path (required)
-o <STRING>	OUTPUT.png	output file path (include the .png, .jpg, etc.)
-f <STRING>	Doulos SIL	font name
-fs <INT>	48		font size
-l <FLOAT>	3.0		stroke size (for the lines)
-sx <INT>	50		spacing between adjacent nodes horizontally
-sy <INT>	200		spacing between adjacent nodes vertically
-c		-		color the tree
-q		-		quits right after generation - no preview window
-b <INT>	50		adds padding around the tree
-a		-		auto-subscript - will add a numerical subscript to node types
```

# Syntax

Generally just bracket notation, with some text effects:

`\n` newline

`[TYPE value 1\nvalue 2]`

![](/Examples/SyntaxDemo/newline.png)

`_WORDS_` subscript

`[TYPE_SUBTYPE_]`

![](/Examples/SyntaxDemo/subscript.png)

`*WORDS*` bold

`[TYPE *value*]`

![](/Examples/SyntaxDemo/bold.png)

`%WORDS%` italic

`[TYPE %value%]`

![](/Examples/SyntaxDemo/italic.png)

`$WORDS$` smaller font

`[TYPE BIG$SMALL$]`

![](/Examples/SyntaxDemo/small.png)

`@WORDS@` underline

`[TYPE @value@]`

![](/Examples/SyntaxDemo/underline.png)

`#WORDS#` highlight

`[TYPE #value#]`

![](/Examples/SyntaxDemo/highlight.png)

At the end of a node, i.e. the `@` in `[N value value@]` you may place these additions to determine what the connecting bar should be:

`^`	triangle

`[TYPE value^]`

![](/Examples/SyntaxDemo/triangle.png)

`|`	full bar

`[TYPE value|]`

![](/Examples/SyntaxDemo/bar.png)

If you need to have multiple words in a token, use the ` around the words.

Of course, all combinations are acceptable.

(e.g. `$*%_W_O_R_D_S_%*$`)

The program defaults to the Doulos SIL font (https://software.sil.org/doulos/), so you should probably have that installed. Not entirely sure what Java defaults to automatically.

Will build web interface at some point?

Currently there's a shoddy GUI interface I built in like ten minutes but it works so if command lines scare you try that.

# Examples

1. Fictionalization
![[[[[[fict][ion]]al]iz]ation]](/Examples/fictionalization.png)

2. Disapproval
![[dis[[[ap][prov]]al]]](/Examples/disapproval.png)

3. あなたはユニコードの書物を使える
![あなたはユニコードの書物を使える](/Examples/jpn.png)
If your font has the characters, you can use any unicode characters in data files. (This example uses Unifont)