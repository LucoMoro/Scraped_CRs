/*Nuke Problematic ContextWrapperTest

Change-Id:I8cfef23594ff550ecfc903ddb1e7becb08af79ba*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextWrapperTest.java b/tests/tests/content/src/android/content/cts/ContextWrapperTest.java
//Synthetic comment -- index 265ec06..ac402ef 100644

//Synthetic comment -- @@ -54,13 +54,9 @@
import android.view.animation.cts.DelayedCheck;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
//Synthetic comment -- @@ -353,111 +349,6 @@
mContextWrapper.unregisterReceiver(broadcastReceiver);
}

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "enforceCallingOrSelfPermission",







