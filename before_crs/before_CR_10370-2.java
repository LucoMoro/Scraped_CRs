/*Documentation: Clarify that a FileDescriptor must be seekable

In MediaPlayer, methods with an offset specified must be passed a
FileDescriptor that is seekable. This change notes that in the
JavaDoc.*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaPlayer.java b/media/java/android/media/MediaPlayer.java
//Synthetic comment -- index 3b46d69..fe768be 100644

//Synthetic comment -- @@ -644,7 +644,8 @@
}

/**
     * Sets the data source (FileDescriptor) to use.  It is the caller's responsibility
* to close the file descriptor. It is safe to do so as soon as this call returns.
*
* @param fd the FileDescriptor for the file you want to play







