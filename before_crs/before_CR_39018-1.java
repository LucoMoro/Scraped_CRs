/*Add compatibility implementation of ActivityOptions and startActivity.

This will allow for constructing ActivityOptions and starting an activity
using them in a backwards-compatible way. The implementation is only used
on API 16 and newer. On platforms prior to API 16 the traditional
startActivity method is used.

Change-Id:I86c0df28dadc391b340303d448c70a25d33a76de*/
//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/ActivityCompat.java b/v4/java/android/support/v4/app/ActivityCompat.java
//Synthetic comment -- index c4aed2c..612a816 100644

//Synthetic comment -- @@ -17,7 +17,9 @@
package android.support.v4.app;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
//Synthetic comment -- @@ -64,4 +66,29 @@
}
return false;
}
}








//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/ActivityOptionsCompat.java b/v4/java/android/support/v4/app/ActivityOptionsCompat.java
new file mode 100644
//Synthetic comment -- index 0000000..c96baafb9

//Synthetic comment -- @@ -0,0 +1,112 @@








//Synthetic comment -- diff --git a/v4/jellybean/android/support/v4/app/ActivityCompatJB.java b/v4/jellybean/android/support/v4/app/ActivityCompatJB.java
new file mode 100644
//Synthetic comment -- index 0000000..61ad59d

//Synthetic comment -- @@ -0,0 +1,11 @@








//Synthetic comment -- diff --git a/v4/jellybean/android/support/v4/app/ActivityOptionsCompatJB.java b/v4/jellybean/android/support/v4/app/ActivityOptionsCompatJB.java
new file mode 100644
//Synthetic comment -- index 0000000..e968f2f

//Synthetic comment -- @@ -0,0 +1,40 @@







