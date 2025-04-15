/*The ActivityThread will restart a stopped activity before sending onActivityResult

When an activity starts a second fullscreen activity by calling
startActivityForResult it will be stopped and any managed cursors
will be deactivated. When the second activity ends the first
activity will recieve onActivityResult. Earlier onActivityResult
was sent to the activity before it was restarted. If the activity
had any managed cursors they would still be deactivated when
onActivityResult was received. Now if the activity is stopped it
will be restarted before onActivityResult is sent to the activity.

Change-Id:I0380a1a890e98a95137eb77e3ecd54f20f908c08*/




//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 162d9eb..e15f254 100644

//Synthetic comment -- @@ -2571,6 +2571,10 @@
}
}
}
            if (r.stopped) {
                r.activity.performRestart();
                r.stopped = false;
            }
deliverResults(r, res.results);
if (resumed) {
mInstrumentation.callActivityOnResume(r.activity);







