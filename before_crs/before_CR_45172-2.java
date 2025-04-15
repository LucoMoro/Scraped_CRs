/*Only format Android projects with our formatter

When formatting, peek at the model base location, if known, and if a
corresponding file is found which isn't in an Android project, use the
builtin Eclipse formatter instead. This should reduce the impact of
the Android XML editor registering itself for the xml content type and
affecting unrelated projects, at least in terms of formatting (which
is more severe than say additional completion options or extra go to
declaration targets.)

See issue 38747: Android Tools XML formatter with pom.xml

Change-Id:Ifa0e8527b7020c2ca4f477d6e0be6397d2dbdeff*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java
//Synthetic comment -- index 35735dc..7c7b2cb 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.formatting;

import static com.android.ide.eclipse.adt.internal.editors.AndroidXmlAutoEditStrategy.findLineStart;
import static com.android.ide.eclipse.adt.internal.editors.AndroidXmlAutoEditStrategy.findTextStart;
import static com.android.ide.eclipse.adt.internal.editors.color.ColorDescriptors.SELECTOR_TAG;
//Synthetic comment -- @@ -32,8 +33,14 @@
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.resources.ResourceType;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
//Synthetic comment -- @@ -84,6 +91,8 @@
private final Queue<IDocument> mDocuments = new LinkedList<IDocument>();
private final LinkedList<TypedPosition> mPartitions = new LinkedList<TypedPosition>();
private ContextBasedFormattingStrategy mDelegate = null;

/**
* Creates a new {@link AndroidXmlFormattingStrategy}
//Synthetic comment -- @@ -92,7 +101,8 @@
}

private ContextBasedFormattingStrategy getDelegate() {
        if (!AdtPrefs.getPrefs().getUseCustomXmlFormatter()) {
if (mDelegate == null) {
mDelegate = new XMLFormattingStrategy();
}
//Synthetic comment -- @@ -159,7 +169,7 @@
* @param length the length of the text range to be formatted
* @return a {@link TextEdit} which edits the model into a formatted document
*/
    public static TextEdit format(IStructuredModel model, int start, int length) {
int end = start + length;

TextEdit edit = new MultiTextEdit();
//Synthetic comment -- @@ -545,6 +555,50 @@
return style;
}

@Override
public void formatterStarts(final IFormattingContext context) {
// Use Eclipse XML formatter instead?
//Synthetic comment -- @@ -567,6 +621,15 @@
IDocument document = (IDocument) context.getProperty(CONTEXT_MEDIUM);
mPartitions.offer(partition);
mDocuments.offer(document);
}

@Override







