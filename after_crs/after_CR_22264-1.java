/*Dealing with with TZ handler in thread-safe manner.

This is a utility class, but it uses shared static field mHandler
accross method calls. The content of the field is not immutable,
but it can be changed. However, the changes are not protected by
any synchronization, so race conditions might occure. This patch
adds a synchronization of mHandler creation and subsequent usage
when necessary, so it is ensured that methods are alwasy called
upon the right instance of handler.

Change-Id:Ia00a8a331da62f78b03c1e89e3ec03d8eee50c94*/




//Synthetic comment -- diff --git a/core/java/android/util/CalendarUtils.java b/core/java/android/util/CalendarUtils.java
//Synthetic comment -- index 1b2a894..9511963 100644

//Synthetic comment -- @@ -49,6 +49,9 @@
private static final String[] TIMEZONE_INSTANCES_ARGS =
{ CalendarCache.TIMEZONE_KEY_INSTANCES };

        // Protects initializations and subsequent usages of 'mHandler';
        private static final Object HANDLER_INIT_LOCK = new Object();

private static StringBuilder mSB = new StringBuilder(50);
private static Formatter mF = new Formatter(mSB, Locale.getDefault());
private volatile static boolean mFirstTZRequest = true;
//Synthetic comment -- @@ -215,29 +218,33 @@

// Update the db
ContentValues values = new ContentValues();
                synchronized (HANDLER_INIT_LOCK) {
                    if (mHandler != null) {
                        mHandler.cancelOperation(mToken);
                    }
}

                synchronized (HANDLER_INIT_LOCK) {
                    mHandler = new AsyncTZHandler(context.getContentResolver());

                    // skip 0 so query can use it
                    if (++mToken == 0) {
                        mToken = 1;
                    }

                    // Write the use home tz setting
                    values.put(CalendarCache.VALUE, mUseHomeTZ ? CalendarCache.TIMEZONE_TYPE_HOME
                            : CalendarCache.TIMEZONE_TYPE_AUTO);
                    mHandler.startUpdate(mToken, null, CalendarCache.URI, values, CalendarCache.WHERE,
                            TIMEZONE_TYPE_ARGS);

                    // If using a home tz write it to the db
                    if (mUseHomeTZ) {
                        ContentValues values2 = new ContentValues();
                        values2.put(CalendarCache.VALUE, mHomeTZ);
                        mHandler.startUpdate(mToken, null, CalendarCache.URI, values2,
                                CalendarCache.WHERE, TIMEZONE_INSTANCES_ARGS);
                    }
}
}
}
//Synthetic comment -- @@ -266,15 +273,17 @@
mUseHomeTZ = prefs.getBoolean(KEY_HOME_TZ_ENABLED, false);
mHomeTZ = prefs.getString(KEY_HOME_TZ, Time.getCurrentTimezone());

                    synchronized (HANDLER_INIT_LOCK) {
                        // When the async query returns it should synchronize on
                        // mTZCallbacks, update mUseHomeTZ, mHomeTZ, and the
                        // preferences, set mTZQueryInProgress to false, and call all
                        // the runnables in mTZCallbacks.
                        if (mHandler == null) {
                            mHandler = new AsyncTZHandler(context.getContentResolver());
                        }
                        mHandler.startQuery(0, context, CalendarCache.URI, CalendarCache.POJECTION,
                                null, null, null);
}
}
if (mTZQueryInProgress) {
mTZCallbacks.add(callback);







