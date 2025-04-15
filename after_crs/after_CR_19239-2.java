/*The rendering target mode should be preserved across IDE sessions

This changeset fixes the bug where the rendering target is not
preserved across IDE sessions the way the other configuration settings
seem to be.

Change-Id:I56fc82b1de18c162f62e80d1ba96b54d697b2607*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 386fd5f..76eeb7e 100644

//Synthetic comment -- @@ -202,6 +202,8 @@
DockMode dock = DockMode.NONE;
/** night mode. Guaranteed to be non null */
NightMode night = NightMode.NOTNIGHT;
        /** the version being targeted for rendering */
        IAndroidTarget target;

String getData() {
StringBuilder sb = new StringBuilder();
//Synthetic comment -- @@ -225,6 +227,10 @@
sb.append(SEP);
sb.append(night.getResourceValue());
sb.append(SEP);
                if (target != null) {
                    sb.append(targetToString(target));
                    sb.append(SEP);
                }
}

return sb.toString();
//Synthetic comment -- @@ -232,7 +238,7 @@

boolean setData(String data) {
String[] values = data.split(SEP);
            if (values.length == 6 || values.length == 7) {
for (LayoutDevice d : mDeviceList) {
if (d.getName().equals(values[0])) {
device = d;
//Synthetic comment -- @@ -261,6 +267,10 @@
night = NightMode.NOTNIGHT;
}

                            if (values.length == 7 && mTargetList != null) {
                                target = stringToTarget(values[6]);
                            }

return true;
}
}
//Synthetic comment -- @@ -294,8 +304,45 @@
sb.append(night.getResourceValue());
sb.append(SEP);

            if (target != null) {
                sb.append(targetToString(target));
                sb.append(SEP);
            }

return sb.toString();
}

        /**
         * Returns a String id to represent an {@link IAndroidTarget} which can be translated
         * back to an {@link IAndroidTarget} by the matching {@link #stringToTarget}. The id
         * will never contain the {@link #SEP} character.
         *
         * @param target the target to return an id for
         * @return an id for the given target; never null
         */
        private String targetToString(IAndroidTarget target) {
            return target.getFullName().replace(SEP, "");  //$NON-NLS-1$
        }

        /**
         * Returns an {@link IAndroidTarget} that corresponds to the given id that was
         * originally returned by {@link #targetToString}. May be null, if the platform is no
         * longer available, or if the platform list has not yet been initialized.
         *
         * @param id the id that corresponds to the desired platform
         * @return an {@link IAndroidTarget} that matches the given id, or null
         */
        private IAndroidTarget stringToTarget(String id) {
            if (mTargetList != null && mTargetList.size() > 0) {
                for (IAndroidTarget target : mTargetList) {
                    if (id.equals(targetToString(target))) {
                        return target;
                    }
                }
            }

            return null;
        }
}

/**
//Synthetic comment -- @@ -595,7 +642,7 @@

/**
* Sets the reference to the file being edited.
     * <p/>The UI is initialized in {@link #onXmlModelLoaded()} which is called as the XML model is
* loaded (or reloaded as the SDK/target changes).
*
* @param file the file being opened
//Synthetic comment -- @@ -790,11 +837,15 @@

mDockCombo.select(DockMode.getIndex(mState.dock));
mNightCombo.select(NightMode.getIndex(mState.night));
                        mTargetCombo.select(mTargetList.indexOf(mState.target));
} else {
findAndSetCompatibleConfig(false /*favorCurrentConfig*/);

mDockCombo.select(0);
mNightCombo.select(0);
                        // We don't want the -first- combobox item, we want the
                        // default one which is sometimes a different index
                        //mTargetCombo.select(0);
}

// update the string showing the config value
//Synthetic comment -- @@ -1042,6 +1093,11 @@
if (index != -1) {
mState.night = NightMode.getByIndex(index);
}

            index = mTargetCombo.getSelectionIndex();
            if (index != -1) {
                mState.target = mTargetList.get(index);
            }
}
}

//Synthetic comment -- @@ -1050,7 +1106,7 @@
*/
public void storeState() {
try {
            QualifiedName qname = new QualifiedName(AdtPlugin.PLUGIN_ID, CONFIG_STATE);
mEditedFile.setPersistentProperty(qname, mState.getData());
} catch (CoreException e) {
// pass







