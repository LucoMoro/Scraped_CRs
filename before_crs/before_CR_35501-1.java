/*extend PreferenceActivity for title text in a single pane mode

startPreferencePanel() ignores titleText on a single pane mode
this commits extends PreferenceActivity by adding EXTRA_SHOW_FRAGMENT_TITLE_TEXT

Change-Id:I946ce009ee62003695f999f7ce326864a861b194Signed-off-by: Sang Tae Park <pastime1971@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/preference/PreferenceActivity.java b/core/java/android/preference/PreferenceActivity.java
//Synthetic comment -- index 1029161..1821ff1 100644

//Synthetic comment -- @@ -145,6 +145,14 @@

/**
* When starting this activity and using {@link #EXTRA_SHOW_FRAGMENT},
* this extra can also be specify to supply the short title to be shown for
* that fragment.
*/
//Synthetic comment -- @@ -152,6 +160,14 @@
= ":android:show_fragment_short_title";

/**
* When starting this activity, the invoking Intent can contain this extra
* boolean that the header list should not be displayed.  This is most often
* used in conjunction with {@link #EXTRA_SHOW_FRAGMENT} to launch
//Synthetic comment -- @@ -535,6 +551,14 @@
CharSequence initialShortTitleStr = initialShortTitle != 0
? getText(initialShortTitle) : null;
showBreadCrumbs(initialTitleStr, initialShortTitleStr);
}

} else {
//Synthetic comment -- @@ -568,6 +592,14 @@
CharSequence initialShortTitleStr = initialShortTitle != 0
? getText(initialShortTitle) : null;
showBreadCrumbs(initialTitleStr, initialShortTitleStr);
}
} else if (mHeaders.size() > 0) {
setListAdapter(new HeaderAdapter(this, mHeaders));
//Synthetic comment -- @@ -1011,7 +1043,33 @@
intent.putExtra(EXTRA_NO_HEADERS, true);
return intent;
}
    
/**
* Like {@link #startWithFragment(String, Bundle, Fragment, int, int, int)}
* but uses a 0 titleRes.
//Synthetic comment -- @@ -1049,6 +1107,33 @@
}

/**
* Change the base title of the bread crumbs for the current preferences.
* This will normally be called for you.  See
* {@link android.app.FragmentBreadCrumbs} for more information.
//Synthetic comment -- @@ -1238,7 +1323,11 @@
public void startPreferencePanel(String fragmentClass, Bundle args, int titleRes,
CharSequence titleText, Fragment resultTo, int resultRequestCode) {
if (mSinglePane) {
            startWithFragment(fragmentClass, args, resultTo, resultRequestCode, titleRes, 0);
} else {
Fragment f = Fragment.instantiate(this, fragmentClass, args);
if (resultTo != null) {







