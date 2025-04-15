/*Fix issue 1167: Contacts: adding new Organization entry erases
"position"s fields of othershttp://code.google.com/p/android/issues/detail?id=1167The text in data2 textview is not saved before.*/




//Synthetic comment -- diff --git a/src/com/android/contacts/EditContactActivity.java b/src/com/android/contacts/EditContactActivity.java
//Synthetic comment -- index c301473..d57ebc1 100644

//Synthetic comment -- @@ -1424,6 +1424,13 @@
if (!TextUtils.isEmpty(enteredText)) {
entry.data = enteredText;
}
            TextView data2 = (TextView) entry.view.findViewById(R.id.data2);
            if (data2 != null) {
                String enteredText2 = data2.getText().toString();
                if (!TextUtils.isEmpty(enteredText2)) {
                    entry.data2 = enteredText2;
                }
            }
}

// Build a new view
//Synthetic comment -- @@ -1458,6 +1465,9 @@
if (entry.syncDataWithView) {
// If there is already data entered don't overwrite it
data.setText(entry.data);
                if (data2 != null) {
                    data2.setText(entry.data2);
                }
} else {
fillViewData(entry);
}







