/*41782: Graphical Layout Editor can't handle TabWidget

Change-Id:Ifc450a7ba91714be30a8fac6802fb28f72128822*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 69710e4..62821ae 100644

//Synthetic comment -- @@ -143,7 +143,7 @@

@Override
public String getAttributeValue(String namespace, String localName) {
        if (localName.equals(ATTR_LAYOUT) && mFragmentLayout != null) {
return mFragmentLayout;
}








