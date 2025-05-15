//<Beginning of snippet n. 0>
private void doUnmount() {
    IMountService mountService = getMountService();
    
    try {
        if (mountService.getVolumeState(mClickedMountPoint).equals(Environment.MEDIA_MOUNTED)) {
            mLastClickedMountToggle.setEnabled(false);
            mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_ejecting_title));
            mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_ejecting_summary));
            mountService.unmountVolume(mClickedMountPoint, true, false);
        } else {
            Toast.makeText(getActivity(), R.string.unmount_bad_removal_text, Toast.LENGTH_SHORT).show();
        }
    } catch (RemoteException e) {
        showDialogInner(DLG_ERROR_UNMOUNT);
    } finally {
        mLastClickedMountToggle.setEnabled(true);
        mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_eject_title));
        mLastClickedMountToggle.setSummary("");
    }
//<End of snippet n. 0>