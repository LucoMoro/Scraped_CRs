/*No need to set the number for the contact in DB

No nned to reset the number for the contacts who
exits in the database, since the NUMBER<number + "*">
user inputed which ends with the special characters
such as "*" can be searched as a contact, so the number
will be reset to an incorrect one.

Change-Id:I07dcf30b08b4cc8254b150a72b81c184c9d56452Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/RecipientsEditor.java b/src/com/android/mms/ui/RecipientsEditor.java
//Synthetic comment -- index de997be..4b5c2ba 100644

//Synthetic comment -- @@ -128,7 +128,14 @@
ContactList list = new ContactList();
for (String number : numbers) {
Contact contact = Contact.get(number, blocking);
            // No need to reset the number of the contact who exists in
            // the database, since the NUMBER<number + "*"> user inputed
            // which ends with the special characters such as '*' can be 
            // searched as a contact, so the number will be reset to an
            // incorrect one.
            if (!contact.existsInDatabase()) {
                contact.setNumber(number);
            }
list.add(contact);
}
return list;







