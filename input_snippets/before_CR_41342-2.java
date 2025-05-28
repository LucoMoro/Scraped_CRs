
//<Beginning of snippet n. 0>


}

private void doUnmount() {
        // Present a toast here
        Toast.makeText(getActivity(), R.string.unmount_inform_text, Toast.LENGTH_SHORT).show();
IMountService mountService = getMountService();
try {
            mLastClickedMountToggle.setEnabled(false);
            mLastClickedMountToggle.setTitle(mResources.getString(R.string.sd_ejecting_title));
            mLastClickedMountToggle.setSummary(mResources.getString(R.string.sd_ejecting_summary));
            mountService.unmountVolume(mClickedMountPoint, true, false);
} catch (RemoteException e) {
// Informative dialog to user that unmount failed.
showDialogInner(DLG_ERROR_UNMOUNT);

//<End of snippet n. 0>








