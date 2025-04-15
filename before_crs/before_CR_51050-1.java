/*Fix Settings app crash after enabled multi-user

Bug: 8128225
Change-Id:I2918a5ec36e861a1ce4b2e67cf7607929732a7bf*/
//Synthetic comment -- diff --git a/src/com/android/settings/Settings.java b/src/com/android/settings/Settings.java
//Synthetic comment -- index f045db0..46f1cc3 100644

//Synthetic comment -- @@ -460,14 +460,14 @@
}
}

            if (target.get(i) == header
&& UserHandle.MU_ENABLED && UserHandle.myUserId() != 0
&& !ArrayUtils.contains(SETTINGS_FOR_RESTRICTED, id)) {
target.remove(i);
}

// Increment if the current one wasn't removed by the Utils code.
            if (target.get(i) == header) {
// Hold on to the first header, when we need to reset to the top-level
if (mFirstHeader == null &&
HeaderAdapter.getHeaderType(header) != HeaderAdapter.HEADER_TYPE_CATEGORY) {







