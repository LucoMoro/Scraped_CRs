/*Prevent potential NPE

Change-Id:I4fa69bac844ebb0561382c8639b571b425306fd3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index c30574b..90ea2a5 100644

//Synthetic comment -- @@ -120,7 +120,7 @@
}

// Handle unicode escapes
        if (value.indexOf('\\') != -1) {
value = AdtUtils.replaceUnicodeEscapes(value);
}








