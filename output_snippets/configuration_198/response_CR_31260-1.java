//<Beginning of snippet n. 0>


if (mActive != null) {
    for (int i = 0; i < mAdded.size(); i++) {
        Fragment f = mAdded.get(i);
        if (f != null && !f.mHidden && f.isVisible() && f.isAdded() && f.isResumed()) {
            if (f.onContextItemSelected(item)) {
                return true;
            }
        }
    }
}

//<End of snippet n. 0>