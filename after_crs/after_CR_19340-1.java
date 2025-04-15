/*Corrected scroll behavior for right-to-left paragraphs.

Paragraphs with RTL direction was misplaced when pressing
and dragging on the text field. The alignment used in Layout have no
meaning without the paragraph direction. ALIGN_NORMAL for a Latin text
is left aligned and ALIGN_OPPOSITE is right aligned but ALIGN_NORMAL
for Arabic is right alignment and ALIGN_OPPOSITE is left aligned.

The scrollTo method in Touch.java made the assumption that ALIGN_NORMAL
always is left alignment and ALIGN_OPPOSITE is right which is wrong.

Change-Id:I5033dac1f10b86365d1bce6afec6a7b84b05dcf9*/




//Synthetic comment -- diff --git a/core/java/android/text/method/Touch.java b/core/java/android/text/method/Touch.java
//Synthetic comment -- index 42ad10e..769b719 100644

//Synthetic comment -- @@ -45,11 +45,16 @@
int left = Integer.MAX_VALUE;
int right = 0;
Alignment a = null;
        int dir = 0;

for (int i = top; i <= bottom; i++) {
left = (int) Math.min(left, layout.getLineLeft(i));
right = (int) Math.max(right, layout.getLineRight(i));

            if (dir == 0) {
                dir = layout.getParagraphDirection(i);
            }

if (a == null) {
a = layout.getParagraphAlignment(i);
}
//Synthetic comment -- @@ -62,7 +67,8 @@
if (right - left < width - padding) {
if (a == Alignment.ALIGN_CENTER) {
diff = (width - padding - (right - left)) / 2;
            } else if ((a == Alignment.ALIGN_OPPOSITE && dir == Layout.DIR_LEFT_TO_RIGHT)
                    || (a == Alignment.ALIGN_NORMAL && dir == Layout.DIR_RIGHT_TO_LEFT)) {
diff = width - padding - (right - left);
}
}







