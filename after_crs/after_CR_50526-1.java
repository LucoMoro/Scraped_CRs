/*Mms: Show menu option to delete all messages

Delete all messages option is not visible in "Manage SIM card messages"
so unable to delete all SIM card messages at a time.
Use invalidate menu options to show "Delete all messages" option.

Change-Id:I77645f51bffe2ca9718f81483e93e3bf925f293a*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index beadb54..e783294 100644

//Synthetic comment -- @@ -150,6 +150,8 @@
// Let user know the SIM is empty
updateState(SHOW_EMPTY);
}
            // Show option menu when query complete.
            invalidateOptionsMenu();
}
}








