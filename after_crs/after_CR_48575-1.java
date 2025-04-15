/*41782: Graphical Layout Editor can't handle TabWidget. DO NOT MERGE

Change-Id:Ia14920bc0f320b51e01b9ebe01d8ce524a650b49*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 69710e4..62821ae 100644

//Synthetic comment -- @@ -143,7 +143,7 @@

@Override
public String getAttributeValue(String namespace, String localName) {
        if (ATTR_LAYOUT.equals(localName) && mFragmentLayout != null) {
return mFragmentLayout;
}








