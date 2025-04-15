/*ADT Manifest: fix XML attribute completion.

XML completion wasn't done properly for attributes for element
that had a dash (-) in them.

Change-Id:I433f3711e1991246d099246847c7204e04ae3e40*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index ad4599d..1e238ae 100644

//Synthetic comment -- @@ -75,7 +75,7 @@
"^ *[a-zA-Z_:]+ *= *(?:\"[^<\"]*\"|'[^<']*')");  //$NON-NLS-1$

/** Regexp to detect an element tag name */
    private static Pattern sFirstElementWord = Pattern.compile("^[a-zA-Z0-9_:-]+"); //$NON-NLS-1$

/** Regexp to detect whitespace */
private static Pattern sWhitespace = Pattern.compile("\\s+"); //$NON-NLS-1$







