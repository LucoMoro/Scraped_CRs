/*Fix possible integer overflow in MtpStorage

When converting the MB value MtpReserveSpace value to bytes there is a
possible integer overflow since 3 integer values are multiplied, even
though the result is stored in a long variable. This would be visible when
the value is more than 2 GB. This is solved by making the constants
longs instead of ints.

Change-Id:I799129f7076a6e331cece17b5c05aed210499648*/




//Synthetic comment -- diff --git a/media/java/android/mtp/MtpStorage.java b/media/java/android/mtp/MtpStorage.java
//Synthetic comment -- index 9cf65a3..e20eabc 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
mStorageId = volume.getStorageId();
mPath = volume.getPath();
mDescription = context.getResources().getString(volume.getDescriptionId());
        mReserveSpace = volume.getMtpReserveSpace() * 1024L * 1024L;
mRemovable = volume.isRemovable();
mMaxFileSize = volume.getMaxFileSize();
}







