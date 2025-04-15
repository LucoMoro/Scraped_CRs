/*Remove top panel in alert dialog if empty.

Remove the topPanel in the alert dialog if it is empty and
therefore invisible. This gives the dialog content more space, and it
makes dialog positioning on the screen correct.

Change-Id:I8ffd9bd9b2360b0ad4338c32c21ab69c055d66a0*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/AlertController.java b/core/java/com/android/internal/app/AlertController.java
//Synthetic comment -- index 107b145..4a0617c 100644

//Synthetic comment -- @@ -435,6 +435,7 @@
View titleTemplate = mWindow.findViewById(R.id.title_template);
titleTemplate.setVisibility(View.GONE);
mIconView.setVisibility(View.GONE);
                topPanel.setVisibility(View.GONE);
hasTitle = false;
}
}







