/*Tweak TabHost drop handler

Instead of creating a single tab, create 3 tabs instead, and pick a
better id since it's used as the tab label by the designtime TabSpec
creator.

Change-Id:I86c064eb922534748cca313ddf31cfdc175ef9b8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java
//Synthetic comment -- index 1029112..92decde 100755

//Synthetic comment -- @@ -67,10 +67,13 @@
frame.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, fillParent);
frame.setAttribute(ANDROID_URI, ATTR_ID, "@android:id/tabcontent"); //$NON-NLS-1$

            INode child = frame.appendChild(FQCN_LINEAR_LAYOUT);
            child.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, fillParent);
            child.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, fillParent);
            child.setAttribute(ANDROID_URI, ATTR_ID, "@+id/contentlayout"); //$NON-NLS-1$
}
}








