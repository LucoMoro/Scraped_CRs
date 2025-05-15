//<Beginning of snippet n. 0>
}

// select the abi type
if (target != null && target.getAbiList() != null && target.getAbiList().length > 0) {
    mAbiTypeCombo.setEnabled(target.getAbiList().length > 1);
    String abiType = AvdInfo.getPrettyAbiType(mEditAvdInfo != null ? mEditAvdInfo.getAbiType() : null);
    int n = mAbiTypeCombo.getItemCount();

    sdcard = props != null ? props.get(AvdManager.AVD_INI_SDCARD_SIZE) : null;
    if (sdcard != null && sdcard.length() > 0) {
        String[] values = new String[2];
        long sdcardSize = AvdManager.parseSdcardSize(sdcard, values);
    }
}
//<End of snippet n. 0>