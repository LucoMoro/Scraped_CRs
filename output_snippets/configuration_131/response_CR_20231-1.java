//<Beginning of snippet n. 0>


import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.WindowManager;

public class PUKHandler {
    private static final int MAX_ATTEMPTS = 3;
    private int attemptCount = 0;

    public void handleInput(String input) {
        if (isValidInput(input)) {
            PhoneApp app = PhoneApp.getInstance();
            Subscription subscription = app.getActiveSubscription();

            if (subscription.isPUKLocked()) {
                boolean isMMIHandled = app.phone.handlePinMmi(input);
                logAttempt(input, isMMIHandled);
                
                if (isMMIHandled) {
                    attemptCount = 0;
                    notifyUser("PUK entry accepted.");
                } else {
                    attemptCount++;
                    if (attemptCount >= MAX_ATTEMPTS) {
                        subscription.lock();
                        notifyUser("Too many failed attempts. Subscription locked.");
                    } else {
                        notifyUser("Invalid PUK code entered. Attempts left: " + (MAX_ATTEMPTS - attemptCount));
                    }
                }
            } else {
                notifyUser("Subscription is not PUK locked.");
            }
        } else {
            notifyUser("Invalid input format. Please use **04 or **05 followed by #.");
        }
    }
    
    private boolean isValidInput(String input) {
        return (input.startsWith("**04") || input.startsWith("**05")) && input.endsWith("#") && input.length() >= 6 && input.length() <= 15 && input.replaceAll("[^\\d*#]", "").equals(input);
    }

    private void logAttempt(String input, boolean isSuccess) {
        Log.d("PUKHandler", "Attempt with input: " + input + ", Success: " + isSuccess + ", Attempts made: " + attemptCount + ", Subscription state: " + (attemptCount >= MAX_ATTEMPTS ? "Locked" : "Active"));
    }

    private void notifyUser(String message) {
        Log.i("UserNotification", message);
    }
}

//<End of snippet n. 0>