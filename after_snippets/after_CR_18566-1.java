
//<Beginning of snippet n. 0>


return mPhone.getServiceState().getState() != ServiceState.STATE_POWER_OFF;
}

    public boolean isModemPowerSave() {
        return mPhone.isModemPowerSave();
    }

public void toggleRadioOnOff() {
enforceModifyPermission();
mPhone.setRadioPower(!isRadioOn());

//<End of snippet n. 0>








