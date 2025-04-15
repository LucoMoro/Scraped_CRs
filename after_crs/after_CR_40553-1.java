/*Look for extra templates under <vendor>/<package>

The template wizard looks for additional templates
delivered separate from the tools in the extras/
folder. It was looking in extras/templates/, but it
should really look under each package directory under
each vendor directory within the extras folder and
merge it all together.

Change-Id:I6cdfe2b3d5206821ea8ef2e06027b527e978b2f5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index d6ca12a..af02d89 100644

//Synthetic comment -- @@ -1067,4 +1067,24 @@

return false;
}

    /**
     * Lists the files of the given directory and returns them as an array which
     * is never null. This simplifies processing file listings from for each
     * loops since {@link File#listFiles} can return null. This method simply
     * wraps it and makes sure it returns an empty array instead if necessary.
     *
     * @param dir the directory to list
     * @return the children, or empty if it has no children, is not a directory,
     *         etc.
     */
    @NonNull
    public static File[] listFiles(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            return files;
        } else {
            return new File[0];
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateManager.java
//Synthetic comment -- index c183ce3..0de1555 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.google.common.base.Charsets;
//Synthetic comment -- @@ -59,17 +60,36 @@
}

/** @return the root folder containing extra templates */
    @NonNull
    public static List<File> getExtraTemplateRootFolders() {
        List<File> folders = new ArrayList<File>();
String location = AdtPrefs.getPrefs().getOsSdkFolder();
if (location != null) {
            File extras = new File(location, FD_EXTRAS);
            if (extras.isDirectory()) {
                for (File vendor : AdtUtils.listFiles(extras)) {
                    if (!vendor.isDirectory()) {
                        continue;
                    }
                    for (File pkg : AdtUtils.listFiles(vendor)) {
                        if (pkg.isDirectory()) {
                            File folder = new File(pkg, FD_TEMPLATES);
                            if (folder.isDirectory()) {
                                folders.add(folder);
                            }
                        }
                    }
                }

                // Legacy
                File folder = new File(extras, FD_TEMPLATES);
                if (folder.isDirectory()) {
                    folders.add(folder);
                }
}
}

        return folders;
}

/**
//Synthetic comment -- @@ -140,9 +160,8 @@
}

// Add in templates from extras/ as well.
        for (File extra : getExtraTemplateRootFolders()) {
            File[] files = new File(extra, folder).listFiles();
if (files != null) {
for (File file : files) {
if (file.isDirectory()) {







