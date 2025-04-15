/*added final modifiers to variables to increase performance slightly

Change-Id:I857cf9785419247fd4e7a6734964165e82357e4b*/




//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/PercentageBar.java b/src/com/android/settings/fuelgauge/PercentageBar.java
//Synthetic comment -- index 1c4478b..ae249be 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
int lastWidth = -1;

@Override
    public void draw(final Canvas canvas) {
if (lastWidth == -1) {
lastWidth = getBarWidth();
bar.setBounds(0, 0, lastWidth, bar.getIntrinsicHeight());
//Synthetic comment -- @@ -45,18 +45,18 @@
}

@Override
    public void setAlpha(final int alpha) {
// Ignore
}

@Override
    public void setColorFilter(final ColorFilter cf) {
// Ignore
}

private int getBarWidth() {
        final int width = (int) ((this.getBounds().width() * percent) / 100);
        final int intrinsicWidth = bar.getIntrinsicWidth();
return Math.max(width, intrinsicWidth);
}









//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/PowerGaugePreference.java b/src/com/android/settings/fuelgauge/PowerGaugePreference.java
//Synthetic comment -- index 68f294c..cc66d1d 100644

//Synthetic comment -- @@ -37,12 +37,12 @@
public class PowerGaugePreference extends Preference {

private Drawable mIcon;
    private final PercentageBar mGauge;
private double mValue;
    private final BatterySipper mInfo;
private double mPercent;

    public PowerGaugePreference(final Context context, final Drawable icon, final BatterySipper info) {
super(context);
setLayoutResource(R.layout.preference_powergauge);
mIcon = icon;
//Synthetic comment -- @@ -55,12 +55,12 @@
* Sets the width of the gauge in percentage (0 - 100)
* @param percent
*/
    void setGaugeValue(final double percent) {
mValue = percent;
mGauge.percent = mValue;
}

    void setPercent(final double percent) {
mPercent = percent;
}

//Synthetic comment -- @@ -68,25 +68,25 @@
return mInfo;
}

    void setIcon(final Drawable icon) {
mIcon = icon;
notifyChanged();
}

@Override
    protected void onBindView(final View view) {
super.onBindView(view);

        final ImageView appIcon = (ImageView) view.findViewById(R.id.appIcon);
if (mIcon == null) {
mIcon = getContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon);
}
appIcon.setImageDrawable(mIcon);

        final ImageView appGauge = (ImageView) view.findViewById(R.id.appGauge);
appGauge.setImageDrawable(mGauge);

        final TextView percentView = (TextView) view.findViewById(R.id.percent);
percentView.setText((int) (Math.ceil(mPercent)) + "%");
}









//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/PowerUsageDetail.java b/src/com/android/settings/fuelgauge/PowerUsageDetail.java
//Synthetic comment -- index 4db968a..79e1072 100644

//Synthetic comment -- @@ -121,7 +121,7 @@
ComponentName mInstaller;

@Override
    protected void onCreate(final Bundle icicle) {
super.onCreate(icicle);
setContentView(R.layout.power_usage_details);
createDetails();
//Synthetic comment -- @@ -148,16 +148,16 @@
mUid = intent.getIntExtra(EXTRA_UID, 0);
mDrainType = (DrainType) intent.getSerializableExtra(EXTRA_DRAIN_TYPE);
mNoCoverage = intent.getDoubleExtra(EXTRA_NO_COVERAGE, 0);
        final String iconPackage = intent.getStringExtra(EXTRA_ICON_PACKAGE);
        final int iconId = intent.getIntExtra(EXTRA_ICON_ID, 0);
if (!TextUtils.isEmpty(iconPackage)) {
try {
final PackageManager pm = getPackageManager();
                final ApplicationInfo ai = pm.getPackageInfo(iconPackage, 0).applicationInfo;
if (ai != null) {
mAppIcon = ai.loadIcon(pm);
}
            } catch (final NameNotFoundException nnfe) {
// Use default icon
}
} else if (iconId != 0) {
//Synthetic comment -- @@ -168,7 +168,7 @@
}

// Set the description
        final String summary = getDescriptionForDrainType();
((TextView)findViewById(R.id.summary)).setText(summary);

mTypes = intent.getIntArrayExtra(EXTRA_DETAIL_TYPES);
//Synthetic comment -- @@ -184,13 +184,13 @@
mReportButton = (Button) findViewById(R.id.right_button);
mForceStopButton.setEnabled(false);

        final ImageView gaugeImage = (ImageView) findViewById(R.id.gauge);
mGauge = new PercentageBar();
mGauge.percent = gaugeValue;
mGauge.bar = getResources().getDrawable(R.drawable.app_gauge);
gaugeImage.setImageDrawable(mGauge);

        final ImageView iconImage = (ImageView) findViewById(R.id.icon);
iconImage.setImageDrawable(mAppIcon);

mDetailsParent = (ViewGroup) findViewById(R.id.details);
//Synthetic comment -- @@ -209,7 +209,7 @@
mReportButton.setOnClickListener(this);

// check if error reporting is enabled in secure settings
            final int enabled = Settings.Secure.getInt(getContentResolver(),
Settings.Secure.SEND_ACTION_APP_ERROR, 0);
if (enabled != 0) {
if (mPackages != null && mPackages.length > 0) {
//Synthetic comment -- @@ -217,7 +217,7 @@
mApp = getPackageManager().getApplicationInfo(mPackages[0], 0);
mInstaller = ApplicationErrorReport.getErrorReportReceiver(
this, mPackages[0], mApp.flags);
                    } catch (final NameNotFoundException e) {
}
}
mReportButton.setEnabled(mInstaller != null);
//Synthetic comment -- @@ -229,11 +229,11 @@
}
}

    public void onClick(final View v) {
doAction((Integer) v.getTag());
}

    private void doAction(final int action) {
switch (action) {
case ACTION_DISPLAY_SETTINGS:
startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));
//Synthetic comment -- @@ -248,7 +248,7 @@
startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
break;
case ACTION_APP_DETAILS:
                final Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setClass(this, InstalledAppDetails.class);
intent.putExtra(ManageApplications.APP_PKG_NAME, mPackages[0]);
startActivity(intent);
//Synthetic comment -- @@ -266,7 +266,7 @@
}

private void fillDetailsSection() {
        final LayoutInflater inflater = getLayoutInflater();
if (mTypes != null && mValues != null) {
for (int i = 0; i < mTypes.length; i++) {
// Only add an item if the time is greater than zero
//Synthetic comment -- @@ -287,26 +287,26 @@
default:
value = Utils.formatElapsedTime(this, mValues[i]);
}
                final ViewGroup item = (ViewGroup) inflater.inflate(R.layout.power_usage_detail_item_text,
null);
mDetailsParent.addView(item);
                final TextView labelView = (TextView) item.findViewById(R.id.label);
                final TextView valueView = (TextView) item.findViewById(R.id.value);
labelView.setText(label);
valueView.setText(value);
}
}
}

    private void fillControlsSection(final int uid) {
        final PackageManager pm = getPackageManager();
        final String[] packages = pm.getPackagesForUid(uid);
PackageInfo pi = null;
try {
pi = packages != null ? pm.getPackageInfo(packages[0], 0) : null;
        } catch (final NameNotFoundException nnfe) { /* Nothing */ }
        final ApplicationInfo ai = pi != null? pi.applicationInfo : null;
        final boolean isSystem = ai != null? (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0 : false;

boolean removeHeader = true;
switch (mDrainType) {
//Synthetic comment -- @@ -357,13 +357,13 @@
}
}

    private void addControl(final int title, final int summary, final int action) {
final Resources res = getResources();
        final LayoutInflater inflater = getLayoutInflater();
        final ViewGroup item = (ViewGroup) inflater.inflate(R.layout.power_usage_action_item,null);
mControlsParent.addView(item);
        final Button actionButton = (Button) item.findViewById(R.id.action_button);
        final TextView summaryView = (TextView) item.findViewById(R.id.summary);
actionButton.setText(res.getString(title));
summaryView.setText(res.getString(summary));
actionButton.setOnClickListener(this);
//Synthetic comment -- @@ -382,7 +382,7 @@

private void killProcesses() {
if (mPackages == null) return;
        final ActivityManager am = (ActivityManager)getSystemService(
Context.ACTIVITY_SERVICE);
for (int i = 0; i < mPackages.length; i++) {
am.forceStopPackage(mPackages[i]);
//Synthetic comment -- @@ -392,7 +392,7 @@

private final BroadcastReceiver mCheckKillProcessesReceiver = new BroadcastReceiver() {
@Override
        public void onReceive(final Context context, final Intent intent) {
mForceStopButton.setEnabled(getResultCode() != RESULT_CANCELED);
}
};
//Synthetic comment -- @@ -402,7 +402,7 @@
mForceStopButton.setEnabled(false);
return;
}
        final Intent intent = new Intent(Intent.ACTION_QUERY_PACKAGE_RESTART,
Uri.fromParts("package", mPackages[0], null));
intent.putExtra(Intent.EXTRA_PACKAGES, mPackages);
intent.putExtra(Intent.EXTRA_UID, mUid);
//Synthetic comment -- @@ -413,7 +413,7 @@
private void reportBatteryUse() {
if (mPackages == null) return;

        final ApplicationErrorReport report = new ApplicationErrorReport();
report.type = ApplicationErrorReport.TYPE_BATTERY;
report.packageName = mPackages[0];
report.installerPackageName = mInstaller.getPackageName();
//Synthetic comment -- @@ -422,30 +422,30 @@
report.systemApp = (mApp.flags & ApplicationInfo.FLAG_SYSTEM) != 0;

final Intent intent = getIntent();
        final ApplicationErrorReport.BatteryInfo batteryInfo = new ApplicationErrorReport.BatteryInfo();
batteryInfo.usagePercent = intent.getIntExtra(EXTRA_PERCENT, 1);
batteryInfo.durationMicros = intent.getLongExtra(EXTRA_USAGE_DURATION, 0);
batteryInfo.usageDetails = intent.getStringExtra(EXTRA_REPORT_DETAILS);
batteryInfo.checkinDetails = intent.getStringExtra(EXTRA_REPORT_CHECKIN_DETAILS);
report.batteryInfo = batteryInfo;

        final Intent result = new Intent(Intent.ACTION_APP_ERROR);
result.setComponent(mInstaller);
result.putExtra(Intent.EXTRA_BUG_REPORT, report);
result.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(result);
}

    private void fillPackagesSection(final int uid) {
if (uid < 1) {
removePackagesSection();
return;
}
        final ViewGroup packagesParent = (ViewGroup) findViewById(R.id.packages_section);
if (packagesParent == null) return;
        final LayoutInflater inflater = getLayoutInflater();

        final PackageManager pm = getPackageManager();
//final Drawable defaultActivityIcon = pm.getDefaultActivityIcon();
mPackages = pm.getPackagesForUid(uid);
if (mPackages == null || mPackages.length < 2) {
//Synthetic comment -- @@ -456,8 +456,8 @@
// Convert package names to user-facing labels where possible
for (int i = 0; i < mPackages.length; i++) {
try {
                final ApplicationInfo ai = pm.getApplicationInfo(mPackages[i], 0);
                final CharSequence label = ai.loadLabel(pm);
//Drawable icon = defaultActivityIcon;
if (label != null) {
mPackages[i] = label.toString();
//Synthetic comment -- @@ -465,12 +465,12 @@
//if (ai.icon != 0) {
//    icon = ai.loadIcon(pm);
//}
                final ViewGroup item = (ViewGroup) inflater.inflate(R.layout.power_usage_package_item,
null);
packagesParent.addView(item);
                final TextView labelView = (TextView) item.findViewById(R.id.label);
labelView.setText(mPackages[i]);
            } catch (final NameNotFoundException e) {
}
}
}








//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/PowerUsageSummary.java b/src/com/android/settings/fuelgauge/PowerUsageSummary.java
//Synthetic comment -- index 5678160..6e7bc2b 100644

//Synthetic comment -- @@ -72,7 +72,7 @@

IBatteryStats mBatteryInfo;
BatteryStatsImpl mStats;
    private final List<BatterySipper> mUsageList = new ArrayList<BatterySipper>();

private PreferenceGroup mAppListGroup;

//Synthetic comment -- @@ -86,10 +86,10 @@
private double mTotalPower;
private PowerProfile mPowerProfile;

    private final HashMap<String,UidToDetail> mUidCache = new HashMap<String,UidToDetail>();

/** Queue for fetching name and icon for an application */
    private final ArrayList<BatterySipper> mRequestQueue = new ArrayList<BatterySipper>();
private Thread mRequestThread;
private boolean mAbort;

//Synthetic comment -- @@ -100,7 +100,7 @@
}

@Override
    protected void onCreate(final Bundle icicle) {
super.onCreate(icicle);

addPreferencesFromResource(R.xml.power_usage_summary);
//Synthetic comment -- @@ -127,10 +127,10 @@
}

@Override
    public boolean onPreferenceTreeClick(final PreferenceScreen preferenceScreen, final Preference preference) {
        final PowerGaugePreference pgp = (PowerGaugePreference) preference;
        final BatterySipper sipper = pgp.getInfo();
        final Intent intent = new Intent(this, PowerUsageDetail.class);
intent.putExtra(PowerUsageDetail.EXTRA_TITLE, sipper.name);
intent.putExtra(PowerUsageDetail.EXTRA_PERCENT, (int)
Math.ceil(sipper.getSortValue() * 100 / mTotalPower));
//Synthetic comment -- @@ -150,7 +150,7 @@
switch (sipper.drainType) {
case APP:
{
                final Uid uid = sipper.uidObj;
types = new int[] {
R.string.usage_type_cpu,
R.string.usage_type_cpu_foreground,
//Synthetic comment -- @@ -211,7 +211,7 @@
}

@Override
    public boolean onCreateOptionsMenu(final Menu menu) {
if (DEBUG) {
menu.add(0, MENU_STATS_TYPE, 0, R.string.menu_stats_total)
.setIcon(com.android.internal.R.drawable.ic_menu_info_details)
//Synthetic comment -- @@ -224,7 +224,7 @@
}

@Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
if (DEBUG) {
menu.findItem(MENU_STATS_TYPE).setTitle(mStatsType == BatteryStats.STATS_TOTAL
? R.string.menu_stats_unplugged
//Synthetic comment -- @@ -234,7 +234,7 @@
}

@Override
    public boolean onOptionsItemSelected(final MenuItem item) {
switch (item.getItemId()) {
case MENU_STATS_TYPE:
if (mStatsType == BatteryStats.STATS_TOTAL) {
//Synthetic comment -- @@ -268,12 +268,12 @@
mAppListGroup.setOrderingAsAdded(false);

Collections.sort(mUsageList);
        for (final BatterySipper sipper : mUsageList) {
if (sipper.getSortValue() < MIN_POWER_THRESHOLD) continue;
final double percentOfTotal =  ((sipper.getSortValue() / mTotalPower) * 100);
if (percentOfTotal < 1) continue;
            final PowerGaugePreference pref = new PowerGaugePreference(this, sipper.getIcon(), sipper);
            final double percentOfMax = (sipper.getSortValue() * 100) / mMaxPower;
sipper.percent = percentOfTotal;
pref.setTitle(sipper.name);
pref.setPercent(percentOfTotal);
//Synthetic comment -- @@ -298,16 +298,16 @@
}
}

    private void updateStatsPeriod(final long duration) {
        final String durationString = Utils.formatElapsedTime(this, duration / 1000);
        final String label = getString(mStats.isOnBattery()
? R.string.battery_stats_duration
: R.string.battery_stats_last_duration, durationString);
setTitle(label);
}

private void processAppUsage() {
        final SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
final int which = mStatsType;
final int speedSteps = mPowerProfile.getNumSpeedSteps();
final double[] powerCpuNormal = new double[speedSteps];
//Synthetic comment -- @@ -316,27 +316,27 @@
powerCpuNormal[p] = mPowerProfile.getAveragePower(PowerProfile.POWER_CPU_ACTIVE, p);
}
final double averageCostPerByte = getAverageDataCost();
        final long uSecTime = mStats.computeBatteryRealtime(SystemClock.elapsedRealtime() * 1000, which);
mStatsPeriod = uSecTime;
updateStatsPeriod(uSecTime);
        final SparseArray<? extends Uid> uidStats = mStats.getUidStats();
final int NU = uidStats.size();
for (int iu = 0; iu < NU; iu++) {
            final Uid u = uidStats.valueAt(iu);
double power = 0;
double highestDrain = 0;
String packageWithHighestDrain = null;
//mUsageList.add(new AppUsage(u.getUid(), new double[] {power}));
            final Map<String, ? extends BatteryStats.Uid.Proc> processStats = u.getProcessStats();
long cpuTime = 0;
long cpuFgTime = 0;
long gpsTime = 0;
if (processStats.size() > 0) {
// Process CPU time
                for (final Map.Entry<String, ? extends BatteryStats.Uid.Proc> ent
: processStats.entrySet()) {
if (DEBUG) Log.i(TAG, "Process name = " + ent.getKey());
                    final Uid.Proc ps = ent.getValue();
final long userTime = ps.getUserTime(which);
final long systemTime = ps.getSystemTime(which);
final long foregroundTime = ps.getForegroundTime(which);
//Synthetic comment -- @@ -352,7 +352,7 @@
// Then compute the ratio of time spent at each speed
double processPower = 0;
for (int step = 0; step < speedSteps; step++) {
                        final double ratio = (double) cpuSpeedStepTimes[step] / totalTimeAtSpeeds;
processPower += ratio * tmpCpuTime * powerCpuNormal[step];
}
cpuTime += tmpCpuTime;
//Synthetic comment -- @@ -379,13 +379,13 @@
* averageCostPerByte;

// Process Sensor usage
            final Map<Integer, ? extends BatteryStats.Uid.Sensor> sensorStats = u.getSensorStats();
            for (final Map.Entry<Integer, ? extends BatteryStats.Uid.Sensor> sensorEntry
: sensorStats.entrySet()) {
                final Uid.Sensor sensor = sensorEntry.getValue();
                final int sensorType = sensor.getHandle();
                final BatteryStats.Timer timer = sensor.getSensorTime();
                final long sensorTime = timer.getTotalTimeLocked(uSecTime, which) / 1000;
double multiplier = 0;
switch (sensorType) {
case Uid.Sensor.GPS:
//Synthetic comment -- @@ -393,7 +393,7 @@
gpsTime = sensorTime;
break;
default:
                        final android.hardware.Sensor sensorData =
sensorManager.getDefaultSensor(sensorType);
if (sensorData != null) {
multiplier = sensorData.getPower();
//Synthetic comment -- @@ -408,7 +408,7 @@

// Add the app to the list if it is consuming power
if (power != 0) {
                final BatterySipper app = new BatterySipper(packageWithHighestDrain, DrainType.APP, 0, u,
new double[] {power});
app.cpuTime = cpuTime;
app.gpsTime = gpsTime;
//Synthetic comment -- @@ -421,24 +421,24 @@
}
}

    private void addPhoneUsage(final long uSecNow) {
        final long phoneOnTimeMs = mStats.getPhoneOnTime(uSecNow, mStatsType) / 1000;
        final double phoneOnPower = mPowerProfile.getAveragePower(PowerProfile.POWER_RADIO_ACTIVE)
* phoneOnTimeMs / 1000;
addEntry(getString(R.string.power_phone), DrainType.PHONE, phoneOnTimeMs,
R.drawable.ic_settings_voice_calls, phoneOnPower);
}

    private void addScreenUsage(final long uSecNow) {
double power = 0;
        final long screenOnTimeMs = mStats.getScreenOnTime(uSecNow, mStatsType) / 1000;
power += screenOnTimeMs * mPowerProfile.getAveragePower(PowerProfile.POWER_SCREEN_ON);
final double screenFullPower =
mPowerProfile.getAveragePower(PowerProfile.POWER_SCREEN_FULL);
for (int i = 0; i < BatteryStats.NUM_SCREEN_BRIGHTNESS_BINS; i++) {
            final double screenBinPower = screenFullPower * (i + 0.5f)
/ BatteryStats.NUM_SCREEN_BRIGHTNESS_BINS;
            final long brightnessTime = mStats.getScreenBrightnessTime(i, uSecNow, mStatsType) / 1000;
power += screenBinPower * brightnessTime;
if (DEBUG) {
Log.i(TAG, "Screen bin power = " + (int) screenBinPower + ", time = "
//Synthetic comment -- @@ -450,20 +450,20 @@
R.drawable.ic_settings_display, power);
}

    private void addRadioUsage(final long uSecNow) {
double power = 0;
final int BINS = BatteryStats.NUM_SIGNAL_STRENGTH_BINS;
long signalTimeMs = 0;
for (int i = 0; i < BINS; i++) {
            final long strengthTimeMs = mStats.getPhoneSignalStrengthTime(i, uSecNow, mStatsType) / 1000;
power += strengthTimeMs / 1000
* mPowerProfile.getAveragePower(PowerProfile.POWER_RADIO_ON, i);
signalTimeMs += strengthTimeMs;
}
        final long scanningTimeMs = mStats.getPhoneSignalScanningTime(uSecNow, mStatsType) / 1000;
power += scanningTimeMs / 1000 * mPowerProfile.getAveragePower(
PowerProfile.POWER_RADIO_SCANNING);
        final BatterySipper bs =
addEntry(getString(R.string.power_cell), DrainType.CELL, signalTimeMs,
R.drawable.ic_settings_cell_standby, power);
if (signalTimeMs != 0) {
//Synthetic comment -- @@ -472,29 +472,29 @@
}
}

    private void addWiFiUsage(final long uSecNow) {
        final long onTimeMs = mStats.getWifiOnTime(uSecNow, mStatsType) / 1000;
        final long runningTimeMs = mStats.getWifiRunningTime(uSecNow, mStatsType) / 1000;
        final double wifiPower = (onTimeMs * 0 /* TODO */
* mPowerProfile.getAveragePower(PowerProfile.POWER_WIFI_ON)
+ runningTimeMs * mPowerProfile.getAveragePower(PowerProfile.POWER_WIFI_ON)) / 1000;
addEntry(getString(R.string.power_wifi), DrainType.WIFI, runningTimeMs,
R.drawable.ic_settings_wifi, wifiPower);
}

    private void addIdleUsage(final long uSecNow) {
        final long idleTimeMs = (uSecNow - mStats.getScreenOnTime(uSecNow, mStatsType)) / 1000;
        final double idlePower = (idleTimeMs * mPowerProfile.getAveragePower(PowerProfile.POWER_CPU_IDLE))
/ 1000;
addEntry(getString(R.string.power_idle), DrainType.IDLE, idleTimeMs,
R.drawable.ic_settings_phone_idle, idlePower);
}

    private void addBluetoothUsage(final long uSecNow) {
        final long btOnTimeMs = mStats.getBluetoothOnTime(uSecNow, mStatsType) / 1000;
double btPower = btOnTimeMs * mPowerProfile.getAveragePower(PowerProfile.POWER_BLUETOOTH_ON)
/ 1000;
        final int btPingCount = mStats.getBluetoothPingCount();
btPower += (btPingCount
* mPowerProfile.getAveragePower(PowerProfile.POWER_BLUETOOTH_AT_CMD)) / 1000;

//Synthetic comment -- @@ -518,8 +518,8 @@
? mobileData * 8 * 1000 / radioDataUptimeMs
: MOBILE_BPS;

        final double mobileCostPerByte = MOBILE_POWER / (mobileBps / 8);
        final double wifiCostPerByte = WIFI_POWER / (WIFI_BPS / 8);
if (wifiData + mobileData != 0) {
return (mobileCostPerByte * mobileData + wifiCostPerByte * wifiData)
/ (mobileData + wifiData);
//Synthetic comment -- @@ -530,7 +530,7 @@

private void processMiscUsage() {
final int which = mStatsType;
        final long uSecTime = SystemClock.elapsedRealtime() * 1000;
final long uSecNow = mStats.computeBatteryRealtime(uSecTime, which);
final long timeSinceUnplugged = uSecNow;
if (DEBUG) {
//Synthetic comment -- @@ -545,11 +545,11 @@
addRadioUsage(uSecNow);
}

    private BatterySipper addEntry(final String label, final DrainType drainType, final long time, final int iconId,
            final double power) {
if (power > mMaxPower) mMaxPower = power;
mTotalPower += power;
        final BatterySipper bs = new BatterySipper(label, drainType, iconId, null, new double[] {power});
bs.usageTime = time;
bs.iconId = iconId;
mUsageList.add(bs);
//Synthetic comment -- @@ -558,13 +558,13 @@

private void load() {
try {
            final byte[] data = mBatteryInfo.getStatistics();
            final Parcel parcel = Parcel.obtain();
parcel.unmarshall(data, 0, data.length);
parcel.setDataPosition(0);
mStats = com.android.internal.os.BatteryStatsImpl.CREATOR
.createFromParcel(parcel);
        } catch (final RemoteException e) {
Log.e(TAG, "RemoteException:", e);
}
}
//Synthetic comment -- @@ -585,7 +585,7 @@
double noCoveragePercent;
String defaultPackageName;

        BatterySipper(final String label, final DrainType drainType, final int iconId, final Uid uid, final double[] values) {
this.values = values;
name = label;
this.drainType = drainType;
//Synthetic comment -- @@ -611,24 +611,24 @@
return icon;
}

        public int compareTo(final BatterySipper other) {
// Return the flipped value because we want the items in descending order
return (int) (other.getSortValue() - getSortValue());
}

        void getQuickNameIconForUid(final Uid uidObj) {
final int uid = uidObj.getUid();
final String uidString = Integer.toString(uid);
if (mUidCache.containsKey(uidString)) {
                final UidToDetail utd = mUidCache.get(uidString);
defaultPackageName = utd.packageName;
name = utd.name;
icon = utd.icon;
return;
}
            final PackageManager pm = getPackageManager();
final Drawable defaultActivityIcon = pm.getDefaultActivityIcon();
            final String[] packages = pm.getPackagesForUid(uid);
icon = pm.getDefaultActivityIcon();
if (packages == null) {
//name = Integer.toString(uid);
//Synthetic comment -- @@ -653,16 +653,16 @@
* @param uid Uid of the application
*/
void getNameIcon() {
            final PackageManager pm = getPackageManager();
final int uid = uidObj.getUid();
final Drawable defaultActivityIcon = pm.getDefaultActivityIcon();
            final String[] packages = pm.getPackagesForUid(uid);
if (packages == null) {
name = Integer.toString(uid);
return;
}

            final String[] packageLabels = new String[packages.length];
System.arraycopy(packages, 0, packageLabels, 0, packages.length);

int preferredIndex = -1;
//Synthetic comment -- @@ -671,8 +671,8 @@
// Check if package matches preferred package
if (packageLabels[i].equals(name)) preferredIndex = i;
try {
                    final ApplicationInfo ai = pm.getApplicationInfo(packageLabels[i], 0);
                    final CharSequence label = ai.loadLabel(pm);
if (label != null) {
packageLabels[i] = label.toString();
}
//Synthetic comment -- @@ -681,7 +681,7 @@
icon = ai.loadIcon(pm);
break;
}
                } catch (final NameNotFoundException e) {
}
}
if (icon == null) icon = defaultActivityIcon;
//Synthetic comment -- @@ -690,7 +690,7 @@
name = packageLabels[0];
} else {
// Look for an official name for this UID.
                for (final String pkgName : packages) {
try {
final PackageInfo pi = pm.getPackageInfo(pkgName, 0);
if (pi.sharedUserLabel != 0) {
//Synthetic comment -- @@ -705,12 +705,12 @@
break;
}
}
                    } catch (final PackageManager.NameNotFoundException e) {
}
}
}
final String uidString = Integer.toString(uidObj.getUid());
            final UidToDetail utd = new UidToDetail();
utd.name = name;
utd.icon = icon;
utd.packageName = defaultPackageName;
//Synthetic comment -- @@ -738,11 +738,11 @@
Handler mHandler = new Handler() {

@Override
        public void handleMessage(final Message msg) {
switch (msg.what) {
case MSG_UPDATE_NAME_ICON:
                    final BatterySipper bs = (BatterySipper) msg.obj;
                    final PowerGaugePreference pgp = 
(PowerGaugePreference) findPreference(
Integer.toString(bs.uidObj.getUid()));
if (pgp != null) {








//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/Utils.java b/src/com/android/settings/fuelgauge/Utils.java
//Synthetic comment -- index 2ffc9de..8ec626c 100644

//Synthetic comment -- @@ -35,8 +35,8 @@
* @param millis the elapsed time in milli seconds
* @return the formatted elapsed time
*/
    public static String formatElapsedTime(final Context context, final double millis) {
        final StringBuilder sb = new StringBuilder();
int seconds = (int) Math.floor(millis / 1000);

int days = 0, hours = 0, minutes = 0;
//Synthetic comment -- @@ -71,7 +71,7 @@
* @param bytes data size in bytes
* @return the formatted size such as 4.52 MB or 245 KB or 332 bytes
*/
    public static String formatBytes(final Context context, final double bytes) {
// TODO: I18N
if (bytes > 1000 * 1000) {
return String.format("%.2f MB", ((int) (bytes / 1000)) / 1000f);







