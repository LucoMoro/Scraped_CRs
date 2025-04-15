/*Improve last editor mode handling

We recently added the ability for the IDE to remember whether you last
edited an XML file in text mode or in graphical mode (see issue
31340). However, this was tracked with a single boolean flag, which
meant that it was an "all or nothing" flag.

However, you may want to always edit string resource files with the
XML editor, but you want to always use the graphical editor for the
manifest file.

In this changeset, the state is kept in a bitmask instead, which
allows us to track the broad editor types separately. With this, new
editors are opened according to the last mode you used for that type
of editor. (Note that it tracks "categories" of editors, not
individual resource types, so for example the editor for color
resources and the editor for state list drawables share the same last
mode as the string resource editor. This is because these editors
share roughly the same graphical editing capabilities.)

(The CL also contains some minor code cleanup.)

Change-Id:I4624dffa2349230684c0558a33081adda8d799b2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index a7dd7d4..a555ef4 100644

//Synthetic comment -- @@ -95,7 +95,7 @@

@Override
public boolean onInitialize(@NonNull String fqcn, @NonNull IClientRulesEngine engine) {
        this.mRulesEngine = engine;

// This base rule can handle any class so we don't need to filter on
// FQCN. Derived classes should do so if they can handle some
//Synthetic comment -- @@ -785,7 +785,7 @@

public EnumPropertyChoiceProvider(Prop property) {
super();
            this.mProperty = property;
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index e6f4a4e..d7a3026 100644

//Synthetic comment -- @@ -631,8 +631,8 @@
private int mPosition;

public MatchPos(int distance, int position) {
            this.mDistance = distance;
            this.mPosition = position;
}

@Override
//Synthetic comment -- @@ -682,10 +682,10 @@

public LinearDropData(List<MatchPos> indexes, int numPositions,
boolean isVertical, int selfPos) {
            this.mIndexes = indexes;
            this.mNumPositions = numPositions;
            this.mVertical = isVertical;
            this.mSelfPos = selfPos;
}

@Override
//Synthetic comment -- @@ -706,7 +706,7 @@
}

private void setCurrX(Integer currX) {
            this.mCurrX = currX;
}

private Integer getCurrX() {
//Synthetic comment -- @@ -714,7 +714,7 @@
}

private void setCurrY(Integer currY) {
            this.mCurrY = currY;
}

private Integer getCurrY() {
//Synthetic comment -- @@ -726,7 +726,7 @@
}

private void setInsertPos(int insertPos) {
            this.mInsertPos = insertPos;
}

private int getInsertPos() {
//Synthetic comment -- @@ -738,7 +738,7 @@
}

private void setWidth(Integer width) {
            this.mWidth = width;
}

private Integer getWidth() {
//Synthetic comment -- @@ -746,7 +746,7 @@
}

private void setHeight(Integer height) {
            this.mHeight = height;
}

private Integer getHeight() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 6098483..fcfdfd1 100644

//Synthetic comment -- @@ -452,11 +452,43 @@
// first page rather than crash the editor load. Logging the error is enough.
AdtPlugin.log(e, "Selecting page '%s' in AndroidXmlEditor failed", defaultPageId);
}
        } else if (AdtPrefs.getPrefs().isLastSwitchedToXml()) {
setActivePage(mTextPageIndex);
}
}

/**
* Removes all the pages from the editor.
*/
//Synthetic comment -- @@ -511,7 +543,8 @@
}
}

        AdtPrefs.getPrefs().setLastSwitchedToXml(newPageIndex == mTextPageIndex);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlDelegate.java
//Synthetic comment -- index b6cbf2c..d9ee8a5 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.resources.ResourceFolderType;

//Synthetic comment -- @@ -235,4 +236,14 @@
public String delegateGetPartName() {
return null;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlEditor.java
//Synthetic comment -- index 7fb820c..be06d38 100755

//Synthetic comment -- @@ -368,6 +368,14 @@
}

@Override
public void initUiRootNode(boolean force) {
if (mDelegate != null) {
mDelegate.delegateInitUiRootNode(force);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditorDelegate.java
//Synthetic comment -- index e10c33b..fc81ac4 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.XmlEditorMultiOutline;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlDelegate;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
//Synthetic comment -- @@ -686,6 +687,11 @@
}

@Override
public void delegatePostPageChange(int newPageIndex) {
super.delegatePostPageChange(newPageIndex);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index 69a6b84..b1bfa88 100644

//Synthetic comment -- @@ -121,6 +121,11 @@
}
}

/**
* Return the root node of the UI element hierarchy, which here
* is the "manifest" node.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java
//Synthetic comment -- index d624cb7..8526ad9 100644

//Synthetic comment -- @@ -18,6 +18,7 @@


import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.build.DebugKeyProvider;
//Synthetic comment -- @@ -52,7 +53,7 @@
public final static String PREFS_MONITOR_DENSITY = AdtPlugin.PLUGIN_ID + ".monitorDensity"; //$NON-NLS-1$

public final static String PREFS_FORMAT_GUI_XML = AdtPlugin.PLUGIN_ID + ".formatXml"; //$NON-NLS-1$
    public final static String PREFS_LAST_SWITCHED_TO_XML = AdtPlugin.PLUGIN_ID + ".lastXml"; //$NON-NLS-1$
public final static String PREFS_USE_CUSTOM_XML_FORMATTER = AdtPlugin.PLUGIN_ID + ".androidForm"; //$NON-NLS-1$

public final static String PREFS_PALETTE_MODE = AdtPlugin.PLUGIN_ID + ".palette"; //$NON-NLS-1$
//Synthetic comment -- @@ -88,7 +89,6 @@
private String mPalette;

private boolean mFormatGuiXml;
    private boolean mLastSwitchedToXml;
private boolean mCustomXmlFormatter;
private boolean mUseEclipseIndent;
private boolean mRemoveEmptyLines;
//Synthetic comment -- @@ -99,6 +99,7 @@
private boolean mLintOnExport;
private AttributeSortOrder mAttributeSort;
private boolean mSharedLayoutEditor;

public static enum BuildVerbosity {
/** Build verbosity "Always". Those messages are always displayed, even in silent mode */
//Synthetic comment -- @@ -199,8 +200,8 @@
mFormatGuiXml = mStore.getBoolean(PREFS_FORMAT_GUI_XML);
}

        if (property == null || PREFS_LAST_SWITCHED_TO_XML.equals(property)) {
            mLastSwitchedToXml = mStore.getBoolean(PREFS_LAST_SWITCHED_TO_XML);
}

if (property == null || PREFS_USE_CUSTOM_XML_FORMATTER.equals(property)) {
//Synthetic comment -- @@ -502,22 +503,39 @@
}
}

    /** Returns whether the most recent page switch was to XML
     * @return whether the most recent page switch was to XML */
    public boolean isLastSwitchedToXml() {
        return mLastSwitchedToXml;
}

/**
     * Set whether the most recent page switch was to XML
*
     * @param xml whether the last manual page switch was to XML
*/
    public void setLastSwitchedToXml(boolean xml) {
        if (xml != mLastSwitchedToXml) {
            mLastSwitchedToXml = xml;
IPreferenceStore store = AdtPlugin.getDefault().getPreferenceStore();
            store.setValue(PREFS_LINT_ON_SAVE, xml);
}
}
}







