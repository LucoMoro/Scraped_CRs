/*Make resource folders customizeable

Until now, lint has hardcoded where the project resources are to be
found: in $project/res.

With the new build system this will no longer necessarily be the case;
there can be multiple resource folders, and the locations might not be
just res/.

Therefore, this CL generalizes lint's handling of resource folders: it
is now provided by the LintClient, and the command line lint tool uses
this to offer a new --resources flag which can be used to set the
resource directory/directories for a project.

Change-Id:Ib51df66cb51f11cdf46c963b0818f793bf3a8094*/




//Synthetic comment -- diff --git a/lint/cli/src/main/java/com/android/tools/lint/Main.java b/lint/cli/src/main/java/com/android/tools/lint/Main.java
//Synthetic comment -- index 673e4d8..c53469e 100644

//Synthetic comment -- @@ -91,6 +91,7 @@
private static final String ARG_EXITCODE   = "--exitcode";     //$NON-NLS-1$
private static final String ARG_CLASSES    = "--classpath";    //$NON-NLS-1$
private static final String ARG_SOURCES    = "--sources";      //$NON-NLS-1$
    private static final String ARG_RESOURCES  = "--resources";    //$NON-NLS-1$
private static final String ARG_LIBRARIES  = "--libraries";    //$NON-NLS-1$

private static final String ARG_NOWARN2    = "--nowarn";       //$NON-NLS-1$
//Synthetic comment -- @@ -128,6 +129,7 @@
protected List<File> mSources;
protected List<File> mClasses;
protected List<File> mLibraries;
    protected List<File> mResources;

protected Configuration mDefaultConfiguration;
protected IssueRegistry mRegistry;
//Synthetic comment -- @@ -500,6 +502,23 @@
}
mSources.add(input);
}
            } else if (arg.equals(ARG_RESOURCES)) {
                if (index == args.length - 1) {
                    System.err.println("Missing resource folder name");
                    System.exit(ERRNO_INVALIDARGS);
                }
                String paths = args[++index];
                for (String path : LintUtils.splitPath(paths)) {
                    File input = getInArgumentPath(path);
                    if (!input.exists()) {
                        System.err.println("Resource folder " + input + " does not exist.");
                        System.exit(ERRNO_INVALIDARGS);
                    }
                    if (mResources == null) {
                        mResources = new ArrayList<File>();
                    }
                    mResources.add(input);
                }
} else if (arg.equals(ARG_LIBRARIES)) {
if (index == args.length - 1) {
System.err.println("Missing library folder name");
//Synthetic comment -- @@ -537,9 +556,11 @@
System.err.println("No files to analyze.");
System.exit(ERRNO_INVALIDARGS);
} else if (files.size() > 1
                && (mClasses != null || mSources != null || mLibraries != null
                    || mResources != null)) {
            System.err.println(String.format(
                  "The %1$s, %2$s, %3$s and %4$s arguments can only be used with a single project",
                  ARG_SOURCES, ARG_CLASSES, ARG_LIBRARIES, ARG_RESOURCES));
System.exit(ERRNO_INVALIDARGS);
}

//Synthetic comment -- @@ -932,6 +953,8 @@
ARG_XML + " <filename>", "Create an XML report instead.",

"", "\nProject Options:",
            ARG_RESOURCES + " <dir>", "Add the given folder (or path) as a resource directory " +
                "for the project. Only valid when running lint on a single project.",
ARG_SOURCES + " <dir>", "Add the given folder (or path) as a source directory for " +
"the project. Only valid when running lint on a single project.",
ARG_CLASSES + " <dir>", "Add the given folder (or jar file, or path) as a class " +
//Synthetic comment -- @@ -1210,6 +1233,16 @@
return info;
}

    @NonNull
    @Override
    public List<File> getResourceFolders(@NonNull Project project) {
        if (mResources == null) {
            return super.getResourceFolders(project);
        }

        return mResources;
    }

/**
* Consult the lint.xml file, but override with the --enable and --disable
* flags supplied on the command line








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/MainTest.java b/lint/cli/src/test/java/com/android/tools/lint/MainTest.java
//Synthetic comment -- index 5a48a47..2ff470e 100644

//Synthetic comment -- @@ -180,7 +180,7 @@
File project = getProjectDir(null, "bytecode/classes.jar=>libs/classes.jar");
checkDriver(
"",
        "The --sources, --classpath, --libraries and --resources arguments can only be used with a single project\n",

// Args
new String[] {
//Synthetic comment -- @@ -194,6 +194,82 @@
});
}

    public void testCustomResourceDirs() throws Exception {
        File project = getProjectDir(null,
                "res/layout/accessibility.xml=>myres1/layout/accessibility1.xml",
                "res/layout/accessibility.xml=>myres2/layout/accessibility1.xml"
        );

        checkDriver(
                "\n"
                + "Scanning MainTest_testCustomResourceDirs: ..\n"
                + "myres1/layout/accessibility1.xml:4: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n"
                + "    <ImageView android:id=\"@+id/android_logo\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n"
                + "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "myres2/layout/accessibility1.xml:4: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n"
                + "    <ImageView android:id=\"@+id/android_logo\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n"
                + "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "myres1/layout/accessibility1.xml:5: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n"
                + "    <ImageButton android:importantForAccessibility=\"yes\" android:id=\"@+id/android_logo2\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n"
                + "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "myres2/layout/accessibility1.xml:5: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n"
                + "    <ImageButton android:importantForAccessibility=\"yes\" android:id=\"@+id/android_logo2\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n"
                + "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "0 errors, 4 warnings\n", // Expected output
                "",

                // Args
                new String[] {
                        "--check",
                        "ContentDescription",
                        "--disable",
                        "LintError",
                        "--resources",
                        new File(project, "myres1").getPath(),
                        "--resources",
                        new File(project, "myres2").getPath(),
                        project.getPath(),
                });
    }

    public void testPathList() throws Exception {
        File project = getProjectDir(null,
                "res/layout/accessibility.xml=>myres1/layout/accessibility1.xml",
                "res/layout/accessibility.xml=>myres2/layout/accessibility1.xml"
        );

        checkDriver(
                "\n"
                + "Scanning MainTest_testPathList: ..\n"
                + "myres1/layout/accessibility1.xml:4: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n"
                + "    <ImageView android:id=\"@+id/android_logo\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n"
                + "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "myres2/layout/accessibility1.xml:4: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n"
                + "    <ImageView android:id=\"@+id/android_logo\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n"
                + "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "myres1/layout/accessibility1.xml:5: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n"
                + "    <ImageButton android:importantForAccessibility=\"yes\" android:id=\"@+id/android_logo2\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n"
                + "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "myres2/layout/accessibility1.xml:5: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n"
                + "    <ImageButton android:importantForAccessibility=\"yes\" android:id=\"@+id/android_logo2\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n"
                + "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                + "0 errors, 4 warnings\n", // Expected output
                "",

                // Args
                new String[] {
                        "--check",
                        "ContentDescription",
                        "--disable",
                        "LintError",
                        "--resources",
                        // Combine two paths with a single separator here
                        new File(project, "myres1").getPath()
                            + ':' + new File(project, "myres2").getPath(),
                        project.getPath(),
                });
    }

public void testClassPath() throws Exception {
File project = getProjectDir(null,
"apicheck/minsdk1.xml=>AndroidManifest.xml",








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 60d0f22..8a43a6c 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -236,20 +237,19 @@
}

/**
     * Returns the resource folders.
*
* @param project the project to look up the resource folder for
     * @return a list of files pointing to the resource folders, possibly empty
*/
    @NonNull
    public List<File> getResourceFolders(@NonNull Project project) {
File res = new File(project.getDir(), RES_FOLDER);
if (res.exists()) {
            return Collections.singletonList(res);
}

        return Collections.emptyList();
}

/**








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index dd268a5..28168a6 100644

//Synthetic comment -- @@ -841,9 +841,11 @@
if (files != null) {
checkIndividualResources(project, main, xmlDetectors, files);
} else {
                        List<File> resourceFolders = project.getResourceFolders();
                        if (!resourceFolders.isEmpty() && !xmlDetectors.isEmpty()) {
                            for (File res : resourceFolders) {
                                checkResFolder(project, main, res, xmlDetectors);
                            }
}
}
}
//Synthetic comment -- @@ -1686,9 +1688,9 @@
}

@Override
        @NonNull
        public List<File> getResourceFolders(@NonNull Project project) {
            return mDelegate.getResourceFolders(project);
}

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index df27b2f..67c3b16 100644

//Synthetic comment -- @@ -353,20 +353,20 @@
* @return a file pointing to the resource folder, or null if the project
*         does not contain any resources
*/
    @NonNull
    public List<File> getResourceFolders() {
        List<File> folders = mClient.getResourceFolders(this);

        if (folders.size() == 1 && isAospFrameworksProject(mDir)) {
// No manifest file for this project: just init the manifest values here
mMinSdk = mTargetSdk = SdkConstants.HIGHEST_KNOWN_API;
            File folder = new File(folders.get(0), RES_FOLDER);
if (!folder.exists()) {
                folders = Collections.emptyList();
}
}

        return folders;
}

/**








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 4d8b3a4..7f26c96 100644

//Synthetic comment -- @@ -386,8 +386,8 @@
}

private void checkResourceFolder(Context context, @NonNull Project project) {
        List<File> resourceFolders = project.getResourceFolders();
        for (File res : resourceFolders) {
File[] folders = res.listFiles();
if (folders != null) {
boolean checkFolders = context.isEnabled(ICON_DENSITIES)








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateKeyDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateKeyDetector.java
//Synthetic comment -- index b1e848b..f957331 100644

//Synthetic comment -- @@ -105,7 +105,9 @@
Project project = context.getProject();
File projectFolder = project.getDir();

        for (File res : project.getResourceFolders()) {
            checkFolder(context, res);
        }
checkFolder(context, new File(projectFolder, "assets"));

for (File srcFolder : project.getJavaSourceFolders()) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 436f420..2a2d77c 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.google.common.collect.Lists;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -260,16 +261,19 @@
if (type != null && LintUtils.isFileBasedResourceType(type)) {
String name = resource.substring(secondDot + 1);

                        List<File> folders = Lists.newArrayList();
                        List<File> resourceFolders = context.getProject().getResourceFolders();
                        for (File res : resourceFolders) {
                            File[] f = res.listFiles();
                            if (f != null) {
                                folders.addAll(Arrays.asList(f));
                            }
}
if (folders != null) {
// Process folders in alphabetical order such that we process
// based folders first: we want the locations in base folder
// order
                            Collections.sort(folders, new Comparator<File>() {
@Override
public int compare(File file1, File file2) {
return file1.getName().compareTo(file2.getName());







