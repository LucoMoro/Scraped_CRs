/*Gallery2: Use ICS alert drawable

Same bug here. It's using the old compability drawable
instead of the Holo theme one.

Change-Id:Ia84ce1d305ee63fc6bc1fdb407a91b3421842eb1*/




//Synthetic comment -- diff --git a/src/com/android/gallery3d/app/AbstractGalleryActivity.java b/src/com/android/gallery3d/app/AbstractGalleryActivity.java
//Synthetic comment -- index d25f60e..9500fff 100644

//Synthetic comment -- @@ -142,7 +142,7 @@
}
};
mAlertDialog = new AlertDialog.Builder(this)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setTitle("No Storage")
.setMessage("No external storage available.")
.setNegativeButton(android.R.string.cancel, onClick)







