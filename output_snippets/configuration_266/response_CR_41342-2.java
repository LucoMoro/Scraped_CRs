//<Beginning of snippet n. 0>
private void doUnmount() {
    IMountService mountService = getMountService();
    try {
        if (!isSdCardStatusGood()) {
            Toast.makeText(getActivity(), R.string.unmount_bad_sd_card_text, Toast.LENGTH_SHORT).show();
            mLastClickedMountToggle.setEnabled(true);
            return;
        }
        
        mLastClickedMountToggle.setEnabled(false);
        mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_ejecting_title));
        mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_ejecting_summary));
        
        mountService.unmountVolume(mClickedMountPoint, true, false);
    } catch (RemoteException e) {
        showDialogInner(DLG_ERROR_UNMOUNT);
        mLastClickedMountToggle.setEnabled(true);
    } finally {
        // Moved enabling the button here to ensure consistent UI state
        mLastClickedMountToggle.setEnabled(true);
    }
}
//<End of snippet n. 0>