/*Support surrogate pairs when layouting text

The current framework does not consider surrogate pairs
when getting the index of the character. This bug becomes
visible when creating the text including Emojis. For
example cursor breaks up when it moves around the Emojis.

Our proposed solution will consider the surrogate pairs
when calculating the index. It will fix not only the Emoji
case, but also the letters that use surrogate pairs.

Change-Id:I4983f2e4df933c8af9d5f0cc27df871e8e10fed4*/




//Synthetic comment -- diff --git a/core/java/android/text/Layout.java b/core/java/android/text/Layout.java
old mode 100644
new mode 100755
//Synthetic comment -- index 38ac9b7..4e197cd

//Synthetic comment -- @@ -749,6 +749,9 @@
if (line == getLineCount() - 1)
max++;

        if (line != getLineCount() - 1)
            max = TextUtils.getOffsetBefore(mText, getLineEnd(line));

int best = min;
float bestdist = Math.abs(getPrimaryHorizontal(best) - horiz);

//Synthetic comment -- @@ -893,7 +896,7 @@
Directions dirs = getLineDirections(line);

if (line != getLineCount() - 1)
            end = TextUtils.getOffsetBefore(mText, end);

float horiz = getPrimaryHorizontal(offset);

//Synthetic comment -- @@ -993,7 +996,7 @@
Directions dirs = getLineDirections(line);

if (line != getLineCount() - 1)
            end = TextUtils.getOffsetBefore(mText, end);

float horiz = getPrimaryHorizontal(offset);

//Synthetic comment -- @@ -1564,7 +1567,8 @@
h = dir * nextTab(text, start, end, h * dir, tabs);
}

                    if (j != there && bm != null) {
                        if (offset == start + j) return h;
workPaint.set(paint);
Styled.measureText(paint, workPaint, text,
j, j + 2, null);
//Synthetic comment -- @@ -1958,4 +1962,3 @@
new Directions(new short[] { 0, 32767 });

}








//Synthetic comment -- diff --git a/core/java/android/text/StaticLayout.java b/core/java/android/text/StaticLayout.java
old mode 100644
new mode 100755
//Synthetic comment -- index f02ad2a..d0d2482

//Synthetic comment -- @@ -313,7 +313,9 @@
class);

if (spanned == null) {
                    final int actualNum = paint.getTextWidths(sub, i, next, widths);
                    if (next - i > actualNum)
                        adjustTextWidths(widths, sub, i, next, actualNum);
System.arraycopy(widths, 0, widths,
end - start + (i - start), next - i);

//Synthetic comment -- @@ -321,9 +323,11 @@
} else {
mWorkPaint.baselineShift = 0;

                    final int actualNum = Styled.getTextWidths(paint, mWorkPaint,
                            spanned, i, next,
                            widths, fm);
                    if (next - i > actualNum)
                        adjustTextWidths(widths, spanned, i, next, actualNum);
System.arraycopy(widths, 0, widths,
end - start + (i - start), next - i);

//Synthetic comment -- @@ -966,6 +970,22 @@
return low;
}

    private static void adjustTextWidths(float[] widths, CharSequence text,
                              int curPos, int nextPos, int actualNum) {
        try {
            int dstIndex = nextPos - curPos - 1;
            for (int srcIndex = actualNum - 1; srcIndex >= 0; srcIndex--) {
                final char c = text.charAt(dstIndex + curPos);
                if (c >= 0xD800 && c <= 0xDFFF) {
                    widths[dstIndex--] = 0.0f;
                }
                widths[dstIndex--] = widths[srcIndex];
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e("text", "adjust text widths failed");
        }
    }

private int out(CharSequence text, int start, int end,
int above, int below, int top, int bottom, int v,
float spacingmult, float spacingadd,








//Synthetic comment -- diff --git a/core/java/android/text/Styled.java b/core/java/android/text/Styled.java
old mode 100644
new mode 100755
//Synthetic comment -- index 513b2cd..13cc42c

//Synthetic comment -- @@ -203,9 +203,10 @@
			}
		}
	
        int result;
if (replacement == null) {
workPaint.getFontMetricsInt(fmi);
            result = workPaint.getTextWidths(text, start, end, widths);
} else {
int wid = replacement.getSize(workPaint, text, start, end, fmi);

//Synthetic comment -- @@ -214,8 +215,9 @@
for (int i = start + 1; i < end; i++)
widths[i - start] = 0;
}
            result = end - start;
}
        return result;
}

/**







