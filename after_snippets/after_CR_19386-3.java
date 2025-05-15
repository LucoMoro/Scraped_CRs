
//<Beginning of snippet n. 0>


private ArrayList<Preference> mPreferences = new ArrayList<Preference> ();
private int mInitIndex= 0;

    private boolean mFirstResume;
    private Bundle mIcicle;

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
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
}


//<End of snippet n. 0>








