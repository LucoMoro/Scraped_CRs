/*When going to other activity destroy self.

After Settings->Account & Sync, add a new account, and save
it. We now return to the account list rather than to the
creating account view. This is considered to be more what
people expect.

Change-Id:I999c8502f4c5eb0d32637b20b68db8931b2b453e*/




//Synthetic comment -- diff --git a/src/com/android/settings/AddAccountSettings.java b/src/com/android/settings/AddAccountSettings.java
//Synthetic comment -- index 50c95a9..0fa7be4 100644

//Synthetic comment -- @@ -129,6 +129,7 @@
ProviderPreference pref = (ProviderPreference) preference;
if (LDEBUG) Log.v(TAG, "Attempting to add account of type " + pref.getAccountType());
addAccount(pref.getAccountType());
            finish();
}
return true;
}







