//<Beginning of snippet n. 0>
if (target != null) {
    String[] abiList = target.getAbiList();
    if (abiList != null && abiList.length > 0) {
        mAbiTypeCombo.setEnabled(abiList.length > 1);
        String abiType = AvdInfo.getPrettyAbiType(mEditAvdInfo != null ? mEditAvdInfo.getAbiType() : null);
        int n = mAbiTypeCombo.getItemCount();

        if (props != null) {
            String sdcard = props.get(AvdManager.AVD_INI_SDCARD_SIZE);
            if (sdcard != null && sdcard.length() > 0) {
                String[] values = new String[2];
                long sdcardSize = AvdManager.parseSdcardSize(sdcard, values);
                if (values != null && values.length == 2) {
                    // Handle values accordingly if needed
                }
            }
        }
    }
}
//<End of snippet n. 0>