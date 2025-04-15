/*Apps/MMS: Fix memory leak due to excessive Jni global references

When the ComposeMessageActivity is destroyed, the cursor for
RecipientsAdapter is not getting closed. This leads to increase in
number of Jni references each time the ComposeMessageActivity is
opened. Thus closing the cursor for RecipientsAdapter while destroying
the ComposeMessageActivity.

Change-Id:I98f33e9681ceca7fb5485e277fe3c6a529163c28*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 7cd685b..28c292c 100644

//Synthetic comment -- @@ -112,6 +112,7 @@
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//Synthetic comment -- @@ -1996,6 +1997,12 @@
android.os.Debug.stopMethodTracing();
}

super.onDestroy();
}








