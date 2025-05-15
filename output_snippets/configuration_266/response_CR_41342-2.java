//<Beginning of snippet n. 0>
private void doUnmount() {
    IMountService mountService = getMountService();
    
    // Check SD card status before unmounting
    int sdCardStatus = mountService.getVolumeState(mClickedMountPoint);
    if (sdCardStatus == MountService.STATE_BAD_REMOVAL) {
        // Notify user that unmount cannot proceed
        Toast.makeText(getActivity(), R.string.sd_bad_removal_text, Toast.LENGTH_SHORT).show();
        resetUI();
        return;
    }
    
    // Present a toast here
    Toast.makeText(getActivity(), R.string.unmount_inform_text, Toast.LENGTH_SHORT).show();
    try {
        mLastClickedMountToggle.setEnabled(false);
        mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_ejecting_title));
        mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_ejecting_summary));
        mountService.unmountVolume(mClickedMountPoint, true, false);
    } catch (RemoteException e) {
        // Informative dialog to user that unmount failed.
        showDialogInner(DLG_ERROR_UNMOUNT);
        resetUI();
    }
}

private void resetUI() {
    mLastClickedMountToggle.setEnabled(true);
    mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_eject_title));
    mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_eject_summary));
}
//<End of snippet n. 0>