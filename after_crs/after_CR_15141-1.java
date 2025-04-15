/*Fixed to display correct date format user selected on status bar

Change-Id:I9b05c2db852bac49b566dac77ca1865905965258*/




//Synthetic comment -- diff --git a/services/java/com/android/server/status/DateView.java b/services/java/com/android/server/status/DateView.java
//Synthetic comment -- index 78bfd5e..43f0e51 100644

//Synthetic comment -- @@ -8,6 +8,7 @@
import android.util.Log;
import android.widget.TextView;
import android.view.MotionEvent;
import android.text.format.DateFormat;

import java.text.DateFormat;
import java.util.Date;
//Synthetic comment -- @@ -51,7 +52,8 @@

private final void updateClock() {
Date now = new Date();
        java.text.DateFormat shortDateFormat = DateFormat.getDateFormat(getContext());
        setText(shortDateFormat.format(now));
}

void setUpdates(boolean update) {







