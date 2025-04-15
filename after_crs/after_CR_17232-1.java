/*Close cursor to avoid excessive JNI references

- Add fix to avoid cursor leak which caused excessive
Jni global references

Change-Id:I0460f9ac2be83a642da196556a7118814fabd6a9*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 29e333a..dda7820 100644

//Synthetic comment -- @@ -2248,6 +2248,10 @@
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







