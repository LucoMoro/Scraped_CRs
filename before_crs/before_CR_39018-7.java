/*Add compatibility implementation of ActivityOptions and start activity.

This will allow for constructing ActivityOptions and starting an activity
using them in a backwards-compatible way. The implementation is only used
on API 16 and newer. On platforms prior to API 16 the traditional
startActivity and startActivityForResult methods are used.

Change-Id:I86c0df28dadc391b340303d448c70a25d33a76de*/
//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/ActivityCompat.java b/v4/java/android/support/v4/app/ActivityCompat.java
//Synthetic comment -- index c4aed2c..4bdfd90 100644

//Synthetic comment -- @@ -17,7 +17,9 @@
package android.support.v4.app;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
//Synthetic comment -- @@ -64,4 +66,57 @@
}
return false;
}
}








//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/ActivityOptionsCompat.java b/v4/java/android/support/v4/app/ActivityOptionsCompat.java
new file mode 100644
//Synthetic comment -- index 0000000..f426a69

//Synthetic comment -- @@ -0,0 +1,156 @@








//Synthetic comment -- diff --git a/v4/jellybean/android/support/v4/app/ActivityCompatJB.java b/v4/jellybean/android/support/v4/app/ActivityCompatJB.java
new file mode 100644
//Synthetic comment -- index 0000000..a4b527f

//Synthetic comment -- @@ -0,0 +1,32 @@








//Synthetic comment -- diff --git a/v4/jellybean/android/support/v4/app/ActivityOptionsCompatJB.java b/v4/jellybean/android/support/v4/app/ActivityOptionsCompatJB.java
new file mode 100644
//Synthetic comment -- index 0000000..df9d987

//Synthetic comment -- @@ -0,0 +1,58 @@







