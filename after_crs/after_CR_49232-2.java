/*Use EmptyArray.STRING in Splitter.fastSplit().

This is a minor cleanup afterhttps://android-review.googlesource.com/47964Change-Id:Id8de6c09f3397b044082c275095ff7a7a43185f9*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Splitter.java b/luni/src/main/java/java/util/regex/Splitter.java
//Synthetic comment -- index 40d6984..a4cb52c 100644

//Synthetic comment -- @@ -77,7 +77,7 @@
// Last part is empty for limit == 0, remove all trailing empty matches.
if (separatorCount == lastPartEnd) {
// Input contains only separators.
                return EmptyArray.STRING;
}
// Find the beginning of trailing separators.
do {







