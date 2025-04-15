/*Close cursor to avoid excessive JNI references

 - Add fix to avoid cursor leak which caused excessive
   Jni global references

Change-Id:Ia5629dc5233920e0a187884c41c4c0b8672b2bce*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 5e55789..4ef5ad3 100644

//Synthetic comment -- @@ -2242,6 +2242,10 @@
getString(
R.string.choosertitle_sharevia));
}
                            // close the cursor after its used
                            if (c != null) {
                                c.close();
                            }
break;
case R.id.copy_link_context_menu_id:
copy(url);







