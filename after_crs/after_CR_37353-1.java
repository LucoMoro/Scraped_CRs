/*Eclipse 4.x fix: Don't attempt to get display too early

Also fix a couple of potential NPEs.

Change-Id:I80d6b625d672ad2e7b96f2fce311aa4347a45a33*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 10cd093..48cebee 100644

//Synthetic comment -- @@ -211,8 +211,6 @@
public void start(BundleContext context) throws Exception {
super.start(context);

// set the default android console.
mAndroidConsole = new MessageConsole("Android", null); //$NON-NLS-1$
ConsolePlugin.getDefault().getConsoleManager().addConsoles(
//Synthetic comment -- @@ -221,17 +219,6 @@
// get the stream to write in the android console.
mAndroidConsoleStream = mAndroidConsole.newMessageStream();
mAndroidConsoleErrorStream = mAndroidConsole.newMessageStream();

// get the eclipse store
IPreferenceStore eclipseStore = getPreferenceStore();
//Synthetic comment -- @@ -303,12 +290,32 @@

DesignerPlugin.dispose();

        if (mRed != null) {
            mRed.dispose();
            mRed = null;
        }

synchronized (AdtPlugin.class) {
sPlugin = null;
}
}

    /** Called when the workbench has been started */
    public void workbenchStarted() {
        Display display = getDisplay();
        mRed = new Color(display, 0xFF, 0x00, 0x00);

        // because this can be run, in some cases, by a non ui thread, and because
        // changing the console properties update the ui, we need to make this change
        // in the ui thread.
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                mAndroidConsoleErrorStream.setColor(mRed);
            }
        });
    }

/**
* Returns the shared instance
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlProperty.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlProperty.java
//Synthetic comment -- index c846ea1..6591575 100644

//Synthetic comment -- @@ -167,6 +167,9 @@
Object s = null;
try {
Element element = (Element) mNode.getXmlNode();
            if (element == null) {
                return false;
            }
String name = mDescriptor.getXmlLocalName();
String uri = mDescriptor.getNamespaceUri();
if (uri != null) {
//Synthetic comment -- @@ -182,6 +185,9 @@

public String getStringValue() {
Element element = (Element) mNode.getXmlNode();
        if (element == null) {
            return null;
        }
String name = mDescriptor.getXmlLocalName();
String uri = mDescriptor.getNamespaceUri();
Attr attr;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/welcome/AdtStartup.java
//Synthetic comment -- index f6dc870..0fcf102 100644

//Synthetic comment -- @@ -68,6 +68,8 @@
}

initializeWindowCoordinator();

        AdtPlugin.getDefault().workbenchStarted();
}

private boolean isFirstTime() {







