/*Interpret keypresses as numbers on ffa Phone app

Keypresses from the hard keyboard on ffa were being interpreted as
letters and not numbers in Phone app since InputType was set to null.
Changed keyboard to phone mode so that keypresses in Phone app are
interpreted as numbers belonging to Phone class

Change-Id:I82dbc59d529975370ca2a8b976ad753c25a5d8a0*/
//Synthetic comment -- diff --git a/src/com/android/contacts/TwelveKeyDialer.java b/src/com/android/contacts/TwelveKeyDialer.java
//Synthetic comment -- index 5219d99..647d9ea 100644

//Synthetic comment -- @@ -237,15 +237,7 @@

mDialpad = findViewById(R.id.dialpad);  // This is null in landscape mode.

        // In landscape we put the keyboard in phone mode.
        // In portrait we prevent the soft keyboard to show since the
        // dialpad acts as one already.
        if (null == mDialpad) {
            mDigits.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        } else {
            mDigits.setInputType(android.text.InputType.TYPE_NULL);
        }

// Set up the "dialpad chooser" UI; see showDialpadChooser().
mDialpadChooser = (ListView) findViewById(R.id.dialpadChooser);
mDialpadChooser.setOnItemClickListener(this);







