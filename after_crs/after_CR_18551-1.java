/*Always set right auth_type value in apn.

It fixes a bug that automatically sets auth_type value
of editing apn that has and should have that value set
to null (-1 = Not yet set) to auth_type value of an
apn previously displayed in the apn editor. It is
caused by careless persisting and reading data from
SharedPreferences. Fixed to keep and persist (to be
consistent with other apn's Preferences) null value
for not yet set auth_type property.

Change-Id:I4e125a4cdbad968c9f6ea8781a8c849f4c31c929*/




//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index 72dba1a..3f0c02f 100644

//Synthetic comment -- @@ -234,6 +234,8 @@
int authVal = mCursor.getInt(AUTH_TYPE_INDEX);
if (authVal != -1) {
mAuthType.setValueIndex(authVal);
            } else {
                mAuthType.setValue(null);
}

}







