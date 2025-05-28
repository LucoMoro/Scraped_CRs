
//<Beginning of snippet n. 0>


return mPhone.getServiceState().getState() != ServiceState.STATE_POWER_OFF;
}

public void toggleRadioOnOff() {
enforceModifyPermission();
mPhone.setRadioPower(!isRadioOn());

//<End of snippet n. 0>








