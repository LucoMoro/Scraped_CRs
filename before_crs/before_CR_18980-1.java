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
                    mStyleIDs.boldId = nativeIndexOfString(mNative, "b");
                    mStyleIDs.italicId = nativeIndexOfString(mNative, "i");
                    mStyleIDs.underlineId = nativeIndexOfString(mNative, "u");
                    mStyleIDs.ttId = nativeIndexOfString(mNative, "tt");
                    mStyleIDs.bigId = nativeIndexOfString(mNative, "big");
                    mStyleIDs.smallId = nativeIndexOfString(mNative, "small");
                    mStyleIDs.supId = nativeIndexOfString(mNative, "sup");
                    mStyleIDs.subId = nativeIndexOfString(mNative, "sub");
                    mStyleIDs.strikeId = nativeIndexOfString(mNative, "strike");
                    mStyleIDs.listItemId = nativeIndexOfString(mNative, "li");
                    mStyleIDs.marqueeId = nativeIndexOfString(mNative, "marquee");

                    if (localLOGV) Log.v(TAG, "BoldId=" + mStyleIDs.boldId
                            + ", ItalicId=" + mStyleIDs.italicId
                            + ", UnderlineId=" + mStyleIDs.underlineId);
}

res = applyStyles(str, style, mStyleIDs);
//Synthetic comment -- @@ -119,17 +144,17 @@
}

static final class StyleIDs {
        private int boldId;
        private int italicId;
        private int underlineId;
        private int ttId;
        private int bigId;
        private int smallId;
        private int subId;
        private int supId;
        private int strikeId;
        private int listItemId;
        private int marqueeId;
}

private CharSequence applyStyles(String str, int[] style, StyleIDs ids) {







