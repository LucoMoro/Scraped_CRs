/*Add MipMap to the resource type list.

Change-Id:I1f56ab470874e39dad8659d15a3699b83f1ac658*/




//Synthetic comment -- diff --git a/common/src/com/android/resources/ResourceType.java b/common/src/com/android/resources/ResourceType.java
//Synthetic comment -- index f5f7063..a4d3aa2 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
INTERPOLATOR("interpolator", "Interpolator"), //$NON-NLS-1$
LAYOUT("layout", "Layout"), //$NON-NLS-1$
MENU("menu", "Menu"), //$NON-NLS-1$
    MIPMAP("mipmap", "Mip Map"), //$NON-NLS-1$
PLURALS("plurals", "Plurals"), //$NON-NLS-1$
RAW("raw", "Raw"), //$NON-NLS-1$
STRING("string", "String"), //$NON-NLS-1$








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index e7ea32e..aa5608d 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
* For client wanting to access both new and old (pre API level 5) layout libraries, it is
* important that the following interfaces be used:<br>
* {@link ILegacyPullParser} instead of {@link ILayoutPullParser}<br>
 * {@link LegacyCallback} instead of {@link com.android.ide.common.rendering.api.IProjectCallback}.
* <p/>
* These interfaces will ensure that both new and older Layout libraries can be accessed.
*/







