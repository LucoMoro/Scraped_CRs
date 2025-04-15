/*The rendering target mode should be preserved across IDE sessions

This changeset fixes the bug where the rendering target is not
preserved across IDE sessions the way the other configuration settings
seem to be.

Change-Id:I56fc82b1de18c162f62e80d1ba96b54d697b2607*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 386fd5f..e83df64 100644

//Synthetic comment -- @@ -202,6 +202,8 @@
DockMode dock = DockMode.NONE;
/** night mode. Guaranteed to be non null */
NightMode night = NightMode.NOTNIGHT;

String getData() {
StringBuilder sb = new StringBuilder();
//Synthetic comment -- @@ -225,6 +227,10 @@
sb.append(SEP);
sb.append(night.getResourceValue());
sb.append(SEP);
}

return sb.toString();
//Synthetic comment -- @@ -232,7 +238,7 @@

boolean setData(String data) {
String[] values = data.split(SEP);
            if (values.length == 6) {
for (LayoutDevice d : mDeviceList) {
if (d.getName().equals(values[0])) {
device = d;
//Synthetic comment -- @@ -261,6 +267,10 @@
night = NightMode.NOTNIGHT;
}

return true;
}
}
//Synthetic comment -- @@ -294,6 +304,11 @@
sb.append(night.getResourceValue());
sb.append(SEP);

return sb.toString();
}
}
//Synthetic comment -- @@ -595,7 +610,7 @@

/**
* Sets the reference to the file being edited.
     * <p/>The UI is intialized in {@link #onXmlModelLoaded()} which is called as the XML model is
* loaded (or reloaded as the SDK/target changes).
*
* @param file the file being opened
//Synthetic comment -- @@ -790,11 +805,15 @@

mDockCombo.select(DockMode.getIndex(mState.dock));
mNightCombo.select(NightMode.getIndex(mState.night));
} else {
findAndSetCompatibleConfig(false /*favorCurrentConfig*/);

mDockCombo.select(0);
mNightCombo.select(0);
}

// update the string showing the config value
//Synthetic comment -- @@ -1042,6 +1061,11 @@
if (index != -1) {
mState.night = NightMode.getByIndex(index);
}
}
}

//Synthetic comment -- @@ -1820,6 +1844,39 @@
return false;
}

private void onThemeChange() {
saveState();








