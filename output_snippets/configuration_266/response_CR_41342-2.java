//<Beginning of snippet n. 0>
private void doUnmount() {
    if (isSdCardRemoved()) {
        Toast.makeText(getActivity(), R.string.sd_removal_error_text, Toast.LENGTH_SHORT).show();
        return;
    }
    
    IMountService mountService = getMountService();
    if (mountService == null) {
        Toast.makeText(getActivity(), R.string.mount_service_unavailable_text, Toast.LENGTH_SHORT).show();
        return;
    }

    mLastClickedMountToggle.setEnabled(false);
    mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_ejecting_title));
    mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_ejecting_summary));
    
    try {
        mountService.unmountVolume(mClickedMountPoint, true, false);
        // Update UI status on successful unmount if needed
    } catch (RemoteException e) {
        showDialogInner(DLG_ERROR_UNMOUNT);
        Toast.makeText(getActivity(), R.string.unmount_failed_text, Toast.LENGTH_SHORT).show();
    } finally {
        mLastClickedMountToggle.setEnabled(true);
        // Ensure UI reflects the status after the attempt
    }
}
//<End of snippet n. 0>