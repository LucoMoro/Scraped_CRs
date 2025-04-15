/*ADT: XML wizard creates layouts in fill_parent by default.

Change the XML wizard to create the root layout of new
XML layouts using layout_width/height=fill_parent by
default. This makes the interraction with the layout
editor a lot easier.

Change-Id:If8e56ffe963a914ed78e4a842d48f01d94618f4e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index 9ed1d79..cb68ec2 100644

//Synthetic comment -- @@ -199,76 +199,76 @@
*/
private static final TypeInfo[] sTypes = {
new TypeInfo(
                "Layout",                                                   // UI name
                "An XML file that describes a screen layout.",              // tooltip
                ResourceFolderType.LAYOUT,                                  // folder type
                AndroidTargetData.DESCRIPTOR_LAYOUT,                        // root seed
                "LinearLayout",                                             // default root
                SdkConstants.NS_RESOURCES,                                  // xmlns
                "android:layout_width=\"fill_parent\"\n" + //$NON-NLS-1$    // default attributes
                "android:layout_height=\"fill_parent\"",   //$NON-NLS-1$
                1                                                           // target API level
),
        new TypeInfo("Values",                                              // UI name
"An XML file with simple values: colors, strings, dimensions, etc.", // tooltip
                ResourceFolderType.VALUES,                                  // folder type
                ResourcesDescriptors.ROOT_ELEMENT,                          // root seed
                null,                                                       // default root
                null,                                                       // xmlns
                null,                                                       // default attributes
                1                                                           // target API level
),
        new TypeInfo("Menu",                                                // UI name
                "An XML file that describes an menu.",                      // tooltip
                ResourceFolderType.MENU,                                    // folder type
                MenuDescriptors.MENU_ROOT_ELEMENT,                          // root seed
                null,                                                       // default root
                SdkConstants.NS_RESOURCES,                                  // xmlns
                null,                                                       // default attributes
                1                                                           // target API level
),
        new TypeInfo("AppWidget Provider",                                  // UI name
                "An XML file that describes a widget provider.",            // tooltip
                ResourceFolderType.XML,                                     // folder type
                AndroidTargetData.DESCRIPTOR_APPWIDGET_PROVIDER,            // root seed
                null,                                                       // default root
                SdkConstants.NS_RESOURCES,                                  // xmlns
                null,                                                       // default attributes
                3                                                           // target API level
),
        new TypeInfo("Preference",                                          // UI name
                "An XML file that describes preferences.",                  // tooltip
                ResourceFolderType.XML,                                     // folder type
                AndroidTargetData.DESCRIPTOR_PREFERENCES,                   // root seed
                SdkConstants.CLASS_NAME_PREFERENCE_SCREEN,                  // default root
                SdkConstants.NS_RESOURCES,                                  // xmlns
                null,                                                       // default attributes
                1                                                           // target API level
),
        new TypeInfo("Searchable",                                          // UI name
                "An XML file that describes a searchable.",                 // tooltip
                ResourceFolderType.XML,                                     // folder type
                AndroidTargetData.DESCRIPTOR_SEARCHABLE,                    // root seed
                null,                                                       // default root
                SdkConstants.NS_RESOURCES,                                  // xmlns
                null,                                                       // default attributes
                1                                                           // target API level
),
        new TypeInfo("Animation",                                           // UI name
                "An XML file that describes an animation.",                 // tooltip
                ResourceFolderType.ANIM,                                    // folder type
// TODO reuse constants if we ever make an editor with descriptors for animations
                new String[] {                                              // root seed
"set",          //$NON-NLS-1$
"alpha",        //$NON-NLS-1$
"scale",        //$NON-NLS-1$
"translate",    //$NON-NLS-1$
"rotate"        //$NON-NLS-1$
},
                "set",              //$NON-NLS-1$                           // default root
                null,                                                       // xmlns
                null,                                                       // default attributes
                1                                                           // target API level
),
};

//Synthetic comment -- @@ -685,6 +685,7 @@
*
* @param selection The selection when the wizard was initiated.
*/
    @SuppressWarnings("null")
private void initializeFromSelection(IStructuredSelection selection) {
if (selection == null) {
return;
//Synthetic comment -- @@ -1033,7 +1034,7 @@
if (wsFolderPath.startsWith(RES_FOLDER_ABS)) {
wsFolderPath.replaceFirst(
"^(" + RES_FOLDER_ABS +")[^-]*(.*)",         //$NON-NLS-1$ //$NON-NLS-2$
                        "\\1" + type.getResFolderName() + "\\2");    //$NON-NLS-1$ //$NON-NLS-2$
} else {
newPath = RES_FOLDER_ABS + mTempConfig.getFolderName(type.getResFolderType());
}
//Synthetic comment -- @@ -1066,16 +1067,18 @@
// enable the combo if there's more than one choice
mRootElementCombo.setEnabled(roots != null && roots.size() > 1);

            if (roots != null) {
                for (String root : roots) {
                    mRootElementCombo.add(root);
                }

                int index = 0; // default is to select the first one
                String defaultRoot = type.getDefaultRoot();
                if (defaultRoot != null) {
                    index = roots.indexOf(defaultRoot);
                }
                mRootElementCombo.select(index < 0 ? 0 : index);
}
}
}








