/*Changing formatBytes to Formatter.formatFileSize.

Change-Id:I3b7dc6d755374f1bf732f2b36382651e32d85bcf*/
//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/Utils.java b/src/com/android/settings/fuelgauge/Utils.java
//Synthetic comment -- index 2ffc9de..e833f6e8 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.settings.fuelgauge;

import android.content.Context;

import com.android.settings.R;

//Synthetic comment -- @@ -72,13 +73,6 @@
* @return the formatted size such as 4.52 MB or 245 KB or 332 bytes
*/
public static String formatBytes(Context context, double bytes) {
        // TODO: I18N
        if (bytes > 1000 * 1000) {
            return String.format("%.2f MB", ((int) (bytes / 1000)) / 1000f);
        } else if (bytes > 1024) {
            return String.format("%.2f KB", ((int) (bytes / 10)) / 100f);
        } else {
            return String.format("%d bytes", (int) bytes);
        }
}
}







