/*Allow user to raise OOM adj of Home app.

This selects the OOM adj as either HOME_APP_ADJ or VISIBLE_APP_ADJ based
on the value of a system property.  This is useful for keeping the home
app from constantly reloading at the expense of additional memory
pressure.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index a473db27..2ed6895 100644

//Synthetic comment -- @@ -12403,8 +12403,14 @@
} else if (app == mHomeProcess) {
// This process is hosting what we currently consider to be the
// home app, so we don't want to let it go into the background.
            adj = HOME_APP_ADJ;
            app.adjType = "home";
} else if ((N=app.activities.size()) != 0) {
// This app is in the background with paused activities.
adj = hiddenAdj;







