/*Fix a DEAD-LOOP in updating the recipients

It will be in a dead-loop between updateContact and
its callback method onUpdate() in ComposeMessageActivity
if we didn't wait the quering of contacts.

Also we don't need to reset the number which read from
RecipientEditor to the number of the contacts queried,
since it will be set a wrong number ends with the special
characters such as '*' to the contacts which has queried
and already be saved in the phonebook.

Change-Id:If89e4af54db38ad06e8d7742c549fcdd25ccb661Signed-off-by: Xingrun Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 4fa1878..42d406c 100644

//Synthetic comment -- @@ -3842,7 +3842,8 @@
mMessageListItemHandler.post(new Runnable() {
public void run() {
ContactList recipients = isRecipientsEditorVisible() ?
                        mRecipientsEditor.constructContactsFromInput(false) : getRecipients();
if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
log("[CMA] onUpdate contact updated: " + updated);
log("[CMA] onUpdate recipients: " + recipients);








//Synthetic comment -- diff --git a/src/com/android/mms/ui/RecipientsEditor.java b/src/com/android/mms/ui/RecipientsEditor.java
//Synthetic comment -- index de997be..c2b259e 100644

//Synthetic comment -- @@ -128,7 +128,9 @@
ContactList list = new ContactList();
for (String number : numbers) {
Contact contact = Contact.get(number, blocking);
            contact.setNumber(number);
list.add(contact);
}
return list;







