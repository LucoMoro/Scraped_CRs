/*Simple code cleanup

We had a number of constants for attribute values which had the prefix
VALUE_ but which were really attributes and should have the prefix
ATTR_.  We already had the equivalent ATTR_ constants, so get rid of
the VALUE_ constants and clean up the usage slightly.

Change-Id:Icc0f0ad8910695b4ac4904c620f73d6cf4bcacba*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index ae82559..3f48847 100644

//Synthetic comment -- @@ -77,26 +77,22 @@
public static final String ATTR_LAYOUT_MARGIN_TOP = "layout_marginTop";        //$NON-NLS-1$
public static final String ATTR_LAYOUT_MARGIN_BOTTOM = "layout_marginBottom";  //$NON-NLS-1$

public static final String ATTR_LAYOUT_ALIGN_LEFT = "layout_alignLeft";        //$NON-NLS-1$
public static final String ATTR_LAYOUT_ALIGN_RIGHT = "layout_alignRight";      //$NON-NLS-1$
public static final String ATTR_LAYOUT_ALIGN_TOP = "layout_alignTop";          //$NON-NLS-1$
public static final String ATTR_LAYOUT_ALIGN_BOTTOM = "layout_alignBottom";    //$NON-NLS-1$

public static final String ATTR_LAYOUT_ALIGN_PARENT_TOP = "layout_alignParentTop"; //$NON-NLS-1$
public static final String ATTR_LAYOUT_ALIGN_PARENT_BOTTOM = "layout_alignParentBottom"; //$NON-NLS-1$
public static final String ATTR_LAYOUT_ALIGN_PARENT_LEFT = "layout_alignParentLeft";//$NON-NLS-1$
public static final String ATTR_LAYOUT_ALIGN_PARENT_RIGHT = "layout_alignParentRight";   //$NON-NLS-1$
public static final String ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING = "layout_alignWithParentMissing"; //$NON-NLS-1$

public static final String ATTR_LAYOUT_ALIGN_BASELINE = "layout_alignBaseline"; //$NON-NLS-1$

public static final String ATTR_LAYOUT_CENTER_IN_PARENT = "layout_centerInParent"; //$NON-NLS-1$
public static final String ATTR_LAYOUT_CENTER_VERTICAL = "layout_centerVertical"; //$NON-NLS-1$
public static final String ATTR_LAYOUT_CENTER_HORIZONTAL = "layout_centerHorizontal"; //$NON-NLS-1$

public static final String ATTR_LAYOUT_TO_RIGHT_OF = "layout_toRightOf";    //$NON-NLS-1$
public static final String ATTR_LAYOUT_TO_LEFT_OF = "layout_toLeftOf";      //$NON-NLS-1$

public static final String ATTR_LAYOUT_BELOW = "layout_below";              //$NON-NLS-1$
public static final String ATTR_LAYOUT_ABOVE = "layout_above";              //$NON-NLS-1$

//Synthetic comment -- @@ -110,25 +106,6 @@
public static final String VALUE_FALSE= "false";                            //$NON-NLS-1$
public static final String VALUE_N_DP = "%ddp";                             //$NON-NLS-1$

    public static final String VALUE_CENTER_VERTICAL = "centerVertical";        //$NON-NLS-1$
    public static final String VALUE_CENTER_IN_PARENT = "centerInParent";       //$NON-NLS-1$
    public static final String VALUE_CENTER_HORIZONTAL = "centerHorizontal";    //$NON-NLS-1$
    public static final String VALUE_ALIGN_PARENT_RIGHT = "alignParentRight";    //$NON-NLS-1$
    public static final String VALUE_ALIGN_PARENT_LEFT = "alignParentLeft";      //$NON-NLS-1$
    public static final String VALUE_ALIGN_PARENT_BOTTOM = "alignParentBottom";  //$NON-NLS-1$
    public static final String VALUE_ALIGN_PARENT_TOP = "alignParentTop";        //$NON-NLS-1$
    public static final String VALUE_ALIGN_RIGHT = "alignRight";                 //$NON-NLS-1$
    public static final String VALUE_ALIGN_LEFT = "alignLeft";                   //$NON-NLS-1$
    public static final String VALUE_ALIGN_BOTTOM = "alignBottom";               //$NON-NLS-1$
    public static final String VALUE_ALIGN_TOP = "alignTop";                     //$NON-NLS-1$
    public static final String VALUE_ALIGN_BASELINE = "alignBaseline";           //$NON-NLS-1$
    public static final String VAUE_TO_RIGHT_OF = "toRightOf";                   //$NON-NLS-1$
    public static final String VALUE_TO_LEFT_OF = "toLeftOf";                    //$NON-NLS-1$
    public static final String VALUE_BELOW = "below";                            //$NON-NLS-1$
    public static final String VALUE_ABOVE = "above";                            //$NON-NLS-1$
    public static final String VALUE_ALIGN_WITH_PARENT_MISSING =
        "alignWithParentMissing"; //$NON-NLS-1$

// Gravity values. These have the GRAVITY_ prefix in front of value because we already
// have VALUE_CENTER_HORIZONTAL defined for layouts, and its definition conflicts
// (centerHorizontal versus center_horizontal)








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index 17f0f8b..5b3334e 100755

//Synthetic comment -- @@ -28,6 +28,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_IN_PARENT;
//Synthetic comment -- @@ -35,24 +36,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_PARENT_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_PARENT_LEFT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_PARENT_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_PARENT_TOP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_WITH_PARENT_MISSING;
import static com.android.ide.common.layout.LayoutConstants.VALUE_BELOW;
import static com.android.ide.common.layout.LayoutConstants.VALUE_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_CENTER_IN_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.ide.common.layout.LayoutConstants.VAUE_TO_RIGHT_OF;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -105,30 +89,36 @@
@Override
public List<String> getSelectionHint(INode parentNode, INode childNode) {
List<String> infos = new ArrayList<String>(18);
        addAttr(VALUE_ABOVE, childNode, infos);
        addAttr(VALUE_BELOW, childNode, infos);
        addAttr(VALUE_TO_LEFT_OF, childNode, infos);
        addAttr(VAUE_TO_RIGHT_OF, childNode, infos);
        addAttr(VALUE_ALIGN_BASELINE, childNode, infos);
        addAttr(VALUE_ALIGN_TOP, childNode, infos);
        addAttr(VALUE_ALIGN_BOTTOM, childNode, infos);
        addAttr(VALUE_ALIGN_LEFT, childNode, infos);
        addAttr(VALUE_ALIGN_RIGHT, childNode, infos);
        addAttr(VALUE_ALIGN_PARENT_TOP, childNode, infos);
        addAttr(VALUE_ALIGN_PARENT_BOTTOM, childNode, infos);
        addAttr(VALUE_ALIGN_PARENT_LEFT, childNode, infos);
        addAttr(VALUE_ALIGN_PARENT_RIGHT, childNode, infos);
        addAttr(VALUE_ALIGN_WITH_PARENT_MISSING, childNode, infos);
        addAttr(VALUE_CENTER_HORIZONTAL, childNode, infos);
        addAttr(VALUE_CENTER_IN_PARENT, childNode, infos);
        addAttr(VALUE_CENTER_VERTICAL, childNode, infos);

return infos;
}

private void addAttr(String propertyName, INode childNode, List<String> infos) {
        String a = childNode.getStringAttr(ANDROID_URI, ATTR_LAYOUT_PREFIX + propertyName);
if (a != null && a.length() > 0) {
String s = propertyName + ": " + a;
infos.add(s);
}







