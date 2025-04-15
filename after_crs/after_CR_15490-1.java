/*Send spaces only if we are already filtered.

Change-Id:I6994002a911a34565739d0bb42db221615799ba6*/




//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index eea97dc..14c1e55 100644

//Synthetic comment -- @@ -2917,7 +2917,7 @@
break;
case KeyEvent.KEYCODE_SPACE:
// Only send spaces once we are filtered
            okToSend = mFiltered;
break;
}








