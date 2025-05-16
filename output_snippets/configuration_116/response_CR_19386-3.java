//<Beginning of snippet n. 0>
private ArrayList<Preference> mPreferences = new ArrayList<Preference>();
private int mInitIndex = 0;

@Override
protected void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    mPreferences.add(mCLIRButton);
    mPreferences.add(mCWButton);

    if (icicle == null) {
        if (DBG) Log.d(LOG_TAG, "start to init ");
        mCLIRButton.init(this, false);
    } else {
        if (DBG) Log.d(LOG_TAG, "restore stored states");
        mInitIndex = mPreferences.size();
        mCLIRButton.init(this, true);
        mCWButton.init(this, true);
        int[] clirArray = icicle.getIntArray(mCLIRButton.getKey());
        if (clirArray != null) {
            if (DBG) Log.d(LOG_TAG, "onCreate:  clirArray[0]=" + clirArray[0] + ", clirArray[1]=" + clirArray[1]);
            mCLIRButton.handleGetCLIRResult(clirArray);
        } else {
            mCLIRButton.init(this, false);
        }
    }
}

@Override
protected void onResume() {
    super.onResume();
    if (mInitIndex < mPreferences.size()) {
        mPreferences.get(mInitIndex).setEnabled(false);
    } else {
        for (Preference preference : mPreferences) {
            preference.setEnabled(true);
        }
    }
}
//<End of snippet n. 0>