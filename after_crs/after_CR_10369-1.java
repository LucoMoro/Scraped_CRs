/*Documentation: Clarify that a FileDescriptor must be seekable

In MediaPlayer, methods with an offset specified must be passed a
FileDescriptor that is seekable. This change notes that in the
JavaDoc.*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaPlayer.java b/media/java/android/media/MediaPlayer.java
//Synthetic comment -- index 19ab0ad..cfe3b02 100644

//Synthetic comment -- @@ -633,7 +633,8 @@
}

/**
     * Sets the data source (FileDescriptor) to use.  The FileDescriptor must be
     * seekable (N.B. a LocalSocket is not seekable). It is the caller's responsibility
* to close the file descriptor. It is safe to do so as soon as this call returns.
* 
* @param fd the FileDescriptor for the file you want to play







