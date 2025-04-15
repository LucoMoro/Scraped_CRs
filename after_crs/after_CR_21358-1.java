/*Removing unused array list; it's forgotten here.

By the way, it seems so funny together with that comment right above. :-P

Change-Id:I33db9b46380e2a0af6c2a3a0e307dafde1543232*/




//Synthetic comment -- diff --git a/core/java/android/pim/vcard/VCardComposer.java b/core/java/android/pim/vcard/VCardComposer.java
//Synthetic comment -- index 193cf1e..ee5c860 100644

//Synthetic comment -- @@ -500,8 +500,6 @@

// This function does not care the OutOfMemoryError on the handler side :-P
if (mCareHandlerErrors) {
for (OneEntryHandler handler : mHandlerList) {
if (!handler.onEntryCreated(vcard)) {
return false;







