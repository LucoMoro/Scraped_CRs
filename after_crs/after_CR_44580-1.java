/*Align "from code" parent-child preference flow with "from XML"

Add setDependency() call to make "from code" parent-child
behavior matched the "from XML" one.

Change-Id:I9e19a8bd16f69fb3101928831831103719a03a04Signed-off-by: Joseph Judistira <joseph.judistira@gmail.com>*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/preference/PreferencesFromCode.java b/samples/ApiDemos/src/com/example/android/apis/preference/PreferencesFromCode.java
//Synthetic comment -- index 884991b..f4bb310 100644

//Synthetic comment -- @@ -32,17 +32,18 @@

public class PreferencesFromCode extends PreferenceActivity {

    private static final String PARENT_CHECKBOX_PREFERENCE = "parent_checkbox_preference";

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        setPreferenceScreen(root);
        populatePreferenceHierarchy(root);
}

    private void populatePreferenceHierarchy(PreferenceScreen root) {
// Inline preferences
PreferenceCategory inlinePrefCat = new PreferenceCategory(this);
inlinePrefCat.setTitle(R.string.inline_preferences);
//Synthetic comment -- @@ -132,6 +133,7 @@
parentCheckBoxPref.setTitle(R.string.title_parent_preference);
parentCheckBoxPref.setSummary(R.string.summary_parent_preference);
prefAttrsCat.addPreference(parentCheckBoxPref);
        parentCheckBoxPref.setKey(PARENT_CHECKBOX_PREFERENCE);

// Visual child toggle preference
// See res/values/attrs.xml for the <declare-styleable> that defines
//Synthetic comment -- @@ -144,8 +146,7 @@
a.getResourceId(R.styleable.TogglePrefAttrs_android_preferenceLayoutChild,
0));
prefAttrsCat.addPreference(childCheckBoxPref);
        childCheckBoxPref.setDependency(PARENT_CHECKBOX_PREFERENCE);
a.recycle();
}
}







