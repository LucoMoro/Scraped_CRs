/*31340: ADT default for opening editors in textual mode

When you reopen a file you've already opened in the past, ADT will
open the file on the same page (graphical or XML) that you left it at.

However, new files are always opened graphically. This changeset will
change this such that if you manually switch to XML, new files opened
from that point open in XML, and if you switch to graphical mode, new
files are opened in graphical mode.

This will hopefully help those users who prefer to work in XML mode
without negatively impacting those who prefer to work in graphical
mode.

Change-Id:Ib229b22c31899c1cd9520e6ab48f5902d2782844*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 50b5505..ea3f30b 100644

//Synthetic comment -- @@ -427,6 +427,8 @@
// first page rather than crash the editor load. Logging the error is enough.
AdtPlugin.log(e, "Selecting page '%s' in AndroidXmlEditor failed", defaultPageId);
}
}
}

//Synthetic comment -- @@ -483,6 +485,8 @@
// ignore
}
}
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java
//Synthetic comment -- index 380c925..3020851 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
public final static String PREFS_MONITOR_DENSITY = AdtPlugin.PLUGIN_ID + ".monitorDensity"; //$NON-NLS-1$

public final static String PREFS_FORMAT_GUI_XML = AdtPlugin.PLUGIN_ID + ".formatXml"; //$NON-NLS-1$
public final static String PREFS_USE_CUSTOM_XML_FORMATTER = AdtPlugin.PLUGIN_ID + ".androidForm"; //$NON-NLS-1$

public final static String PREFS_PALETTE_MODE = AdtPlugin.PLUGIN_ID + ".palette"; //$NON-NLS-1$
//Synthetic comment -- @@ -86,6 +87,7 @@
private String mPalette;

private boolean mFormatGuiXml;
private boolean mCustomXmlFormatter;
private boolean mUseEclipseIndent;
private boolean mRemoveEmptyLines;
//Synthetic comment -- @@ -195,6 +197,10 @@
mFormatGuiXml = mStore.getBoolean(PREFS_FORMAT_GUI_XML);
}

if (property == null || PREFS_USE_CUSTOM_XML_FORMATTER.equals(property)) {
mCustomXmlFormatter = mStore.getBoolean(PREFS_USE_CUSTOM_XML_FORMATTER);
}
//Synthetic comment -- @@ -461,4 +467,23 @@
AdtPlugin.log(e, "Get default debug keystore path failed"); //$NON-NLS-1$
}
}
}







