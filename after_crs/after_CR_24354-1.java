/*add test on the Intent to start Gallery and the Intent to view the bookmark in Browser

Change-Id:I0aaf89a2caffe19fa39c1d1f3ee4f89956bd0820*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index 45aff4c..4a2a45e 100644

//Synthetic comment -- @@ -26,6 +26,9 @@
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.content.ContentUris;
import android.provider.Browser;
import android.provider.MediaStore;

import java.util.List;

//Synthetic comment -- @@ -197,4 +200,35 @@
assertCanBeHandled(intent);
}
}

    /**
     * Test view the web bookmark
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "Intent",
        args = {java.lang.String.class}
    )
    public void testViewBookmark() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(ContentUris.withAppendedId(Browser.BOOKMARKS_URI, 1));
        assertCanBeHandled(intent);
    }

    /**
     * Test view images by Gallery
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "Intent",
        args = {java.lang.String.class}
    )
    public void testGallery() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setClassName("com.android.camera",
                "com.android.camera.ImageGallery2");
        assertCanBeHandled(intent);
    }
}







