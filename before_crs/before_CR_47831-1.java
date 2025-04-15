/*Fix EventLog string class problem in onOptionMenuSelected

EventLog function can handle string,integer class and long class. (in android_util_EventLog.cpp)
If menu title string are used bold tag(like <b>test</b>), it'll be android.text.SpannedString.
In onOptionMenuSelected, it is using item.getTitleCondensed() function for writing event log.
therefore any android activity using tag menu string(like <b></b>) can be crashed by IllegalArgumentException.

I found this crash on GMS Application.
change locale chinese -> launch Google+ -> hangout -> menu key -> Invite(expressed chinese) click -> Google+ crash

Change-Id:I0437be81699925e29bf4510eb615ef2424432763*/
//Synthetic comment -- diff --git a/core/java/android/app/Activity.java b/core/java/android/app/Activity.java
//Synthetic comment -- index 5dc9da2..f6b9a8e 100644

//Synthetic comment -- @@ -2544,7 +2544,7 @@
// Put event logging here so it gets called even if subclass
// doesn't call through to superclass's implmeentation of each
// of these methods below
                EventLog.writeEvent(50000, 0, item.getTitleCondensed());
if (onOptionsItemSelected(item)) {
return true;
}
//Synthetic comment -- @@ -2562,7 +2562,7 @@
return false;

case Window.FEATURE_CONTEXT_MENU:
                EventLog.writeEvent(50000, 1, item.getTitleCondensed());
if (onContextItemSelected(item)) {
return true;
}







