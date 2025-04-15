/*Add lint flag for setting a custom library path

Change-Id:Ie36bcb31be87a7cd16ab1b958c0264ca8b51e385*/




//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index e83601f..541ad4e 100644

//Synthetic comment -- @@ -88,6 +88,7 @@
private static final String ARG_EXITCODE   = "--exitcode";     //$NON-NLS-1$
private static final String ARG_CLASSES    = "--classpath";    //$NON-NLS-1$
private static final String ARG_SOURCES    = "--sources";      //$NON-NLS-1$
    private static final String ARG_LIBRARIES  = "--libraries";    //$NON-NLS-1$

private static final String ARG_NOWARN2    = "--nowarn";       //$NON-NLS-1$
// GCC style flag names for options
//Synthetic comment -- @@ -123,6 +124,7 @@
protected boolean mAllErrors;
protected List<File> mSources;
protected List<File> mClasses;
    protected List<File> mLibraries;

protected Configuration mDefaultConfiguration;
protected IssueRegistry mRegistry;
//Synthetic comment -- @@ -495,6 +497,23 @@
}
mSources.add(input);
}
            } else if (arg.equals(ARG_LIBRARIES)) {
                if (index == args.length - 1) {
                    System.err.println("Missing library folder name");
                    System.exit(ERRNO_INVALIDARGS);
                }
                String paths = args[++index];
                for (String path : LintUtils.splitPath(paths)) {
                    File input = getInArgumentPath(path);
                    if (!input.exists()) {
                        System.err.println("Library " + input + " does not exist.");
                        System.exit(ERRNO_INVALIDARGS);
                    }
                    if (mLibraries == null) {
                        mLibraries = new ArrayList<File>();
                    }
                    mLibraries.add(input);
                }
} else if (arg.startsWith("--")) {
System.err.println("Invalid argument " + arg + "\n");
printUsage(System.err);
//Synthetic comment -- @@ -514,9 +533,10 @@
if (files.size() == 0) {
System.err.println("No files to analyze.");
System.exit(ERRNO_INVALIDARGS);
        } else if (files.size() > 1
                && (mClasses != null || mSources != null || mLibraries != null)) {
            System.err.println("The " + ARG_SOURCES + ", " + ARG_CLASSES + " and "
                    + ARG_LIBRARIES + " arguments can only be used with a single project");
System.exit(ERRNO_INVALIDARGS);
}

//Synthetic comment -- @@ -953,6 +973,8 @@
"the project. Only valid when running lint on a single project.",
ARG_CLASSES + " <dir>", "Add the given folder (or jar file, or path) as a class " +
"directory for the project. Only valid when running lint on a single project.",
            ARG_LIBRARIES + " <dir>", "Add the given folder (or jar file, or path) as a class " +
                    "library for the project. Only valid when running lint on a single project.",

"", "\nExit Status:",
"0",                                 "Success.",
//Synthetic comment -- @@ -1185,7 +1207,7 @@
protected ClassPathInfo getClassPath(@NonNull Project project) {
ClassPathInfo classPath = super.getClassPath(project);

        if (mClasses == null && mSources == null && mLibraries == null) {
return classPath;
}

//Synthetic comment -- @@ -1210,7 +1232,14 @@
} else {
classes = classPath.getClassFolders();
}
            List<File> libraries;
            if (mLibraries != null) {
                libraries = mLibraries;
            } else {
                libraries = classPath.getLibraries();
            }

            info = new ClassPathInfo(sources, classes, libraries);
mProjectInfo.put(project, info);
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java
//Synthetic comment -- index 6b4ca31..6bd8c2d 100644

//Synthetic comment -- @@ -140,7 +140,6 @@
}
}

public void testArguments() throws Exception {
checkDriver(
// Expected output
//Synthetic comment -- @@ -205,6 +204,101 @@
});
}

    public void testNonexistentLibrary() throws Exception {
        checkDriver(
        "",
        "Library foo.jar does not exist.\n",

        // Args
        new String[] {
                "--libraries",
                "foo.jar",
                "prj"

        });
    }

    public void testMultipleProjects() throws Exception {
        File project = getProjectDir(null, "bytecode/classes.jar=>libs/classes.jar");
        checkDriver(
        "",
        "The --sources, --classpath and --libraries arguments can only be used with a single project\n",

        // Args
        new String[] {
                "--libraries",
                new File(project, "libs/classes.jar").getPath(),
                "--disable",
                "LintError",
                project.getPath(),
                project.getPath()

        });
    }

    public void testClassPath() throws Exception {
        File project = getProjectDir(null,
                "apicheck/minsdk1.xml=>AndroidManifest.xml",
                "bytecode/GetterTest.java.txt=>src/test/bytecode/GetterTest.java",
                "bytecode/GetterTest.jar.data=>bin/classes.jar"
        );
        checkDriver(
        "\n" +
        "Scanning MainTest_testClassPath: \n" +
        "src/test/bytecode/GetterTest.java:47: Warning: Calling getter method getFoo1() on self is slower than field access (mFoo1) [FieldGetter]\n" +
        "  getFoo1();\n" +
        "  ~~~~~~~\n" +
        "src/test/bytecode/GetterTest.java:48: Warning: Calling getter method getFoo2() on self is slower than field access (mFoo2) [FieldGetter]\n" +
        "  getFoo2();\n" +
        "  ~~~~~~~\n" +
        "src/test/bytecode/GetterTest.java:52: Warning: Calling getter method isBar1() on self is slower than field access (mBar1) [FieldGetter]\n" +
        "  isBar1();\n" +
        "  ~~~~~~\n" +
        "src/test/bytecode/GetterTest.java:54: Warning: Calling getter method getFoo1() on self is slower than field access (mFoo1) [FieldGetter]\n" +
        "  this.getFoo1();\n" +
        "       ~~~~~~~\n" +
        "src/test/bytecode/GetterTest.java:55: Warning: Calling getter method getFoo2() on self is slower than field access (mFoo2) [FieldGetter]\n" +
        "  this.getFoo2();\n" +
        "       ~~~~~~~\n" +
        "0 errors, 5 warnings\n",
        "",

        // Args
        new String[] {
                "--check",
                "FieldGetter",
                "--classpath",
                new File(project, "bin/classes.jar").getPath(),
                "--disable",
                "LintError",
                project.getPath()
        });
    }

    public void testLibraries() throws Exception {
        File project = getProjectDir(null,
                "apicheck/minsdk1.xml=>AndroidManifest.xml",
                "bytecode/GetterTest.java.txt=>src/test/bytecode/GetterTest.java",
                "bytecode/GetterTest.jar.data=>bin/classes.jar"
        );
        checkDriver(
        "\n" +
        "Scanning MainTest_testLibraries: \n" +
        "\n" +
        "No issues found.\n",
        "",

        // Args
        new String[] {
                "--check",
                "FieldGetter",
                "--libraries",
                new File(project, "bin/classes.jar").getPath(),
                "--disable",
                "LintError",
                project.getPath()
        });
    }

@Override
protected Detector getDetector() {







