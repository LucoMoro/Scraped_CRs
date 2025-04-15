/*extend PreferenceActivity for title text in a single pane mode

startPreferencePanel() ignores titleText on a single pane mode
this commits extends PreferenceActivity by adding EXTRA_SHOW_FRAGMENT_TITLE_TEXT

Change-Id:I946ce009ee62003695f999f7ce326864a861b194Signed-off-by: Sang Tae Park <pastime1971@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/preference/PreferenceActivity.java b/core/java/android/preference/PreferenceActivity.java
//Synthetic comment -- index 1029161..1821ff1 100644

//Synthetic comment -- @@ -145,6 +145,14 @@

/**
* When starting this activity and using {@link #EXTRA_SHOW_FRAGMENT},
     * this extra can also be specify to supply the title text to be shown for
     * that fragment.
     */
    public static final String EXTRA_SHOW_FRAGMENT_TITLE_TEXT
            = ":android:show_fragment_title_text";

    /**
     * When starting this activity and using {@link #EXTRA_SHOW_FRAGMENT},
* this extra can also be specify to supply the short title to be shown for
* that fragment.
*/
//Synthetic comment -- @@ -152,6 +160,14 @@
= ":android:show_fragment_short_title";

/**
     * When starting this activity and using {@link #EXTRA_SHOW_FRAGMENT},
     * this extra can also be specify to supply the short title text to be shown for
     * that fragment.
     */
    public static final String EXTRA_SHOW_FRAGMENT_SHORT_TITLE_TEXT
            = ":android:show_fragment_short_title_text";

    /**
* When starting this activity, the invoking Intent can contain this extra
* boolean that the header list should not be displayed.  This is most often
* used in conjunction with {@link #EXTRA_SHOW_FRAGMENT} to launch
//Synthetic comment -- @@ -535,6 +551,14 @@
CharSequence initialShortTitleStr = initialShortTitle != 0
? getText(initialShortTitle) : null;
showBreadCrumbs(initialTitleStr, initialShortTitleStr);
                } else {
                    CharSequence initialTitleStr = getIntent().getStringExtra(
                            EXTRA_SHOW_FRAGMENT_TITLE_TEXT);
                    if ( initialTitleStr != null ) {
                        CharSequence initialShortTitleStr = getIntent().getStringExtra(
                                EXTRA_SHOW_FRAGMENT_SHORT_TITLE_TEXT);
                        showBreadCrumbs(initialTitleStr, initialShortTitleStr);
                    }
}

} else {
//Synthetic comment -- @@ -568,6 +592,14 @@
CharSequence initialShortTitleStr = initialShortTitle != 0
? getText(initialShortTitle) : null;
showBreadCrumbs(initialTitleStr, initialShortTitleStr);
            } else {
                CharSequence initialTitleStr = getIntent().getStringExtra(
                        EXTRA_SHOW_FRAGMENT_TITLE_TEXT);
                if ( initialTitleStr != null ) {
                    CharSequence initialShortTitleStr = getIntent().getStringExtra(
                            EXTRA_SHOW_FRAGMENT_SHORT_TITLE_TEXT);
                    showBreadCrumbs(initialTitleStr, initialShortTitleStr);
                }
}
} else if (mHeaders.size() > 0) {
setListAdapter(new HeaderAdapter(this, mHeaders));
//Synthetic comment -- @@ -1011,7 +1043,33 @@
intent.putExtra(EXTRA_NO_HEADERS, true);
return intent;
}

    /**
     * Called by {@link #startWithFragment(String, Bundle, Fragment, int, CharSequence, CharSequence)}
     * when in single-pane mode, to build an Intent to launch a new activity
     * showing the selected fragment.  The default implementation constructs an
     * Intent that re-launches the current activity with the appropriate
     * arguments to display the fragment.
     * 
     * @param fragmentName The name of the fragment to display.
     * @param args Optional arguments to supply to the fragment.
     * @param titleText Optional text of the title of this fragment.
     * @param shortTitleText Optional text of short title to show for this item.
     * @return Returns an Intent that can be launched to display the given
     * fragment.
     */
    public Intent onBuildStartFragmentIntent(String fragmentName, Bundle args,
            CharSequence titleText, CharSequence shortTitleText) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(this, getClass());
        intent.putExtra(EXTRA_SHOW_FRAGMENT, fragmentName);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, args);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_TITLE_TEXT, titleText);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_SHORT_TITLE_TEXT, shortTitleText);
        intent.putExtra(EXTRA_NO_HEADERS, true);
        return intent;
    }

/**
* Like {@link #startWithFragment(String, Bundle, Fragment, int, int, int)}
* but uses a 0 titleRes.
//Synthetic comment -- @@ -1049,6 +1107,33 @@
}

/**
     * Start a new instance of this activity, showing only the given
     * preference fragment.  When launched in this mode, the header list
     * will be hidden and the given preference fragment will be instantiated
     * and fill the entire activity.
     *
     * @param fragmentName The name of the fragment to display.
     * @param args Optional arguments to supply to the fragment.
     * @param resultTo Option fragment that should receive the result of
     * the activity launch.
     * @param resultRequestCode If resultTo is non-null, this is the request
     * code in which to report the result.
     * @param titleText text of string to display for the title of
     * this set of preferences.
     * @param shortTitleText text of string to display for the short title of
     * this set of preferences.
     */
    public void startWithFragment(String fragmentName, Bundle args, Fragment resultTo,
            int resultRequestCode, CharSequence titleText, CharSequence shortTitleText) {
        Intent intent = onBuildStartFragmentIntent(fragmentName, args, titleText, shortTitleText);
        if (resultTo == null) {
            startActivity(intent);
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode);
        }
    }

    /**
* Change the base title of the bread crumbs for the current preferences.
* This will normally be called for you.  See
* {@link android.app.FragmentBreadCrumbs} for more information.
//Synthetic comment -- @@ -1238,7 +1323,11 @@
public void startPreferencePanel(String fragmentClass, Bundle args, int titleRes,
CharSequence titleText, Fragment resultTo, int resultRequestCode) {
if (mSinglePane) {
            if (titleRes == 0 && titleText != null) {
                startWithFragment(fragmentClass, args, resultTo, resultRequestCode, titleText, null);
            } else {
                startWithFragment(fragmentClass, args, resultTo, resultRequestCode, titleRes, 0);
            }
} else {
Fragment f = Fragment.instantiate(this, fragmentClass, args);
if (resultTo != null) {







