
//<Beginning of snippet n. 0>


}
mBackup.setChecked(enable);
mAutoRestore.setEnabled(enable);
        if (!enable) {
            mConfigure.setEnabled(enable);
        } else {
            try {
                String transport = mBackupManager.getCurrentTransport();
                Intent configIntent = mBackupManager.getConfigurationIntent(transport);
                if (configIntent != null) {
                    mConfigure.setEnabled(enable);
                }
            } catch (RemoteException e) {
                //To do Nothing
            }
        }
}

@Override

//<End of snippet n. 0>








