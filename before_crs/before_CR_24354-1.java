/*add test on the Intent to start Gallery and the Intent to view the bookmark in Browser

Change-Id:I0aaf89a2caffe19fa39c1d1f3ee4f89956bd0820*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index 45aff4c..4a2a45e 100644

//Synthetic comment -- @@ -26,6 +26,9 @@
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.List;

//Synthetic comment -- @@ -197,4 +200,35 @@
assertCanBeHandled(intent);
}
}
}







