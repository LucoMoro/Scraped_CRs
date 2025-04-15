/*Add and document .nomedia constant for MediaStore (Issue 6365)*/




//Synthetic comment -- diff --git a/core/java/android/provider/MediaStore.java b/core/java/android/provider/MediaStore.java
//Synthetic comment -- index 062080d..d132ac72 100644

//Synthetic comment -- @@ -1695,4 +1695,11 @@
* Name of current volume being scanned by the media scanner.
*/
public static final String MEDIA_SCANNER_VOLUME = "volume";

    /**
     * Name of the file signalling the media scanner to ignore media in the containing directory
     * and its subdirectories. Developers should use this to avoid littering the Gallery with
     * application graphics.
     */
    public static final String MEDIA_IGNORE_FILENAME = ".nomedia";
}







