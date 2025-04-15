/*Fixing the attempt of update the shortcut icon on DB before retrieving the id

Change-Id:I18170d5a80e3a2288b78656660ce1c23b80dadee*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherModel.java b/src/com/android/launcher2/LauncherModel.java
//Synthetic comment -- index b819510..b68c658 100644

//Synthetic comment -- @@ -701,7 +701,6 @@
}

if (info != null) {

info.intent = intent;
info.id = c.getLong(idIndex);
//Synthetic comment -- @@ -711,6 +710,8 @@
info.cellX = c.getInt(cellXIndex);
info.cellY = c.getInt(cellYIndex);

                                updateSavedIcon(context, info, c, iconIndex);

// check & update map of what's occupied
if (!checkItemPlacement(occupied, info)) {
break;







