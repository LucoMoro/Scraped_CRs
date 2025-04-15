/*Call From SIM

When trying to call a number on the SIM, for example by dialing '1#',
Android code does not make a distinction between order and position
on the SIM. The contacts are sorted, which is wrong, and any empty
positions are removed (positions can be empty on a SIM). This means
that trying to make a phone call this way will lead to users dialing
the wrong number.

An API to load SIM positions has been added to solve the issue.

Change-Id:Ia6a921be75f0f969998745323acc60e0ae8d37e3*/




//Synthetic comment -- diff --git a/src/com/android/contacts/SpecialCharSequenceMgr.java b/src/com/android/contacts/SpecialCharSequenceMgr.java
//Synthetic comment -- index 140e7d4..a49ada4 100644

//Synthetic comment -- @@ -158,7 +158,7 @@
sc.progressDialog.show();

// run the query.
                handler.startQuery(ADN_QUERY_TOKEN, sc, Uri.parse("content://icc/adn/" + index),
new String[]{ADN_PHONE_NUMBER_COLUMN_NAME}, null, null, null);
return true;
} catch (NumberFormatException ex) {
//Synthetic comment -- @@ -314,21 +314,24 @@
// get the EditText to update or see if the request was cancelled.
EditText text = sc.getTextField();

            // if the textview is valid, and the cursor is valid and positionable
// on the Nth number, then we update the text field and display a
// toast indicating the caller name.
            if ((c != null) && (text != null) && (c.moveToPosition(0))) {
String name = c.getString(c.getColumnIndexOrThrow(ADN_NAME_COLUMN_NAME));
String number = c.getString(c.getColumnIndexOrThrow(ADN_PHONE_NUMBER_COLUMN_NAME));

                // There might be empty positions on the SIM or contacts without number
                if (number.length() > 0) {
                    // fill the text in.
                    text.getText().replace(0, 0, number);

                    // display the name as a toast
                    Context context = sc.progressDialog.getContext();
                    name = context.getString(R.string.menu_callNumber, name);
                    Toast.makeText(context, name, Toast.LENGTH_SHORT)
                        .show();
                }
}
}
}







