/*Added receiver in MountService to detect locale changes.

In the settings application, Settings->Storage, the description
string from storage volumes are displayed. The storage volumes
are parsed upon creation of MountService and since the MountService
does not listen for locale changes, this description is not
updated when changing the language.

By adding a receiver listening and re-reading the storage volumes
upon locale changes (ACTION_LOCALE_CHANGED) this string is
localized correctly.

Change-Id:If377a4e1e2cb5c0dcf1f9d7f806065918bcdda5b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 5425813..82b287f 100644

//Synthetic comment -- @@ -162,6 +162,14 @@
public static final int VolumeBadRemoval               = 632;
}

    private final BroadcastReceiver mLocaleChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            synchronized(mVolumes) {
                readStorageList(context.getResources());
            }
        }
    };

private Context                               mContext;
private NativeDaemonConnector                 mConnector;
private final ArrayList<StorageVolume>        mVolumes = new ArrayList<StorageVolume>();
//Synthetic comment -- @@ -1056,6 +1064,9 @@
private static final String TAG_STORAGE = "storage";

private void readStorageList(Resources resources) {
        mVolumes.clear();
        mVolumeMap.clear();

int id = com.android.internal.R.xml.storage_list;
XmlResourceParser parser = resources.getXml(id);
AttributeSet attrs = Xml.asAttributeSet(parser);
//Synthetic comment -- @@ -1195,6 +1206,9 @@
if (WATCHDOG_ENABLE) {
Watchdog.getInstance().addMonitor(this);
}

        mContext.registerReceiver(mLocaleChangedReceiver,
                new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
}

/**







