/*Look at mNumber instead of mName when deciding whether to add or edit a fixed dialling list entry

3GPP TS 31.102 (USIM application) v9.1.0 states in chapter 4.2.24
EFFDN (Fixed Dialling Number) that the Dialling Number field is
mandatory whereas the Alpha Identifier is optional. The current
implementation of the user interface for editing the fixed
dialling list blocks creation and deletion list entries without a
name (corresponding to the Alpha Identifier) but instead permits
entries without a number. This is incompatible with the USIM
specification and implementation. This contribution corrects the
implementation in line with the USIM specification.

Change-Id:I33aa615854d2a2972503caf36b36dc876c137619*/




//Synthetic comment -- diff --git a/src/com/android/phone/DeleteFdnContactScreen.java b/src/com/android/phone/DeleteFdnContactScreen.java
//Synthetic comment -- index 98b5e64..b030d87 100644

//Synthetic comment -- @@ -93,16 +93,20 @@
mName =  intent.getStringExtra(INTENT_EXTRA_NAME);
mNumber =  intent.getStringExtra(INTENT_EXTRA_NUMBER);

        if (TextUtils.isEmpty(mNumber)) {
finish();
}
}

private void deleteContact() {
StringBuilder buf = new StringBuilder();
        if (TextUtils.isEmpty(mName)) {
            buf.append("number='");
        } else {
            buf.append("tag='");
            buf.append(mName);
            buf.append("' AND number='");
        }
buf.append(mNumber);
buf.append("' AND pin2='");
buf.append(mPin2);








//Synthetic comment -- diff --git a/src/com/android/phone/EditFdnContactScreen.java b/src/com/android/phone/EditFdnContactScreen.java
//Synthetic comment -- index ccd1448..d164a3f 100644

//Synthetic comment -- @@ -205,9 +205,7 @@
mName =  intent.getStringExtra(INTENT_EXTRA_NAME);
mNumber =  intent.getStringExtra(INTENT_EXTRA_NUMBER);

        mAddContact = TextUtils.isEmpty(mNumber);
}

/**







