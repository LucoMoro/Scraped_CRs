/*Fixes to previous commit*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index 3eaf588..327737e 100644

//Synthetic comment -- @@ -371,8 +371,7 @@
ContactInfoCache cache = ContactInfoCache.getInstance();
ContactInfoCache.CacheEntry info;
if (Mms.isEmailAddress(address)) {
            info = cache.getContactInfoForEmailAddress(getApplicationContext(), address,
                    true /* allow query */);
} else {
info = cache.getContactInfo(this, address);
}







