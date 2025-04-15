/*Fix NullPointerException in RecipientsEditor.

In function PointToPosition, should check if mLayout
is NULL before call function getExtendedPaddingTop
because mLayout is refered in getExtendedPaddingTop.

Change-Id:I750313c3a4bceb7213433cf3d110ea9e87cee0b8Signed-off-by: Bin Li <libin@marvell.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/RecipientsEditor.java b/src/com/android/mms/ui/RecipientsEditor.java
//Synthetic comment -- index 0baeff8..ae1ae34 100644

//Synthetic comment -- @@ -280,6 +280,13 @@
}

private int pointToPosition(int x, int y) {
        // Check layout before getExtendedPaddingTop().
        // mLayout is used in getExtendedPaddingTop().
        Layout layout = getLayout();
        if (layout == null) {
            return -1;
        }

x -= getCompoundPaddingLeft();
y -= getExtendedPaddingTop();

//Synthetic comment -- @@ -287,11 +294,6 @@
x += getScrollX();
y += getScrollY();

int line = layout.getLineForVertical(y);
int off = layout.getOffsetForHorizontal(line, x);








