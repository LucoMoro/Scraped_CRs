//<Beginning of snippet n. 0>

if (mActive != null && isVisible()) {
    for (int i = 0; i < mAdded.size(); i++) {
        Fragment f = mAdded.get(i);
        if (f != null && !f.mHidden && f.isAdded() && f.getView() != null && f.getView().getVisibility() == View.VISIBLE) {
            if (f.onContextItemSelected(item)) {
                return true;
            }
        }
    }
}

//<End of snippet n. 0>