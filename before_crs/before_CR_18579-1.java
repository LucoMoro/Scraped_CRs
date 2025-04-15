/*Contacts: Close cursor object after use

In callOrSmsContact function, phonesCursor is never closed, which
causes cursor leaks. Close the phonesCursor after its use.

Change-Id:Ia578bc066652e654696f10b94ff5462f6ba436b9*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index 0d2c7eb..ae29072 100644

//Synthetic comment -- @@ -2662,6 +2662,10 @@
ContactsUtils.initiateCall(this, phone);
}
}
}
}
return true;







