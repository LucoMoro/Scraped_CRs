/*Patch to backup settings using BackupAgentHelper

Change-Id:Ieedb6ef410a7012082c29c32abf7df1142ed0597*/




//Synthetic comment -- diff --git a/src/com/android/calendar/CalendarBackupAgent.java b/src/com/android/calendar/CalendarBackupAgent.java
new file mode 100644
//Synthetic comment -- index 0000000..5843ec5

//Synthetic comment -- @@ -0,0 +1,16 @@
package com.android.calendar;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class CalendarBackupAgent extends BackupAgentHelper
{
    static final String SHARED_KEY = "shared_pref";

    public void onCreate ()
    {
        addHelper(SHARED_KEY, new SharedPreferencesBackupHelper(this,
                                                                CalendarPreferenceActivity.SHARED_PREFS_NAME));
    }
}
\ No newline at end of file







