/*ResolverActivity removes activity from recent applications

If an application is present in recent applications and it
for some reason is open again from ResolverActivity the
application is removed from recent applications.

The application is removed since it's started with flag
FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS. The intent used to start
ResolverActivity (mIntent) is also used to start the activity
selected in the ResolverActivity. ResolverActivity is started with
FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS set.

The solution is to use the flags of the ActivityInfo of the selected
activity in the ResolverActivity.

The following steps reproduce the problem:
1. Launch Browser->longpress homekey->Browser is displayed in recent applications
2. Go to www.google.se
3. Enter y in the searchbar and press go
4. Launch ResolverActivity by selecting a video in search results
5. Select browser in ResolverActivity's list
6. Press homekey -> Browser is removed from recent applications

Change-Id:Iba56e16ebc3144b6ba2666f029c487683dc3d8af*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ResolverActivity.java b/core/java/com/android/internal/app/ResolverActivity.java
//Synthetic comment -- index 841de06..a990a46 100644

//Synthetic comment -- @@ -371,8 +371,17 @@

DisplayResolveInfo dri = mList.get(position);

            // If DisplayResolveInfo has an associated intent
            // use it else use the intent that started ResolverActivity
            // and clear the FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS flag.
            Intent intent;
            if (dri.origIntent != null) {
                intent = dri.origIntent;
            } else {
                intent = mIntent;
                intent.setFlags(intent.getFlags() &
                       ~Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            }
intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT
|Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
ActivityInfo ai = dri.ri.activityInfo;







