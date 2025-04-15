/*Implementing onResume in Additional call setting.

Implementing onResume to handle initialization in
GsmUmtsAdditionalCallOption since it extends
TimeConsumingPreferenceActivity which relies on onResume
to maintain its foreground state.
TimeConsumingPreferenceActivity reading dialog will then
block the settings for editing while reading the whole time.

Change-Id:Id97ab6aaf857b08b8159afdc9c15732d71253670*/




//Synthetic comment -- diff --git a/src/com/android/phone/GsmUmtsAdditionalCallOptions.java b/src/com/android/phone/GsmUmtsAdditionalCallOptions.java
//Synthetic comment -- index 69a025c..8868d9c 100644

//Synthetic comment -- @@ -24,6 +24,9 @@
private ArrayList<Preference> mPreferences = new ArrayList<Preference> ();
private int mInitIndex= 0;

    private boolean mFirstResume;
    private Bundle mIcicle;

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -37,22 +40,37 @@
mPreferences.add(mCLIRButton);
mPreferences.add(mCWButton);

        // we wait to do the initialization until onResume so that the
        // TimeConsumingPreferenceActivity dialog can display as it
        // relies on onResume / onPause to maintain its foreground state.
        mFirstResume = true;
        mIcicle = icicle;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mFirstResume) {
            if (mIcicle == null) {
                if (DBG) Log.d(LOG_TAG, "start to init ");
mCLIRButton.init(this, false);
            } else {
                if (DBG) Log.d(LOG_TAG, "restore stored states");
                mInitIndex = mPreferences.size();
                mCLIRButton.init(this, true);
                mCWButton.init(this, true);
                int[] clirArray = mIcicle.getIntArray(mCLIRButton.getKey());
                if (clirArray != null) {
                    if (DBG) Log.d(LOG_TAG, "onCreate:  clirArray[0]="
                            + clirArray[0] + ", clirArray[1]=" + clirArray[1]);
                    mCLIRButton.handleGetCLIRResult(clirArray);
                } else {
                    mCLIRButton.init(this, false);
                }
}
            mFirstResume = false;
            mIcicle = null;
}

ActionBar actionBar = getActionBar();







