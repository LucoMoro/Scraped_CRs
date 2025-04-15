/*Improve performance when getting styled string.

The style used in the composing text for input methods
takes a long time to create. This is experienced as a
lag when composing the first word.

The bottleneck lies in the 10 calls to
nativeIndexOfString which does a linear search through
thousands of strings.

Change-Id:I3184b2be3673d384cca19e9a70ad94b4d3085576*/




//Synthetic comment -- diff --git a/core/java/android/content/res/StringBlock.java b/core/java/android/content/res/StringBlock.java
//Synthetic comment -- index 5e90b91..16de5c5 100644

//Synthetic comment -- @@ -87,21 +87,46 @@
if (style != null) {
if (mStyleIDs == null) {
mStyleIDs = new StyleIDs();
                }

                for (int styleIndex = 0; styleIndex < style.length; styleIndex += 3) {
                    int styleId = style[styleIndex];

                    if (styleId == mStyleIDs.boldId || styleId == mStyleIDs.italicId
                            || styleId == mStyleIDs.underlineId || styleId == mStyleIDs.ttId
                            || styleId == mStyleIDs.bigId || styleId == mStyleIDs.smallId
                            || styleId == mStyleIDs.subId || styleId == mStyleIDs.supId
                            || styleId == mStyleIDs.strikeId || styleId == mStyleIDs.listItemId
                            || styleId == mStyleIDs.marqueeId) {
                        // id already found skip to next style
                        continue;
                    }

                    String styleTag = nativeGetString(mNative, styleId);

                    if (styleTag.equals("b")) {
                        mStyleIDs.boldId = styleId;
                    } else if (styleTag.equals("i")) {
                        mStyleIDs.italicId = styleId;
                    } else if (styleTag.equals("u")) {
                        mStyleIDs.underlineId = styleId;
                    } else if (styleTag.equals("tt")) {
                        mStyleIDs.ttId = styleId;
                    } else if (styleTag.equals("big")) {
                        mStyleIDs.bigId = styleId;
                    } else if (styleTag.equals("small")) {
                        mStyleIDs.smallId = styleId;
                    } else if (styleTag.equals("sup")) {
                        mStyleIDs.supId = styleId;
                    } else if (styleTag.equals("sub")) {
                        mStyleIDs.subId = styleId;
                    } else if (styleTag.equals("strike")) {
                        mStyleIDs.strikeId = styleId;
                    } else if (styleTag.equals("li")) {
                        mStyleIDs.listItemId = styleId;
                    } else if (styleTag.equals("marquee")) {
                        mStyleIDs.marqueeId = styleId;
                    }
}

res = applyStyles(str, style, mStyleIDs);
//Synthetic comment -- @@ -119,17 +144,17 @@
}

static final class StyleIDs {
        private int boldId = -1;
        private int italicId = -1;
        private int underlineId = -1;
        private int ttId = -1;
        private int bigId = -1;
        private int smallId = -1;
        private int subId = -1;
        private int supId = -1;
        private int strikeId = -1;
        private int listItemId = -1;
        private int marqueeId = -1;
}

private CharSequence applyStyles(String str, int[] style, StyleIDs ids) {







