/*Account for collapsed tab navigation for selection.

Since the tabs may have collapsed to be displayed in a spinner we need to account for updating the selection on it as well.

Change-Id:I07ac21e7b6bca320b5c9f576d7267dd025c623ab*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/ScrollingTabContainerView.java b/core/java/com/android/internal/widget/ScrollingTabContainerView.java
//Synthetic comment -- index 25b0065..4bb057d 100644

//Synthetic comment -- @@ -176,6 +176,9 @@
animateToTab(position);
}
}
}

public void setContentHeight(int contentHeight) {







