/*Fix 1855: Restore constants to pre-cupcake values.

Since some of those constants are used in the persistent database,
modifying them for cupcake meant that the new code was getting
confused by the old persistified data after an upgrade to cupcake,
teh most visible symptom being that downloads that had been initiated
from the browser would re-appear in the notifications.*/
//Synthetic comment -- diff --git a/core/java/android/provider/Downloads.java b/core/java/android/provider/Downloads.java
//Synthetic comment -- index 9593595..790fe5c 100644

//Synthetic comment -- @@ -471,34 +471,40 @@
public static final int STATUS_UNHANDLED_REDIRECT = 493;

/**
     * This download couldn't be completed because there were
     * too many redirects.
     */
    public static final int STATUS_TOO_MANY_REDIRECTS = 494;

    /**
* This download couldn't be completed because of an
* unspecified unhandled HTTP code.
*/
    public static final int STATUS_UNHANDLED_HTTP_CODE = 495;

/**
* This download couldn't be completed because of an
* error receiving or processing data at the HTTP level.
*/
    public static final int STATUS_HTTP_DATA_ERROR = 496;

/**
     * This download is visible and shows in the notifications while
     * in progress and after completion.
*/
    public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 0;

/**
* This download is visible but only shows in the notifications
* while it's in progress.
*/
    public static final int VISIBILITY_VISIBLE = 1;

/**
* This download doesn't show in the UI or in the notifications.







