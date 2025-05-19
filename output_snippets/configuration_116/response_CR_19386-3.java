//<Beginning of snippet n. 0>


private ArrayList<Preference> mPreferences = new ArrayList<Preference>();

@Override
protected void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    mPreferences.add(mCLIRButton);
    mPreferences.add(mCWButton);
}

@Override
protected void onResume() {
    super.onResume();
    if (DBG) Log.d(LOG_TAG, "start to init ");

    mCLIRButton.init(this, false);
    mCWButton.init(this, true);

    if (DBG) Log.d(LOG_TAG, "restoring stored states");

    int[] clirArray = getIntent().getIntArrayExtra(mCLIRButton.getKey());
    if (clirArray != null) {
        if (DBG) Log.d(LOG_TAG, "onResume: clirArray[0]=" + clirArray[0] + ", clirArray[1]=" + clirArray[1]);
        mCLIRButton.handleGetCLIRResult(clirArray);
    }
}

//<End of snippet n. 0>