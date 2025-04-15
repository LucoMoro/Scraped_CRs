/*Fix Home launch on Settings-Clear Data for Home

A blank screen(InCall Screen) or multiple screens behind Home in the
History Stack are displayed after "Clear Data" for Home application
from the settings and clicking the back button to go back to Home.
This happens only if the user has received atleast one call(blank screen)
or launches any of the activities which call moveTaskToBack or one of
its variants by launching apps like Browser).

 -for more details see issuehttp://code.google.com/p/android/issues/detail?id=6669*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 40283a8..a106680 100644

//Synthetic comment -- @@ -5115,7 +5115,8 @@

for (i=mHistory.size()-1; i>=0; i--) {
HistoryRecord r = (HistoryRecord)mHistory.get(i);
            if (r.packageName.equals(name)) {
if (Config.LOGD) Log.d(
TAG, "  Force finishing activity "
+ r.intent.getComponent().flattenToShortString());







