/*Fix refresh issue in the resource chooser

When you switch between Framework and Project resources in the
resource chooser, the icon preview would not get updated since
apparently no selection event is fired. This fixes that issue.

Change-Id:I431ba3be4fbc01594232deb1a2f3949894cd8899*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index fc026e4..856928d 100644

//Synthetic comment -- @@ -192,6 +192,7 @@
if (mProjectButton.getSelection()) {
setupResourceList();
mNewButton.setEnabled(true);
}
}
});
//Synthetic comment -- @@ -204,6 +205,7 @@
if (mSystemButton.getSelection()) {
setupResourceList();
mNewButton.setEnabled(false);
}
}
});
//Synthetic comment -- @@ -261,6 +263,10 @@
}
}

if (mPreviewHelper != null) {
computeResult();
mPreviewHelper.updatePreview(mResourceType, mCurrentResource);







