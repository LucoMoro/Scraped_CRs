/*Merge into f9297748 r9: Handle Animator and Interpolator resources.

Change-Id:I85242fb436e7de99887bd60b320a16a051bddc51*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceType.java
//Synthetic comment -- index 4555913..0312f44 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
*/
public enum ResourceType {
ANIM("anim", "Animation"), //$NON-NLS-1$
ARRAY("array", "Array", "string-array", "integer-array"), //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
ATTR("attr", "Attr"), //$NON-NLS-1$
BOOL("bool", "Boolean"), //$NON-NLS-1$
//Synthetic comment -- @@ -31,6 +32,7 @@
FRACTION("fraction", "Fraction"), //$NON-NLS-1$
ID("id", "ID"), //$NON-NLS-1$
INTEGER("integer", "Integer"), //$NON-NLS-1$
LAYOUT("layout", "Layout"), //$NON-NLS-1$
MENU("menu", "Menu"), //$NON-NLS-1$
PLURALS("plurals", "Plurals"), //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationship.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationship.java
//Synthetic comment -- index c96182d..3a9c7a1 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
new HashMap<ResourceFolderType, List<ResourceType>>();

add(ResourceType.ANIM, ResourceFolderType.ANIM, typeToFolderMap, folderToTypeMap);
add(ResourceType.ARRAY, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
add(ResourceType.ATTR, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
add(ResourceType.BOOL, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
//Synthetic comment -- @@ -57,6 +58,8 @@
add(ResourceType.FRACTION, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
add(ResourceType.ID, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
add(ResourceType.INTEGER, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
add(ResourceType.LAYOUT, ResourceFolderType.LAYOUT, typeToFolderMap, folderToTypeMap);
add(ResourceType.MENU, ResourceFolderType.MENU, typeToFolderMap, folderToTypeMap);
add(ResourceType.PLURALS, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java
//Synthetic comment -- index 735a23c..2243e6b 100644

//Synthetic comment -- @@ -24,8 +24,10 @@
*/
public enum ResourceFolderType {
ANIM(SdkConstants.FD_ANIM),
COLOR(SdkConstants.FD_COLOR),
DRAWABLE(SdkConstants.FD_DRAWABLE),
LAYOUT(SdkConstants.FD_LAYOUT),
MENU(SdkConstants.FD_MENU),
RAW(SdkConstants.FD_RAW),








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 4e085ad..c282884 100644

//Synthetic comment -- @@ -134,8 +134,13 @@
// get the name from the filename.
String name = getFile().getName();

        if (type == ResourceType.ANIM || type == ResourceType.LAYOUT || type == ResourceType.MENU ||
                type == ResourceType.COLOR || type == ResourceType.XML) {
Matcher m = sXmlPattern.matcher(name);
if (m.matches()) {
return m.group(1);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index e42ac6f..b2c1326 100644

//Synthetic comment -- @@ -172,10 +172,14 @@
public final static String FD_OUTPUT = "bin"; //$NON-NLS-1$
/** Default anim resource folder name, i.e. "anim" */
public final static String FD_ANIM = "anim"; //$NON-NLS-1$
/** Default color resource folder name, i.e. "color" */
public final static String FD_COLOR = "color"; //$NON-NLS-1$
/** Default drawable resource folder name, i.e. "drawable" */
public final static String FD_DRAWABLE = "drawable"; //$NON-NLS-1$
/** Default layout resource folder name, i.e. "layout" */
public final static String FD_LAYOUT = "layout"; //$NON-NLS-1$
/** Default menu resource folder name, i.e. "menu" */







