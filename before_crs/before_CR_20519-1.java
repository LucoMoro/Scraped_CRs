/*Add drop handler for SlidingDrawer

Add a drop handler for SlidingDrawers such that when dropped from the
palette they create the mandatory handle and content
children. (Without this, you get a rendering error instead.)

Change-Id:Icb521fca9cdbb1da14693d4872ef3fb00187d8f7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index e8f9261..2669a6b 100644

//Synthetic comment -- @@ -45,6 +45,8 @@

public static final String ATTR_TEXT = "text";                      //$NON-NLS-1$
public static final String ATTR_ID = "id";                          //$NON-NLS-1$

public static final String ATTR_LAYOUT_PREFIX = "layout_";          //$NON-NLS-1$
public static final String ATTR_LAYOUT_HEIGHT = "layout_height";    //$NON-NLS-1$
//Synthetic comment -- @@ -117,6 +119,9 @@
/** The fully qualified class name of a TabWidget view */
public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$

/** The fully qualified class name of an AdapterView */
public static final String FQCN_ADAPTER_VIEW = "android.widget.AdapterView"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java
new file mode 100644
//Synthetic comment -- index 0000000..fb0f33e

//Synthetic comment -- @@ -0,0 +1,64 @@







