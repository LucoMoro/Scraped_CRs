/*Fix NullpointException problem in onMenuItemSelected

If both title and condensed title is null,
item.getTitleCondensed() could be return null value
in onMenuItemSelected. therefore need to check
whether retun value is null or not.

Change-Id:Ib08f52b949a794aa7bd6cc25414041e820f62969*/




//Synthetic comment -- diff --git a/core/java/android/app/Activity.java b/core/java/android/app/Activity.java
//Synthetic comment -- index f6b9a8e..48366ab 100644

//Synthetic comment -- @@ -2539,12 +2539,16 @@
* Activity don't need to deal with feature codes.
*/
public boolean onMenuItemSelected(int featureId, MenuItem item) {
        CharSequence titleCondensed = item.getTitleCondensed();

switch (featureId) {
case Window.FEATURE_OPTIONS_PANEL:
// Put event logging here so it gets called even if subclass
// doesn't call through to superclass's implmeentation of each
// of these methods below
                if(titleCondensed != null) {
                    EventLog.writeEvent(50000, 0, titleCondensed.toString());
                }
if (onOptionsItemSelected(item)) {
return true;
}
//Synthetic comment -- @@ -2562,7 +2566,9 @@
return false;

case Window.FEATURE_CONTEXT_MENU:
                if(titleCondensed != null) {
                    EventLog.writeEvent(50000, 1, titleCondensed.toString());
                }
if (onContextItemSelected(item)) {
return true;
}







