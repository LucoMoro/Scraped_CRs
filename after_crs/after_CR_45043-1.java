/*Phone: Fix Nullpointer exception in GsmUmtsOptions

EnableScreen is trying to  remove the preference,
But which is already removed in GsmUmtsOptions create().
Thus the findPreference is returning null.

To fix this instead of removing  preference set
mButtonOperatorSelectionExpand setEnabled to false.

Change-Id:I156f51894da774d969aa9469b42d06f97992dd68*/




//Synthetic comment -- diff --git a/src/com/android/phone/GsmUmtsOptions.java b/src/com/android/phone/GsmUmtsOptions.java
//Synthetic comment -- index 64dccd8..abafc44 100644

//Synthetic comment -- @@ -64,8 +64,7 @@
mButtonOperatorSelectionExpand.setEnabled(true);
} else {
log("[CSP] Disabling Operator Selection menu.");
                mButtonOperatorSelectionExpand.setEnabled(false);
}
}
}







