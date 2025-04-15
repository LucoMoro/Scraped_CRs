/*- Add is_ringtone to video columns.*/




//Synthetic comment -- diff --git a/core/java/android/provider/MediaStore.java b/core/java/android/provider/MediaStore.java
//Synthetic comment -- index b6f96c4..08ccbb5 100644

//Synthetic comment -- @@ -1348,6 +1348,12 @@
* <P>Type: INTEGER</P>
*/
public static final String BOOKMARK = "bookmark";

            /**
             * Non-zero id the video file may be a ringtone
             * <P>Type: INTEGER (boolean)</P>
             */
            public static final String IS_RINGTONE = "is_ringtone";
}

public static final class Media implements VideoColumns {







