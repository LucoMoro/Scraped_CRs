/*Don't abort AAPT parsing in the presence of the XML Bad Block message

For some types of XML errors, AAPT will emit an error message like
this:
W/ResourceType(12345): Bad XML block: no root element node found

This is not an error message our AAPT parser recognizes, and the way
it behaves is that if it encounters an unknown error message, it
aborts parsing (so no further error messages will get Eclipse
markers), and then the full set of errors are dumped to the console
instead.

I ran into this error when I had an unknown attribute in a res/menu/
file (android:showAsAction="never" compiled with Froyo).

Just dumping the error to the console has the unfortunate side effect
of creating no markers in the Problems view, so I was getting Java
compilation errors for a missing R class.

This changeset simply recognizes the above error message, and ignores
it (unless it's the only message). The error isn't very useful in
isolation because it does not name which file it applies to, and some
quick Google searches on the error message reveals that it's usually
displayed along with the real culprit following the error message.

Longer term I think we should stop aborting parsing when we see these
messages, and process all the messages and add them to the Problems
view faithfully, but for now this is a simple and low risk fix.

Change-Id:Ic003fcaf94be76866a7473d9b4002078b823ba27*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index 5742888..1f17fb7 100644

//Synthetic comment -- @@ -202,6 +202,9 @@
private final static Pattern sPattern9Line1 = Pattern.compile(
"^Invalid configuration: (.+)$"); //$NON-NLS-1$

/**
* Parse the output of aapt and mark the incorrect file with error markers
*
//Synthetic comment -- @@ -462,7 +465,19 @@
continue;
}

            // invalid line format, flag as error, and bail
return true;
}








