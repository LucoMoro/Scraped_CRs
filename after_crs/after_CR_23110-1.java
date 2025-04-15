/*Detect and add special warning for wrong themes. DO NOT MERGE

If you open a layout designed for a particular theme in another theme,
you can get many confusing error messages. Instead of "attribute
missing" it may tell you that it failed to convert a resource to a
color or drawable, and so on.

To help guide users to the root problem (wrong theme chosen) this
changeset detects the scenario where theme attributes can't be
resolved, and when these are found the top of the error log will start
with a bold message stating that theme resources were not found and to
check whether the correct theme is chosen.

To do this, the resource resolver emits a new sub-type of the resource
missing tag into the error log, which is used in the IDE to prefix the
errors with the special error message.

Change-Id:Ifa188cd67412e4536bc10bda64716a62dfa9695d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 29811dc..18cb516 100644

//Synthetic comment -- @@ -1755,7 +1755,9 @@
}

if (!append) {
            mErrorLabel.setText("");    //$NON-NLS-1$
        } else {
            addText(mErrorLabel, "\n"); //$NON-NLS-1$
}
if (missingClasses.size() > 0) {
addText(mErrorLabel, "The following classes could not be found:\n");
//Synthetic comment -- @@ -1833,6 +1835,14 @@
AdtPlugin.log(e, null);
}

            if (logger.seenTagPrefix(LayoutLog.TAG_RESOURCES_RESOLVE_THEME_ATTR)) {
                addBoldText(mErrorLabel,
                        "Missing styles. Is the correct theme chosen for this layout?\n");
                addText(mErrorLabel,
                        "Use the Theme combo box above the layout to choose a different layout, " +
                        "or fix the theme style references.\n\n");
            }

if (hasAaptErrors && logger.seenTagPrefix(LayoutLog.TAG_RESOURCES_PREFIX)) {
// Text will automatically be wrapped by the error widget so no reason
// to insert linebreaks in this error message:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index e44c81b..5ec50c9 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
}

if (mFidelityWarnings != null) {
            sb.append("The graphics preview in the layout editor may not be accurate:\n");
for (String warning : mFidelityWarnings) {
sb.append("* ");
sb.append(warning).append('\n');
//Synthetic comment -- @@ -91,6 +91,7 @@
@Override
public void error(String tag, String message, Object data) {
String description = describe(message);

AdtPlugin.log(IStatus.ERROR, "%1$s: %2$s", mName, description);

addError(tag, description);
//Synthetic comment -- @@ -211,6 +212,4 @@
return false;
}
}
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 89a4cba..938a6ef 100644

//Synthetic comment -- @@ -67,14 +67,13 @@
}

/**
     * Creates a new {@link ResourceResolver} object.
*
* @param projectResources the project resources.
* @param frameworkResources the framework resources.
* @param themeName the name of the current theme.
* @param isProjectTheme Is this a project theme?
     * @return a new {@link ResourceResolver}
*/
public static ResourceResolver create(
Map<ResourceType, Map<String, ResourceValue>> projectResources,
//Synthetic comment -- @@ -231,12 +230,22 @@
}

// Now look for the item in the theme, starting with the current one.
            ResourceValue item;
if (frameworkOnly) {
// FIXME for now we do the same as if it didn't specify android:
                item = findItemInStyle(mTheme, referenceName);
            } else {
                item = findItemInStyle(mTheme, referenceName);
}

            if (item == null && mLogger != null) {
                mLogger.warning(LayoutLog.TAG_RESOURCES_RESOLVE_THEME_ATTR,
                        String.format("Couldn't find theme resource %1$s for the current theme",
                                reference),
                        new ResourceValue(ResourceType.ATTR, referenceName, frameworkOnly));
            }

            return item;
} else if (reference.startsWith(PREFIX_RESOURCE_REF)) {
boolean frameworkOnly = false;









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java b/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java
//Synthetic comment -- index 26d0479..df29537 100644

//Synthetic comment -- @@ -57,6 +57,13 @@
public final static String TAG_RESOURCES_RESOLVE = TAG_RESOURCES_PREFIX + "resolve";

/**
     * Tag for resource resolution failure, specifically for theme attributes.
     * In this case the warning/error data object will be a ResourceValue containing the type
     * and name of the resource that failed to resolve
     */
    public final static String TAG_RESOURCES_RESOLVE_THEME_ATTR = TAG_RESOURCES_RESOLVE + ".theme";

    /**
* Tag for failure when reading the content of a resource file.
*/
public final static String TAG_RESOURCES_READ = TAG_RESOURCES_PREFIX + "read";







