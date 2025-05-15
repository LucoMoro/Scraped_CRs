//<Beginning of snippet n. 0>

public void onKeyPress() {
    checkModemPowerSaveStatus();
}

private void checkModemPowerSaveStatus() {
    if (isModemInPowerSaveMode()) {
        wakeUpModem();
    }
}

public void toggleRadioOnOff() {
    enforceModifyPermission();
    checkModemPowerSaveStatus();
    mPhone.setRadioPower(!isRadioOn());
}

//<End of snippet n. 0>