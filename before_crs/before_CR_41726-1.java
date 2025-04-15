/*Telephony: Fix alpha tag being overwritten by number

When user tries to update fdn list entry suddenly alpha
tag gets set to the number of the entry

Change-Id:I0b1a7487dfe717d9b8b982be17903c3135ca659b*/
//Synthetic comment -- diff --git a/src/com/android/phone/EditFdnContactScreen.java b/src/com/android/phone/EditFdnContactScreen.java
//Synthetic comment -- index cdeae19..36a6b79 100644

//Synthetic comment -- @@ -295,6 +295,7 @@
private void updateContact() {
if (DBG) log("updateContact");

final String number = PhoneNumberUtils.convertAndStrip(getNumberFromTextField());

if (!isValidNumber(number)) {
//Synthetic comment -- @@ -306,7 +307,7 @@
ContentValues bundle = new ContentValues();
bundle.put("tag", mName);
bundle.put("number", mNumber);
        bundle.put("newTag", number);
bundle.put("newNumber", number);
bundle.put("pin2", mPin2);








