/*Do not set the content description on the icon tab when not necessary

It is not necessary to set the content description of a tab's icon
when the icon is not actually visible. Based on the implementation,
the tab's icon is only visible when not null and its visibility is 
set to true.

Change-Id:I4430dfc7d1faa0ae00fdc789745e7cae7b453a89*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/ScrollingTabContainerView.java b/core/java/com/android/internal/widget/ScrollingTabContainerView.java
//Synthetic comment -- index b620568..f8ff948 100644

//Synthetic comment -- @@ -449,7 +449,7 @@
mTextView.setText(null);
}

                if (mIconView != null && mIconView.getVisibility() == View.VISIBLE) {
mIconView.setContentDescription(tab.getContentDescription());
}








