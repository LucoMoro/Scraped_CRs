/*Settings: JAVACRASH occurs on 'com.android.settings' when editing tag in Soical Gallery

The maxlength of edittext is set to constrain the text length to the specified number,
When too long string was set,edittext will truncation string.When invoke setSelection method to
move the cursor to the length of this string,system will occur the exception of IndexOutOfBoundsException.

Change-Id:I7ccd9f0cb5ed77d90b79c1581388bdd08380745dAuthor: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: b619 <b619@borqs.com>
Signed-off-by: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57769*/




//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/UserDictionaryAddWordActivity.java b/src/com/android/settings/inputmethod/UserDictionaryAddWordActivity.java
//Synthetic comment -- index e52ab7a..5f49bcf 100644

//Synthetic comment -- @@ -54,6 +54,9 @@
final Bundle args = intent.getExtras();
args.putInt(UserDictionaryAddWordContents.EXTRA_MODE, mode);

        int maxlength = getResources().getInteger(R.integer.maximum_user_dictionary_word_length);
        args.putInt(UserDictionaryAddWordContents.EXTRA_LENGTH, maxlength);

if (null != savedInstanceState) {
// Override options if we have a saved state.
args.putAll(savedInstanceState);








//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/UserDictionaryAddWordContents.java b/src/com/android/settings/inputmethod/UserDictionaryAddWordContents.java
//Synthetic comment -- index e46b19c..03d5e00 100644

//Synthetic comment -- @@ -43,6 +43,7 @@
public static final String EXTRA_WORD = "word";
public static final String EXTRA_SHORTCUT = "shortcut";
public static final String EXTRA_LOCALE = "locale";
    public static final String EXTRA_LENGTH = "maxlength";

public static final int MODE_EDIT = 0;
public static final int MODE_INSERT = 1;
//Synthetic comment -- @@ -55,14 +56,17 @@
private String mLocale;
private final String mOldWord;
private final String mOldShortcut;
    int mMaxLength;

/* package */ UserDictionaryAddWordContents(final View view, final Bundle args) {
mWordEditText = (EditText)view.findViewById(R.id.user_dictionary_add_word_text);
mShortcutEditText = (EditText)view.findViewById(R.id.user_dictionary_add_shortcut);
final String word = args.getString(EXTRA_WORD);

        mMaxLength = args.getInt(EXTRA_LENGTH);
if (null != word) {
mWordEditText.setText(word);
            mWordEditText.setSelection(word.length() < mMaxLength ? word.length() : mMaxLength);
}
final String shortcut = args.getString(EXTRA_SHORTCUT);
if (null != shortcut && null != mShortcutEditText) {








//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/UserDictionaryAddWordFragment.java b/src/com/android/settings/inputmethod/UserDictionaryAddWordFragment.java
//Synthetic comment -- index 97ffa19..53cd212 100644

//Synthetic comment -- @@ -61,7 +61,10 @@
mRootView = inflater.inflate(R.layout.user_dictionary_add_word_fullscreen, null);
mIsDeleting = false;
if (null == mContents) {
            Bundle targs = getArguments();
            int maxlength = getResources().getInteger(R.integer.maximum_user_dictionary_word_length);
            targs.putInt(UserDictionaryAddWordContents.EXTRA_LENGTH, maxlength);
            mContents = new UserDictionaryAddWordContents(mRootView, targs);
}
return mRootView;
}







