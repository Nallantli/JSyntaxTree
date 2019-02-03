# Overview

Labelled Bracket Notation, usually just shortened to Bracket Notation, is a convention used by linguists so as to represent tree-structures without, essentially, having to actually draw trees. LBN is widely used by syntacticians and morphologists in lieu even of those actual trees, for sake of it being easier to generate compared to putting the effort into drawing trees.

For that reason it is imperative to learn the basics of LBN so that one may utilize the software and resources made available to syntacticians, like this program here. All SyntaxTree generators found on the web use LBN, so even if JSyntaxTree were not your preferred choice, it would still be necessary that you understand the notation.

Bracket Notation is so-called because it uses brackets: `[` and `]`. The data between the brackets is called a _Node_. A tree with a single node may therefore look like this:

```
[N world]
```

Note that within the brackets there are two identifiable elements, the `N` and `world`, separated by a space. Bracket Notation suggests that each node have a label, this being the first element right following the first bracket - and that it have some value, this being all the writing that follows (including subsequent spaces) the space after the label.

```
[N White House]
```

Here, even though `White` and `House` are separated with a space, LBN treats them as a single entity, since they follow the label declared as `N`. It may be easier to consider the LBN as an algorithm that takes text between brackets and splits the text at the first space, making two strings of `"N"` and `"White House"` i.e. `["N", "White House"]`.

Now certainly, trees are much more than single nodes. Adding children is equally trivial however, it takes only that you replace the node value with another (set of) bracketed node(s), e.g.:

```
[NP [N world]]
```

This designates `[N world]` to be a child of `[NP ...]`.

```
[N [D the][N world]]
```

Multiple elements can exist as siblings, a term describing two elements right next to each other with the same parent.