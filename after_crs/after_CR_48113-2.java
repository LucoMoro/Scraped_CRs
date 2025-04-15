/*Fix NullpointException problem in onOptionMenuSelected

If both title and condensed title is null,
item.getTitleCondensed() could be return null value
in onOptionMenuSelected. therefore need to check
whether retun value is null or not.

Change-Id:Ib08f52b949a794aa7bd6cc25414041e820f62969*/




//Synthetic comment -- diff --git a/core/java/android/app/Activity.java b/core/java/android/app/Activity.java
//Synthetic comment -- index f6b9a8e..e844581 100644

//Synthetic comment -- @@ -2544,7 +2544,9 @@
// Put event logging here so it gets called even if subclass
// doesn't call through to superclass's implmeentation of each
// of these methods below
                if(item.getTitleCondensed() != null) {
                    EventLog.writeEvent(50000, 0, item.getTitleCondensed().toString());
                }
if (onOptionsItemSelected(item)) {
return true;
}
//Synthetic comment -- @@ -2562,7 +2564,9 @@
return false;

case Window.FEATURE_CONTEXT_MENU:
                if(item.getTitleCondensed() != null) {
                    EventLog.writeEvent(50000, 1, item.getTitleCondensed().toString());
                }
if (onContextItemSelected(item)) {
return true;
}







