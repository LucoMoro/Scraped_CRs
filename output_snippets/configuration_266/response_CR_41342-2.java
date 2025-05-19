//<Beginning of snippet n. 0>
private void doUnmount() {
    IMountService mountService = getMountService();
    if (mountService == null) {
        showDialogInner(DLG_ERROR_UNMOUNT);
        resetUnmountUIState();
        return;
    }

    try {
        String sdCardStatus = mountService.getVolumeState(mClickedMountPoint);
        if ("bad removal".equals(sdCardStatus)) {
            showDialogInner(DLG_ERROR_BAD_REMOVAL); 
            resetUnmountUIState();
            return;
        }

        mLastClickedMountToggle.setEnabled(false);
        mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_ejecting_title));
        mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_ejecting_summary));
        mountService.unmountVolume(mClickedMountPoint, true, false);
    } catch (RemoteException e) {
        showDialogInner(DLG_ERROR_UNMOUNT_REMOTE);
        resetUnmountUIState();
        return;
    } catch (Exception e) {
        showDialogInner(DLG_ERROR_UNMOUNT);
        resetUnmountUIState();
        return;
    }
    resetUnmountUIState();
}

private void resetUnmountUIState() {
    mLastClickedMountToggle.setEnabled(true);
    mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_default_title));
    mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_default_summary));
}
//<End of snippet n. 0>