//<Beginning of snippet n. 0>
public void keypressDetected() {
    if (isModemInPowerSaveMode()) {
        try {
            wakeModem();
            if (!isModemInPowerSaveMode()) {
                return;
            }
            // Adding explicit state check after waking the modem
            confirmModemState();
        } catch (RadioPowerException e) {
            handleError(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            handleError(e);
        } catch (Exception e) {
            handleError(e);
        }
    }
}

private boolean isModemInPowerSaveMode() {
    ServiceState state = mPhone.getServiceState();
    return state != null && state.getState() == ServiceState.STATE_POWER_SAVE;
}

private void wakeModem() throws RadioPowerException, InterruptedException {
    if (!isRadioOn()) {
        toggleRadioOnOff();
        confirmModemState();
    }
}

private void confirmModemState() throws InterruptedException {
    int retries = 5;
    while (retries > 0 && isModemInPowerSaveMode()) {
        Thread.sleep(200); // Wait before checking state again
        retries--;
    }
    if (isModemInPowerSaveMode()) {
        throw new RadioPowerException("Failed to wake modem from power save mode.");
    }
}

private void toggleRadioOnOff() {
    enforceModifyPermission();
    mPhone.setRadioPower(!isRadioOn());
}

private void handleError(Exception e) {
    Log.e("ModemError", "Error occurred: " + e.getMessage());
}

class RadioPowerException extends Exception {
    public RadioPowerException(String message) {
        super(message);
    }
}
//<End of snippet n. 0>