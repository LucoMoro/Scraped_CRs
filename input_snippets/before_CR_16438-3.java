
//<Beginning of snippet n. 0>


public AlarmManagerService(Context context) {
mContext = context;
mDescriptor = init();
PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);


//<End of snippet n. 0>








