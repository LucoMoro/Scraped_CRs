/*Fixing memory leak in PreferenceScreen.

Every time the PreferenceScreen is displayed a new ListView is
created and bound to the adapter. As the same adapter is used during
the lifetime of PreferenceScreen and the listviews never gets
unbound, the adapter will contain a list of unused views. The old view
should be unbound from adapter when we create a new view.

Change-Id:I13e2d0dc79c8ff79b58efa650653e3f84c6e53c5*/
//Synthetic comment -- diff --git a/core/java/android/preference/PreferenceScreen.java b/core/java/android/preference/PreferenceScreen.java
//Synthetic comment -- index 95e54324..fae5f1a 100644

//Synthetic comment -- @@ -80,6 +80,8 @@
private ListAdapter mRootAdapter;

private Dialog mDialog;

/**
* Do NOT use this constructor, use {@link PreferenceManager#createPreferenceScreen(Context)}.
//Synthetic comment -- @@ -145,15 +147,18 @@

private void showDialog(Bundle state) {
Context context = getContext();
        ListView listView = new ListView(context);
        bind(listView);

// Set the title bar if title is available, else no title bar
final CharSequence title = getTitle();
Dialog dialog = mDialog = new Dialog(context, TextUtils.isEmpty(title)
? com.android.internal.R.style.Theme_NoTitleBar
: com.android.internal.R.style.Theme);
        dialog.setContentView(listView);
if (!TextUtils.isEmpty(title)) {
dialog.setTitle(title);
}







