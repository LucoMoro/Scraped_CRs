/*Add lint launcher icon shape check

Change-Id:I789e74b667afbaea4089a62060b7138dadd8da6b*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index e521803..a211029 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 129;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -150,6 +150,7 @@
issues.add(IconDetector.ICON_EXTENSION);
issues.add(IconDetector.ICON_COLORS);
issues.add(IconDetector.ICON_XML_AND_PNG);
        issues.add(IconDetector.ICON_LAUNCHER_SHAPE);
issues.add(TypographyDetector.DASHES);
issues.add(TypographyDetector.QUOTES);
issues.add(TypographyDetector.FRACTIONS);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index eb87a5d..f435810 100644

//Synthetic comment -- @@ -333,6 +333,26 @@
ICON_TYPE_SCOPE).setMoreInfo(
"http://developer.android.com/design/style/iconography.html"); //$NON-NLS-1$

    /** Wrong launcher icon shape */
    public static final Issue ICON_LAUNCHER_SHAPE = Issue.create(
            "IconLauncherShape", //$NON-NLS-1$
            "Checks that launcher icons follow the recommended visual style",

            "According to the Android Design Guide " +
            "(http://developer.android.com/design/style/iconography.html) " +
            "your launcher icons should \"use a distinct silhouette\", " +
            "a \"three-dimensional, front view, with a slight perspective as if viewed " +
            "from above, so that users perceive some depth.\"\n" +
            "\n" +
            "The unique silhouette implies that your launcher icon should not be a filled " +
            "square.",
            Category.ICONS,
            6,
            Severity.WARNING,
            IconDetector.class,
            ICON_TYPE_SCOPE).setMoreInfo(
                "http://developer.android.com/design/style/iconography.html"); //$NON-NLS-1$

/** Constructs a new {@link IconDetector} check */
public IconDetector() {
}
//Synthetic comment -- @@ -1113,6 +1133,25 @@
return file.getName().contains("-nodpi");
}

    private Map<File, BufferedImage> mImageCache;

    @Nullable
    private BufferedImage getImage(@Nullable File file) throws IOException {
        if (mImageCache == null) {
            mImageCache = Maps.newHashMap();
        } else {
            BufferedImage image = mImageCache.get(file);
            if (image != null) {
                return image;
            }
        }

        BufferedImage image = ImageIO.read(file);
        mImageCache.put(file, image);

        return image;
    }

private void checkDrawableDir(Context context, File folder, File[] files,
Map<File, Dimension> pixelSizes, Map<File, Long> fileSizes) {
if (folder.getName().equals(DRAWABLE_FOLDER)
//Synthetic comment -- @@ -1179,6 +1218,18 @@
}
}

        if (context.isEnabled(ICON_LAUNCHER_SHAPE)) {
            // Look up launcher icon name
            for (File file : files) {
                String name = file.getName();
                if (isLauncherIcon(name)
                        && !endsWith(name, DOT_XML)
                        && !endsWith(name, DOT_9PNG)) {
                    checkLauncherShape(context, file);
                }
            }
        }

// Check icon sizes
if (context.isEnabled(ICON_EXPECTED_SIZE)) {
checkExpectedSizes(context, folder, files);
//Synthetic comment -- @@ -1205,6 +1256,36 @@
}
}
}

        mImageCache = null;
    }

    /**
     * Check that launcher icons do not fill every pixel in the image
     */
    private void checkLauncherShape(Context context, File file) {
        try {
            BufferedImage image = getImage(file);
            if (image != null) {
                // TODO: see if the shape is rectangular but inset from outer rectangle; if so
                // that's probably not right either!
                for (int y = 0, height = image.getHeight(); y < height; y++) {
                    for (int x = 0, width = image.getWidth(); x < width; x++) {
                        int rgb = image.getRGB(x, y);
                        if ((rgb & 0xFF000000) == 0) {
                            return;
                        }
                    }
                }

                String message = "Launcher icons should not fill every pixel of their square " +
                                 "region; see the design guide for details";
                context.report(ICON_LAUNCHER_SHAPE, Location.create(file),
                        message, null);
            }
        } catch (IOException e) {
            // Pass: ignore files we can't read
        }
}

/**
//Synthetic comment -- @@ -1231,7 +1312,7 @@
// also check that they actually include a -v11 or -v14 folder with proper
// icons, since the below won't flag the older icons.
try {
            BufferedImage image = getImage(file);
if (image != null) {
if (isActionBarIcon) {
checkPixels:








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java
//Synthetic comment -- index 4053c33..e2c20ba 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
ALL.add(IconDetector.ICON_NODPI);
ALL.add(IconDetector.ICON_COLORS);
ALL.add(IconDetector.ICON_XML_AND_PNG);
        ALL.add(IconDetector.ICON_LAUNCHER_SHAPE);
}

@Override
//Synthetic comment -- @@ -444,5 +445,16 @@
));
}

    public void testSquareLauncher() throws Exception {
        mEnabled = Collections.singleton(IconDetector.ICON_LAUNCHER_SHAPE);
        assertEquals(
            "res/drawable-hdpi/ic_launcher_filled.png: Warning: Launcher icons should not fill every pixel of their square region; see the design guide for details [IconLauncherShape]\n" +
            "0 errors, 1 warnings\n",

            lintProject(
                    "apicheck/minsdk4.xml=>AndroidManifest.xml",
                    "res/drawable-hdpi/filled.png=>res/drawable-hdpi/ic_launcher_filled.png",
                    "res/drawable-mdpi/sample_icon.gif=>res/drawable-mdpi/ic_launcher_2.gif"
            ));
    }
}
\ No newline at end of file







