/*Defect 8195:Contacts organization Info not displayed with out inputting
position

Change-Id:Iee823caa592ad147506883706b97aee29b2b966c*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ViewContactActivity.java b/src/com/android/contacts/ViewContactActivity.java
//Synthetic comment -- index ca3c08a..ecf268f 100644

//Synthetic comment -- @@ -884,7 +884,7 @@
}
mImEntries.add(entry);
} else if ((Organization.CONTENT_ITEM_TYPE.equals(mimeType)
                            || Nickname.CONTENT_ITEM_TYPE.equals(mimeType)) && hasData) {
// Build organization and note entries
entry.uri = null;
mOrganizationEntries.add(entry);







