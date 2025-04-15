/*Return early when checking divider before child zero.

Previously the `getChildAt` method would be called with an index of -1 which
would lead to an exception being thrown and caught. This is unnecessary since
we know there will never be a divider before the first child. It also avoids
additional object creation since this method can be invoked quite frequently.

Change-Id:Iab44520d5d52f96a829a009cdd1201696edbf9a4*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/view/menu/ActionMenuView.java b/core/java/com/android/internal/view/menu/ActionMenuView.java
//Synthetic comment -- index f54575b..cef6a8f 100644

//Synthetic comment -- @@ -524,6 +524,9 @@

@Override
protected boolean hasDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            return false;
        }
final View childBefore = getChildAt(childIndex - 1);
final View child = getChildAt(childIndex);
boolean result = false;







