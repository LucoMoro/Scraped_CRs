/*Fixing spinner dialog crash upon orientation changes.

Bug:http://code.google.com/p/android/issues/detail?id=4936Calling dismiss() on the Spinner internal dialog inside
onDetachedFromWindow() crashes since the window has been
removed. See crash stack trace details on issue's page.

Change-Id:Iead225975701fc8c2f7475f4ed6572dba2578f84Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/Spinner.java b/core/java/android/widget/Spinner.java
//Synthetic comment -- index ec3790e..7ae3aa6 100644

//Synthetic comment -- @@ -260,15 +260,6 @@
}
}

/**
* <p>A spinner does not support item click events. Calling this method
* will raise an exception.</p>







