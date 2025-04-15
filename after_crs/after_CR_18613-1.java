/*Fixing a system server crash.

The system server crashed when doing searches on Market
on some devices.

The search dialog is beeing asked to show non existing
resources as of:

android.content.res.Resources$NotFoundException: String resource ID #0x7f080143
at android.content.res.Resources.getText(Resources.java:200)
at android.content.res.Resources.getString(Resources.java:253)
at android.content.Context.getString(Context.java:149)
at android.app.SearchDialog.updateQueryHint(SearchDialog.java:674)
at android.app.SearchDialog.updateUI(SearchDialog.java:528)
at android.app.SearchDialog.show(SearchDialog.java:393)
at android.app.SearchDialog.doShow(SearchDialog.java:319)
at android.app.SearchDialog.show(SearchDialog.java:251)
...

This modification adds a catcher for the exception.

Change-Id:I9fd1c3e32f654bd420fd382b7a3f879dd421c3e7*/




//Synthetic comment -- diff --git a/core/java/android/app/SearchDialog.java b/core/java/android/app/SearchDialog.java
//Synthetic comment -- index 2fb746c..e56a002 100644

//Synthetic comment -- @@ -572,7 +572,11 @@
if (mSearchable != null) {
int hintId = mSearchable.getHintId();
if (hintId != 0) {
                    try {
                        hint = mActivityContext.getString(hintId);
                    } catch (Resources.NotFoundException e) {
                        Log.w(LOG_TAG, "Could not find hint string: " + e);
                    }
}
}
mSearchAutoComplete.setHint(hint);







