/*Markup

Change-Id:I672f519ac7a0bff0ce8eeb4d89ed38ed8508bc07*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 36ebf5a..26b5924 100644

//Synthetic comment -- @@ -577,9 +577,10 @@
}

String summary = issue.getDescription();
        String explanation = issue.getExplanation();

StringBuilder sb = new StringBuilder(summary.length() + explanation.length() + 20);
try {
sb.append((String) marker.getAttribute(IMarker.MESSAGE));
sb.append('\n').append('\n');
//Synthetic comment -- @@ -597,6 +598,7 @@
sb.append('\n').append('\n');
sb.append(issue.getMoreInfo());
}

return sb.toString();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java
//Synthetic comment -- index 5d38df2..1a6aac2 100644

//Synthetic comment -- @@ -493,7 +493,7 @@
sb.append('\n');
if (issue.getExplanation() != null) {
sb.append('\n');
                sb.append(issue.getExplanation());
} else {
sb.append(issue.getDescription());
}








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/HtmlReporter.java b/lint/cli/src/com/android/tools/lint/HtmlReporter.java
//Synthetic comment -- index c3ed9d2..09cc676 100644

//Synthetic comment -- @@ -368,8 +368,8 @@
}
mWriter.write("</div>\n");                               //$NON-NLS-1$
mWriter.write("<div class=\"explanation\">\n");          //$NON-NLS-1$
        String explanation = issue.getExplanation();
        appendEscapedText(explanation, true /* preserve newlines*/);
mWriter.write("\n</div>\n");                             //$NON-NLS-1$;
if (issue.getMoreInfo() != null) {
mWriter.write("<br/>");                                  //$NON-NLS-1$
//Synthetic comment -- @@ -729,6 +729,7 @@
}

private void appendEscapedText(String textValue, boolean preserveNewlines) throws IOException {
for (int i = 0, n = textValue.length(); i < n; i++) {
char c = textValue.charAt(i);
if (c == '<') {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Issue.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Issue.java
//Synthetic comment -- index 1ac15e2..70d3cf7 100644

//Synthetic comment -- @@ -42,6 +42,8 @@
*/
@Beta
public final class Issue implements Comparable<Issue> {
private final String mId;
private final String mDescription;
private final String mExplanation;
//Synthetic comment -- @@ -131,6 +133,12 @@
* "Buttons must define contentDescriptions". Preferably the explanation
* should also contain a description of how the problem should be solved.
* Additional info can be provided via {@link #getMoreInfo()}.
*
* @return an explanation of the issue, never null.
*/
//Synthetic comment -- @@ -140,6 +148,30 @@
}

/**
* The primary category of the issue
*
* @return the primary category of the issue, never null
//Synthetic comment -- @@ -395,4 +427,126 @@
public String toString() {
return mId;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AccessibilityDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AccessibilityDetector.java
//Synthetic comment -- index e1be383..af5c805 100644

//Synthetic comment -- @@ -51,7 +51,7 @@
"ContentDescription", //$NON-NLS-1$
"Ensures that image widgets provide a contentDescription",
"Non-textual widgets like ImageViews and ImageButtons should use the " +
            "contentDescription attribute to specify a textual description of " +
"the widget such that screen readers and other accessibility tools " +
"can adequately describe the user interface.",
Category.A11Y,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AlwaysShowActionDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AlwaysShowActionDetector.java
//Synthetic comment -- index bc5eaae..42d48bc 100644

//Synthetic comment -- @@ -61,18 +61,18 @@
"Checks for uses of showAsAction=\"always\" and suggests showAsAction=\"ifRoom\" " +
"instead",

            "Using showAsAction=\"always\" in menu XML, or MenuItem.SHOW_AS_ACTION_ALWAYS in " +
"Java code is usually a deviation from the user interface style guide." +
            "Use \"ifRoom\" or the corresponding MenuItem.SHOW_AS_ACTION_IF_ROOM instead.\n" +
"\n" +
            "If \"always\" is used sparingly there are usually no problems and behavior is " +
            "roughly equivalent to \"ifRoom\" but with preference over other \"ifRoom\" " +
"items. Using it more than twice in the same menu is a bad idea.\n" +
"\n" +
            "This check looks for menu XML files that contain more than two \"always\" " +
            "actions, or some \"always\" actions and no \"ifRoom\" actions. In Java code, " +
            "it looks for projects that contain references to MenuItem.SHOW_AS_ACTION_ALWAYS " +
            "and no references to MenuItem.SHOW_AS_ACTION_IF_ROOM.",

Category.USABILITY,
3,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java
//Synthetic comment -- index aa0c84d..0cb145e 100644

//Synthetic comment -- @@ -57,10 +57,10 @@
"LocalSuppress", //$NON-NLS-1$
"Looks for @SuppressLint annotations in locations where it doesn't work for class based checks",

            "The @SuppressAnnotation is used to suppress Lint warnings in Java files. However, " +
"while many lint checks analyzes the Java source code, where they can find " +
"annotations on (for example) local variables, some checks are analyzing the " +
            ".class files. And in class files, annotations only appear on classes, fields " +
"and methods. Annotations placed on local variables disappear. If you attempt " +
"to suppress a lint error for a class-file based lint check, the suppress " +
"annotation not work. You must move the annotation out to the surrounding method.",








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 8762a0c..1643e58 100644

//Synthetic comment -- @@ -79,13 +79,13 @@
"by this application (according to its minimum SDK attribute in the manifest).\n" +
"\n" +
"If you really want to use this API and don't need to support older devices just " +
            "set the minSdkVersion in your AndroidManifest.xml file." +
"\n" +
"If your code is *deliberately* accessing newer APIs, and you have ensured " +
"(e.g. with conditional execution) that this code will only ever be called on a " +
"supported platform, then you can annotate your class or method with the " +
            "@TargetApi annotation specifying the local minimum SDK to apply, such as" +
            "@TargetApi(11), such that this check considers 11 rather than your manifest " +
"file's minimum SDK as the required API level.",
Category.CORRECTNESS,
6,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ColorUsageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ColorUsageDetector.java
//Synthetic comment -- index dc3410b..2c7a520 100644

//Synthetic comment -- @@ -49,7 +49,7 @@

"Methods that take a color in the form of an integer should be passed " +
"an RGB triple, not the actual color resource id. You must call " +
            "getResources().getColor(resource) to resolve the actual color value first.",

Category.CORRECTNESS,
7,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java
//Synthetic comment -- index c8e34c5..f27005c 100644

//Synthetic comment -- @@ -76,7 +76,7 @@
public static final Issue WITHIN_LAYOUT = Issue.create(
"DuplicateIds", //$NON-NLS-1$
"Checks for duplicate ids within a single layout",
            "Within a layout, id's should be unique since otherwise findViewById() can " +
"return an unexpected view.",
Category.CORRECTNESS,
7,
//Synthetic comment -- @@ -90,7 +90,7 @@
"Checks for duplicate ids across layouts that are combined with include tags",
"It's okay for two independent layouts to use the same ids. However, if " +
"layouts are combined with include tags, then the id's need to be unique " +
            "within any chain of included layouts, or Activity#findViewById() can " +
"return an unexpected view.",
Category.CORRECTNESS,
6,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index 2fc3034..06162c4 100644

//Synthetic comment -- @@ -60,8 +60,8 @@
"restoring its activity's state. It is strongly recommended that subclasses do not " +
"have other constructors with parameters, since these constructors will not be " +
"called when the fragment is re-instantiated; instead, arguments can be supplied " +
        "by the caller with setArguments(Bundle) and later retrieved by the Fragment " +
        "with getArguments().",

Category.CORRECTNESS,
6,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/GridLayoutDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/GridLayoutDetector.java
//Synthetic comment -- index 6e8d8a3..f32905a 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
"Checks for potential GridLayout errors like declaring rows and columns outside " +
"the declared grid dimensions",
"Declaring a layout_row or layout_column that falls outside the declared size " +
            "of a GridLayout's rowCount or columnCount is usually an unintentional error.",
Category.CORRECTNESS,
4,
Severity.FATAL,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java
//Synthetic comment -- index 1237e1b..b684e6f 100644

//Synthetic comment -- @@ -46,10 +46,10 @@
"HardcodedDebugMode", //$NON-NLS-1$
"Checks for hardcoded values of android:debuggable in the manifest",

            "It's best to leave out the android:debuggable attribute from the manifest. " +
            "If you do, then the tools will automatically insert android:debuggable=true when " +
"building an APK to debug on an emulator or device. And when you perform a " +
            "release build, such as Exporting APK, it will automatically set it to false.\n" +
"\n" +
"If on the other hand you specify a specific value in the manifest file, then " +
"the tools will always use it. This can lead to accidentally publishing " +








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index da27cc9..5d3d952 100644

//Synthetic comment -- @@ -129,7 +129,7 @@
"IconDipSize", //$NON-NLS-1$
"Ensures that icons across densities provide roughly the same density-independent size",
"Checks the all icons which are provided in multiple densities, all compute to " +
            "roughly the same density-independent pixel (dip) size. This catches errors where " +
"images are either placed in the wrong folder, or icons are changed to new sizes " +
"but some folders are forgotten.",
Category.ICONS,
//Synthetic comment -- @@ -143,10 +143,10 @@
"IconLocation", //$NON-NLS-1$
"Ensures that images are not defined in the density-independent drawable folder",
"The res/drawable folder is intended for density-independent graphics such as " +
            "shapes defined in XML. For bitmaps, move it to drawable-mdpi and consider " +
            "providing higher and lower resolution versions in drawable-ldpi, drawable-hdpi " +
            "and drawable-xhdpi. If the icon *really* is density independent (for example " +
            "a solid color) you can place it in drawable-nodpi.",
Category.ICONS,
5,
Severity.WARNING,
//Synthetic comment -- @@ -165,7 +165,7 @@
"\n" +
"Low density is not really used much anymore, so this check ignores " +
"the ldpi density. To force lint to include it, set the environment " +
            "variable ANDROID_LINT_INCLUDE_LDPI=true. For more information on " +
"current density usage, see " +
"http://developer.android.com/resources/dashboard/screens.html",
Category.ICONS,
//Synthetic comment -- @@ -181,11 +181,11 @@
"Ensures that all the density folders are present",
"Icons will look best if a custom version is provided for each of the " +
"major screen density classes (low, medium, high, extra high). " +
            "This lint check identifies folders which are missing, such as drawable-hdpi." +
"\n" +
"Low density is not really used much anymore, so this check ignores " +
"the ldpi density. To force lint to include it, set the environment " +
            "variable ANDROID_LINT_INCLUDE_LDPI=true. For more information on " +
"current density usage, see " +
"http://developer.android.com/resources/dashboard/screens.html",
Category.ICONS,
//Synthetic comment -- @@ -199,8 +199,8 @@
public static final Issue GIF_USAGE = Issue.create(
"GifUsage", //$NON-NLS-1$
"Checks for images using the GIF file format which is discouraged",
            "The .gif file format is discouraged. Consider using .png (preferred) " +
            "or .jpg (acceptable) instead.",
Category.ICONS,
5,
Severity.WARNING,
//Synthetic comment -- @@ -227,7 +227,7 @@
"IconDuplicatesConfig", //$NON-NLS-1$
"Finds icons that have identical bitmaps across various configuration parameters",
"If an icon is provided under different configuration parameters such as " +
            "drawable-hdpi or -v11, they should typically be different. This detector " +
"catches cases where the same icon is provided in different configuration folder " +
"which is usually not intentional.",
Category.ICONS,
//Synthetic comment -- @@ -240,9 +240,9 @@
public static final Issue ICON_NODPI = Issue.create(
"IconNoDpi", //$NON-NLS-1$
"Finds icons that appear in both a -nodpi folder and a dpi folder",
            "Bitmaps that appear in drawable-nodpi folders will not be scaled by the " +
"Android framework. If a drawable resource of the same name appears *both* in " +
            "a -nodpi folder as well as a dpi folder such as drawable-hdpi, then " +
"the behavior is ambiguous and probably not intentional. Delete one or the " +
"other, or use different names for the icons.",
Category.ICONS,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/InefficientWeightDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/InefficientWeightDetector.java
//Synthetic comment -- index 3fd5573..c00b57e 100644

//Synthetic comment -- @@ -55,8 +55,8 @@
"InefficientWeight", //$NON-NLS-1$
"Looks for inefficient weight declarations in LinearLayouts",
"When only a single widget in a LinearLayout defines a weight, it is more " +
            "efficient to assign a width/height of 0dp to it since it will absorb all " +
            "the remaining space anyway. With a declared width/height of 0dp it " +
"does not have to measure its own size first.",
Category.PERFORMANCE,
3,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/JavaPerformanceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/JavaPerformanceDetector.java
//Synthetic comment -- index 0073d06..fe3b713 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
"The way this is generally handled is to allocate the needed objects up front " +
"and to reuse them for each drawing operation.\n" +
"\n" +
            "Some methods allocate memory on your behalf (such as Bitmap.create), and these " +
"should be handled in the same way.",

Category.PERFORMANCE,
//Synthetic comment -- @@ -87,16 +87,16 @@
"Looks for opportunities to replace HashMaps with the more efficient SparseArray",

"For maps where the keys are of type integer, it's typically more efficient to " +
            "use the Android SparseArray API. This check identifies scenarios where you might " +
            "want to consider using SparseArray instead of HashMap for better performance.\n" +
"\n" +
"This is *particularly* useful when the value types are primitives like ints, " +
            "where you can use SparseIntArray and avoid auto-boxing the values from int to " +
            "Integer.\n" +
"\n" +
            "If you need to construct a HashMap because you need to call an API outside of " +
            "your control which requires a Map, you can suppress this warning using for " +
            "example the @SuppressLint annotation.",

Category.PERFORMANCE,
4,
//Synthetic comment -- @@ -110,8 +110,8 @@
"Looks for usages of \"new\" for wrapper classes which should use \"valueOf\" instead",

"You should not call the constructor for wrapper classes directly, such as" +
            "\"new Integer(42)\". Instead, call the \"valueOf\" factory method, such as " +
            "Integer.valueOf(42). This will typically use less memory because common integers " +
"such as 0 and 1 will share a single instance.",

Category.PERFORMANCE,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java
//Synthetic comment -- index eb1dcba..7aeae15 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
"The <application> tag should appear after the elements which declare " +
"which version you need, which features you need, which libraries you " +
"need, and so on. In the past there have been subtle bugs (such as " +
            "themes not getting applied correctly) when the <application> tag appears " +
"before some of these other elements, so it's best to order your " +
"manifest in the logical dependency order.",
Category.CORRECTNESS,
//Synthetic comment -- @@ -82,7 +82,7 @@
"UsesMinSdkAttributes", //$NON-NLS-1$
"Checks that the minimum SDK and target SDK attributes are defined",

            "The manifest should contain a <uses-sdk> element which defines the " +
"minimum minimum API Level required for the application to run, " +
"as well as the target version (the highest API level you have tested " +
"the version for.)",
//Synthetic comment -- @@ -100,12 +100,12 @@
"Checks that the manifest specifies a targetSdkVersion that is recent",

"When your application runs on a version of Android that is more recent than your " +
            "targetSdkVersion specifies that it has been tested with, various compatibility " +
"modes kick in. This ensures that your application continues to work, but it may " +
            "look out of place. For example, if the targetSdkVersion is less than 14, your " +
"app may get an option button in the UI.\n" +
"\n" +
            "To fix this issue, set the targetSdkVersion to the highest available value. Then " +
"test your app to make sure everything works correctly. You may want to consult " +
"the compatibility notes to see what changes apply to each version you are adding " +
"support for: " +
//Synthetic comment -- @@ -123,7 +123,7 @@
"MultipleUsesSdk", //$NON-NLS-1$
"Checks that the <uses-sdk> element appears at most once",

            "The <uses-sdk> element should appear just once; the tools will *not* merge the " +
"contents of all the elements so if you split up the atttributes across multiple " +
"elements, only one of them will take effect. To fix this, just merge all the " +
"attributes from the various elements into a single <uses-sdk> element.",
//Synthetic comment -- @@ -140,9 +140,9 @@
"WrongManifestParent", //$NON-NLS-1$
"Checks that various manifest elements are declared in the right place",

            "The <uses-library> element should be defined as a direct child of the " +
            "<application> tag, not the <manifest> tag or an <activity> tag. Similarly, " +
            "a <uses-sdk> tag much be declared at the root level, and so on. This check " +
"looks for incorrect declaration locations in the manifest, and complains " +
"if an element is found in the wrong place.",









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java
//Synthetic comment -- index ff2609d..5b3fe99 100644

//Synthetic comment -- @@ -87,10 +87,10 @@
public static final Issue ISSUE = Issue.create(
"MergeRootFrame", //$NON-NLS-1$
"Checks whether a root <FrameLayout> can be replaced with a <merge> tag",
            "If a <FrameLayout> is the root of a layout and does not provide background " +
            "or padding etc, it can often be replaced with a <merge> tag which is slightly " +
"more efficient. Note that this depends on context, so make sure you understand " +
            "how the <merge> tag works before proceeding.",
Category.PERFORMANCE,
4,
Severity.WARNING,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java
//Synthetic comment -- index d6cdbd6..3f0fef3 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
"NestedScrolling", //$NON-NLS-1$
"Checks whether a scrolling widget has any nested scrolling widgets within",
// TODO: Better description!
            "A scrolling widget such as a ScrollView should not contain any nested " +
"scrolling widgets since this has various usability issues",
Category.CORRECTNESS,
7,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java
//Synthetic comment -- index 3c1b4bf..86191bf 100644

//Synthetic comment -- @@ -44,7 +44,8 @@
"Looks for code sending text messages to unlocalized phone numbers",

"SMS destination numbers must start with a country code or the application code " +
            "must ensure that the SMS is only sent when the user is in the same country as the receiver.",

Category.CORRECTNESS,
5,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index bb9c998..4283b7b 100644

//Synthetic comment -- @@ -60,9 +60,9 @@
"OnClick", //$NON-NLS-1$
"Ensures that onClick attribute values refer to real methods",

            "The onClick attribute value should be the name of a method in this View's context " +
"to invoke when the view is clicked. This name must correspond to a public method " +
            "that takes exactly one parameter of type View.\n" +
"\n" +
"Must be a string value, using '\\;' to escape characters such as '\\n' or " +
"'\\uxxxx' for a unicode character.",








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/PrivateResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/PrivateResourceDetector.java
//Synthetic comment -- index 7d909ae..c1e405b 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
"even where they are they may disappear without notice.\n" +
"\n" +
"To fix this, copy the resource into your own project. You can find the platform " +
            "resources under $ANDROID_SK/platforms/android-$VERSION/data/res/.",
Category.CORRECTNESS,
3,
Severity.FATAL,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ProguardDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ProguardDetector.java
//Synthetic comment -- index 41af3b1..7544de5 100644

//Synthetic comment -- @@ -44,8 +44,8 @@
"Using -keepclasseswithmembernames in a proguard config file is not " +
"correct; it can cause some symbols to be renamed which should not be.\n" +
"Earlier versions of ADT used to create proguard.cfg files with the " +
            "wrong format. Instead of -keepclasseswithmembernames use " +
            "-keepclasseswithmembers, since the old flags also implies " +
"\"allow shrinking\" which means symbols only referred to from XML and " +
"not Java (such as possibly CustomViews) can get deleted.",
Category.CORRECTNESS,
//Synthetic comment -- @@ -61,7 +61,7 @@
"ProguardSplit", //$NON-NLS-1$
"Checks for old proguard.cfg files that contain generic Android rules",

            "Earlier versions of the Android tools bundled a single \"proguard.cfg\" file " +
"containing a ProGuard configuration file suitable for Android shrinking and " +
"obfuscation. However, that version was copied into new projects, which " +
"means that it does not continue to get updated as we improve the default " +
//Synthetic comment -- @@ -76,14 +76,14 @@
"directory which means that it gets updated along with the tools.\n" +
"\n" +
"In order for this to work, the proguard.config property in the " +
            "project.properties file now refers to a path, so you can reference both " +
"the generic file as well as your own (and any additional files too).\n" +
"\n" +
            "To migrate your project to the new setup, create a new proguard-project.txt file " +
"in your project containing any project specific ProGuard flags as well as " +
"any customizations you have made, then update your project.properties file " +
"to contain:\n" +
            "proguard.config=${sdk.dir}/tools/proguard/proguard-android.txt:proguard-project.txt",

Category.CORRECTNESS,
3,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/PxUsageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/PxUsageDetector.java
//Synthetic comment -- index f9e3bdd..6734592 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
"For performance reasons and to keep the code simpler, the Android system uses pixels " +
"as the standard unit for expressing dimension or coordinate values. That means that " +
"the dimensions of a view are always expressed in the code using pixels, but " +
            "always based on the current screen density. For instance, if myView.getWidth() " +
"returns 10, the view is 10 pixels wide on the current screen, but on a device with " +
"a higher density screen, the value returned might be 15. If you use pixel values " +
"in your application code to work with bitmaps that are not pre-scaled for the " +
//Synthetic comment -- @@ -72,13 +72,13 @@
"SpUsage", //$NON-NLS-1$
"Looks for uses of \"dp\" instead of \"sp\" dimensions for text sizes",

            "When setting text sizes, you should normally use \"sp\", or \"scale-independent " +
            "pixels\". This is like the dp unit, but it is also scaled " +
"by the user's font size preference. It is recommend you use this unit when " +
"specifying font sizes, so they will be adjusted for both the screen density " +
"and the user's preference.\n" +
"\n" +
            "There *are* cases where you might need to use \"dp\"; typically this happens when " +
"the text is in a container with a specific dp-size. This will prevent the text " +
"from spilling outside the container. Note however that this means that the user's " +
"font size settings are not respected, so consider adjusting the layout itself " +








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java
//Synthetic comment -- index 2455365..b645253 100644

//Synthetic comment -- @@ -62,7 +62,7 @@
"Ensures that Activities, Services and Content Providers are registered in the manifest",

"Activities, services and content providers should be registered in the " +
        "AndroidManifext.xml file using <activity>, <service> and <provider> tags.\n" +
"\n" +
"If your activity is simply a parent class intended to be subclassed by other " +
"\"real\" activities, make it an abstract class.",








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ScrollViewChildDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ScrollViewChildDetector.java
//Synthetic comment -- index e5db5f1..e7796ea 100644

//Synthetic comment -- @@ -51,8 +51,8 @@
"ScrollViewSize", //$NON-NLS-1$
"Checks that ScrollViews use wrap_content in scrolling dimension",
// TODO add a better explanation here!
            "ScrollView children must set their layout_width or layout_height attributes " +
            "to wrap_content rather than fill_parent or match_parent in the scrolling " +
"dimension",
Category.CORRECTNESS,
7,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SdCardDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SdCardDetector.java
//Synthetic comment -- index d5e3a88..e7545a1 100644

//Synthetic comment -- @@ -45,8 +45,8 @@
"SdCardPath", //$NON-NLS-1$
"Looks for hardcoded references to /sdcard",

            "Your code should not reference the /sdcard path directly; instead use " +
            "Environment.getExternalStorageDirectory().getPath()",

Category.CORRECTNESS,
6,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index c3bc919..8c79a2c 100644

//Synthetic comment -- @@ -77,8 +77,8 @@
public static final Issue EXPORTED_SERVICE = Issue.create(
"ExportedService", //$NON-NLS-1$
"Checks for exported services that do not require permissions",
            "Exported services (services which either set exported=true or contain " +
            "an intent-filter and do not specify exported=false) should define a " +
"permission that an entity must have in order to launch the service " +
"or bind to it. Without this, any application can use this service.",
Category.SECURITY,
//Synthetic comment -- @@ -94,7 +94,7 @@
"Content providers are exported by default and any application on the " +
"system can potentially use them to read and write data. If the content" +
"provider provides access to sensitive data, it should be protected by " +
            "specifying export=false in the manifest or by protecting it with a " +
"permission that can be granted to other applications.",
Category.SECURITY,
5,
//Synthetic comment -- @@ -106,8 +106,8 @@
public static final Issue EXPORTED_ACTIVITY = Issue.create(
"ExportedActivity", //$NON-NLS-1$
"Checks for exported activities that do not require permissions",
            "Exported activities (activities which either set exported=true or contain " +
            "an intent-filter and do not specify exported=false) should define a " +
"permission that an entity must have in order to launch the activity " +
"or bind to it. Without this, any application can use this activity.",
Category.SECURITY,
//Synthetic comment -- @@ -120,8 +120,8 @@
public static final Issue EXPORTED_RECEIVER = Issue.create(
"ExportedReceiver", //$NON-NLS-1$
"Checks for exported receivers that do not require permissions",
            "Exported receivers (receivers which either set exported=true or contain " +
            "an intent-filter and do not specify exported=false) should define a " +
"permission that an entity must have in order to launch the receiver " +
"or bind to it. Without this, any application can use this receiver.",
Category.SECURITY,
//Synthetic comment -- @@ -134,7 +134,7 @@
public static final Issue OPEN_PROVIDER = Issue.create(
"GrantAllUris", //$NON-NLS-1$
"Checks for <grant-uri-permission> elements where everything is shared",
            "The <grant-uri-permission> element allows specific paths to be shared. " +
"This detector checks for a path URL of just '/' (everything), which is " +
"probably not what you want; you should limit access to a subset.",
Category.SECURITY,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SetJavaScriptEnabledDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SetJavaScriptEnabledDetector.java
//Synthetic comment -- index f78d8c4..361d4f9 100644

//Synthetic comment -- @@ -39,8 +39,8 @@
public static final Issue ISSUE = Issue.create("SetJavaScriptEnabled", //$NON-NLS-1$
"Looks for invocations of android.webkit.WebSettings.setJavaScriptEnabled",

            "Your code should not invoke setJavaScriptEnabled if you are not sure that" +
                    "your app really requires JavaScript support.",

Category.SECURITY,
6,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index 48fdbeb..685d9cf 100644

//Synthetic comment -- @@ -50,8 +50,8 @@
"CommitPrefEdits", //$NON-NLS-1$
"Looks for code editing a SharedPreference but forgetting to call commit() on it",

            "After calling edit() on a SharedPreference, you must call commit() or apply() on " +
            "the editor to save the results.",

Category.CORRECTNESS,
6,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index c8671af..23cfa38 100644

//Synthetic comment -- @@ -93,18 +93,18 @@
"Checks that format strings are valid",

"If a string contains a '%' character, then the string may be a formatting string " +
            "which will be passed to String.format from Java code to replace each '%' " +
"occurrence with specific values.\n" +
"\n" +
"This lint warning checks for two related problems:\n" +
            "(1) Formatting strings that are invalid, meaning that String.format will throw " +
"exceptions at runtime when attempting to use the format string.\n" +
"(2) Strings containing '%' that are not formatting strings getting passed to " +
            "a String.format call. In this case the '%' will need to be escaped as '%%'.\n" +
"\n" +
"NOTE: Not all Strings which look like formatting strings are intended for " +
            "use by String.format; for example, they may contain date formats intended " +
            "for android.text.format.Time#format(). Lint cannot always figure out that " +
"a String is a date format, so you may get false warnings in those scenarios. " +
"See the suppress help topic for information on how to suppress errors in " +
"that case.",








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextFieldDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextFieldDetector.java
//Synthetic comment -- index c133ce2..518bf2e 100644

//Synthetic comment -- @@ -44,14 +44,14 @@
public static final Issue ISSUE = Issue.create(
"TextFields", //$NON-NLS-1$
"Looks for text fields missing inputType or hint settings",
            "Providing an inputType attribute on a text field improves usability " +
"because depending on the data to be input, optimized keyboards can be shown " +
"to the user (such as just digits and parentheses for a phone number). Similarly," +
"a hint attribute displays a hint to the user for what is expected in the " +
"text field.\n" +
"\n" +
"If you really want to keep the text field generic, you can suppress this warning " +
            "by setting inputType=\"text\".",

Category.USABILITY,
5,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextViewDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextViewDetector.java
//Synthetic comment -- index e6e0371..db9155a 100644

//Synthetic comment -- @@ -70,14 +70,14 @@
"TextViewEdits", //$NON-NLS-1$
"Looks for TextViews being used for input",

            "Using a <TextView> to input text is generally an error, you should be " +
            "using <EditText> instead.  EditText is a subclass of TextView, and some " +
            "of the editing support is provided by TextView, so it's possible to set " +
            "some input-related properties on a TextView. However, using a TextView " +
"along with input attributes is usually a cut & paste error. To input " +
            "text you should be using <EditText>." +
"\n" +
            "This check also checks subclasses of TextView, such as Button and CheckBox, " +
"since these have the same issue: they should not be used with editable " +
"attributes.",









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index b15d149..52ae8ed 100644

//Synthetic comment -- @@ -48,8 +48,8 @@
"ShowToast", //$NON-NLS-1$
"Looks for code creating a Toast but forgetting to call show() on it",

            "Toast.makeText() creates a Toast but does *not* show it. You must call " +
            "show() on the resulting object to actually make the Toast appear.",

Category.CORRECTNESS,
6,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TooManyViewsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TooManyViewsDetector.java
//Synthetic comment -- index ed3480a..3256b91 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
"performance. Consider using compound drawables or other tricks for " +
"reducing the number of views in this layout.\n\n" +
"The maximum view count defaults to 80 but can be configured with the " +
            "environment variable ANDROID_LINT_MAX_VIEW_COUNT.",
Category.PERFORMANCE,
1,
Severity.WARNING,
//Synthetic comment -- @@ -54,9 +54,9 @@
"TooDeepLayout", //$NON-NLS-1$
"Checks whether a layout hierarchy is too deep",
"Layouts with too much nesting is bad for performance. " +
            "Consider using a flatter layout (such as RelativeLayout or GridLayout)." +
"The default maximum depth is 10 but can be configured with the environment " +
            "variable ANDROID_LINT_MAX_DEPTH.",
Category.PERFORMANCE,
1,
Severity.WARNING,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index 391033e..c796d26 100644

//Synthetic comment -- @@ -75,15 +75,15 @@
"one language should also be translated in all other languages.\n" +
"\n" +
"If the string should *not* be translated, you can add the attribute\n" +
            "translatable=\"false\" on the <string> element, or you can define all " +
            "your non-translatable strings in a resource file called \"donottranslate.xml\". " +
            "Or, you can ignore the issue with a tools:ignore=\"MissingTranslation\" " +
"attribute.\n" +
"\n" +
"By default this detector allows regions of a language to just provide a " +
"subset of the strings and fall back to the standard language strings. " +
"You can require all regions to provide a full translation by setting the " +
            "environment variable ANDROID_LINT_COMPLETE_REGIONS.",
Category.MESSAGES,
8,
Severity.FATAL,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UseCompoundDrawableDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UseCompoundDrawableDetector.java
//Synthetic comment -- index 777c4ff..e2923b0 100644

//Synthetic comment -- @@ -49,10 +49,13 @@
public static final Issue ISSUE = Issue.create(
"UseCompoundDrawables", //$NON-NLS-1$
"Checks whether the current node can be replaced by a TextView using compound drawables.",
            // TODO: OFFER MORE HELP!
            "A LinearLayout which contains an ImageView and a TextView can be more efficiently " +
            "handled as a compound drawable. If the two widgets are offset from each other with " +
            "margins, this can be replaced with a drawablePadding attribute.\n" +
"\n" +
"There's a lint quickfix to perform this conversion in the Eclipse plugin.",
Category.PERFORMANCE,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index 68062c5..a532ced 100644

//Synthetic comment -- @@ -53,13 +53,13 @@

"Some layout tools (such as the Android layout editor for Eclipse) needs to " +
"find a constructor with one of the following signatures:\n" +
            "* View(Context context)\n" +
            "* View(Context context, AttributeSet attrs)\n" +
            "* View(Context context, AttributeSet attrs, int defStyle)\n" +
"\n" +
"If your custom view needs to perform initialization which does not apply when " +
"used in a layout editor, you can surround the given code with a check to " +
            "see if View#isInEditMode() is false, since that method will return false " +
"at runtime but true within a user interface editor.",

Category.USABILITY,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WakelockDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WakelockDetector.java
//Synthetic comment -- index c828eba..066575a 100644

//Synthetic comment -- @@ -55,12 +55,12 @@

"Failing to release a wakelock properly can keep the Android device in " +
"a high power mode, which reduces battery life. There are several causes " +
        "of this, such as releasing the wake lock in onDestroy() instead of in " +
        "onPause(), failing to call release() in all possible code paths after " +
        "an acquire(), and so on.\n" +
"\n" +
"NOTE: If you are using the lock just to keep the screen on, you should " +
        "strongly consider using FLAG_KEEP_SCREEN_ON instead. This window flag " +
"will be correctly managed by the platform as the user moves between " +
"applications and doesn't require a special permission. See " +
"http://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_KEEP_SCREEN_ON.",








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java
//Synthetic comment -- index 18db53d..8003843 100644

//Synthetic comment -- @@ -90,7 +90,7 @@
public static final Issue UNKNOWN_ID = Issue.create(
"UnknownId", //$NON-NLS-1$
"Checks for id references in RelativeLayouts that are not defined elsewhere",
            "The \"@+id/\" syntax refers to an existing id, or creates a new one if it has " +
"not already been defined elsewhere. However, this means that if you have a " +
"typo in your reference, or if the referred view no longer exists, you do not " +
"get a warning since the id will be created on demand. This check catches " +
//Synthetic comment -- @@ -107,7 +107,7 @@
"UnknownIdInLayout", //$NON-NLS-1$
"Makes sure that @+id references refer to views in the same layout",

            "The \"@+id/\" syntax refers to an existing id, or creates a new one if it has " +
"not already been defined elsewhere. However, this means that if you have a " +
"typo in your reference, or if the referred view no longer exists, you do not " +
"get a warning since the id will be created on demand.\n" +








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongImportDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongImportDetector.java
//Synthetic comment -- index fc6cde2..1f04eb2 100644

//Synthetic comment -- @@ -49,14 +49,13 @@
/** Is android.R being imported? */
public static final Issue ISSUE = Issue.create("SuspiciousImport", //$NON-NLS-1$
"Checks for 'import android.R' statements, which are usually accidental",
            "Importing android.R is usually not intentional; it sometimes happens when " +
                    "you use an IDE and ask it to automatically add imports at a time when your " +
                    "project's R class it not present.\n" +
                    "\n" +
                    "Once the import is there you might get a lot of \"confusing\" error messages "
                    +
                    "because of course the fields available on android.R are not the ones you'd " +
                    "expect from just looking at your own R class.",
Category.CORRECTNESS,
9,
Severity.WARNING,








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/IssueTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/IssueTest.java
new file mode 100644
//Synthetic comment -- index 0000000..740e921

//Synthetic comment -- @@ -0,0 +1,221 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/LintUtilsTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/LintUtilsTest.java
//Synthetic comment -- index 8379618..0ce3815 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintUtils.splitPath;
import static com.android.tools.lint.detector.api.LintUtils.getLocaleAndRegion;

import com.android.tools.lint.Main;
import com.google.common.collect.Iterables;







