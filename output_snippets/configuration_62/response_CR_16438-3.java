//<Beginning of snippet n. 0>
public class AlarmManagerService {
    private final Context mContext;
    private final PowerManager.WakeLock mWakeLock;
    private String sys_tz;

    public AlarmManagerService(Context context) {
        mContext = context;
        mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        restoreTimezone();
        loadAndSetTimezone();
    }

    private void restoreTimezone() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("timezone_prefs", Context.MODE_PRIVATE);
        String savedTimezone = sharedPreferences.getString("saved_timezone", null);
        if (isValidTimezone(savedTimezone)) {
            sys_tz = savedTimezone;
            System.setProperty("user.timezone", sys_tz);
            TimeZone.setDefault(TimeZone.getTimeZone(sys_tz));
        }
    }

    private void loadAndSetTimezone() {
        synchronized (this) {
            String timezone = Settings.Global.getString(mContext.getContentResolver(), Settings.Global.TIMEZONE);
            if (isValidTimezone(timezone)) {
                updateTimezone(timezone);
            } else {
                String defaultTimezone = TimeZone.getDefault().getID();
                updateTimezone(defaultTimezone);
                Log.e(TAG, "Invalid or unavailable timezone information. Fallback to default timezone.");
            }
            verifyTimezoneUpdate();
        }
    }

    private void updateTimezone(String timezone) {
        System.setProperty("user.timezone", timezone);
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        sys_tz = timezone; // Set the kernel timezone variable
        saveTimezoneToPreferences(timezone);
    }

    private boolean isValidTimezone(String timezone) {
        String[] availableIDs = TimeZone.getAvailableIDs();
        return timezone != null && !timezone.isEmpty() && Arrays.asList(availableIDs).contains(timezone);
    }

    private void saveTimezoneToPreferences(String timezone) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("timezone_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_timezone", timezone);
        editor.apply();
    }

    private void verifyTimezoneUpdate() {
        String currentTimezone = System.getProperty("user.timezone");
        if (!sys_tz.equals(currentTimezone)) {
            Log.e(TAG, "Failed to update the kernel timezone.");
        }
    }
}
//<End of snippet n. 0>