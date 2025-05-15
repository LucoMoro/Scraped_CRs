
//<Beginning of snippet n. 0>


public AlarmManagerService(Context context) {
mContext = context;
mDescriptor = init();

        // We have to set current TimeZone info to kernel
        // because kernel doesn't keep this after reboot
        String tz = SystemProperties.get(TIMEZONE_PROPERTY);
        if (tz != null) {
            setTimeZone(tz);
        }

PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);


//<End of snippet n. 0>








