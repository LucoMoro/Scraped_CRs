/*Added final modifier where appropriate to increase performance (resubmit in master branch)

Change-Id:I86b8fdf4e04a2778742a5b1ab748c8b1dff26172*/




//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/PercentageBar.java b/src/com/android/settings/fuelgauge/PercentageBar.java
//Synthetic comment -- index 1c4478b..aab84eb 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
/**
* A drawable for drawing a bar with a background.
*/
final class PercentageBar extends Drawable {

Drawable bar;
double percent;
//Synthetic comment -- @@ -55,8 +55,8 @@
}

private int getBarWidth() {
        final int width = (int) ((this.getBounds().width() * percent) / 100);
        final int intrinsicWidth = bar.getIntrinsicWidth();
return Math.max(width, intrinsicWidth);
}









//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/PowerGaugePreference.java b/src/com/android/settings/fuelgauge/PowerGaugePreference.java
//Synthetic comment -- index 68f294c..0c6a841 100644

//Synthetic comment -- @@ -37,9 +37,9 @@
public class PowerGaugePreference extends Preference {

private Drawable mIcon;
    private final PercentageBar mGauge;
private double mValue;
    private final BatterySipper mInfo;
private double mPercent;

public PowerGaugePreference(Context context, Drawable icon, BatterySipper info) {
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

//Synthetic comment -- @@ -77,16 +77,16 @@
protected void onBindView(View view) {
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
//Synthetic comment -- index 4db968a..5636e0d 100644

//Synthetic comment -- @@ -46,7 +46,7 @@

public class PowerUsageDetail extends Activity implements Button.OnClickListener {

    public static enum DrainType {
IDLE,
CELL,
PHONE,
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
//Synthetic comment -- @@ -287,11 +287,11 @@
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
//Synthetic comment -- @@ -299,14 +299,14 @@
}

private void fillControlsSection(int uid) {
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
//Synthetic comment -- @@ -359,11 +359,11 @@

private void addControl(int title, int summary, int action) {
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
//Synthetic comment -- @@ -422,14 +422,14 @@
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
//Synthetic comment -- @@ -441,11 +441,11 @@
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
//Synthetic comment -- index 5678160..7cb0cb3 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
* Displays a list of apps and subsystems that consume power, ordered by how much power was
* consumed since the last time it was unplugged.
*/
public final class PowerUsageSummary extends PreferenceActivity implements Runnable {

private static final boolean DEBUG = false;

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
//Synthetic comment -- @@ -273,7 +273,7 @@
final double percentOfTotal =  ((sipper.getSortValue() / mTotalPower) * 100);
if (percentOfTotal < 1) continue;
PowerGaugePreference pref = new PowerGaugePreference(this, sipper.getIcon(), sipper);
            final double percentOfMax = (sipper.getSortValue() * 100) / mMaxPower;
sipper.percent = percentOfTotal;
pref.setTitle(sipper.name);
pref.setPercent(percentOfTotal);
//Synthetic comment -- @@ -298,7 +298,7 @@
}
}

    private void updateStatsPeriod(final long duration) {
String durationString = Utils.formatElapsedTime(this, duration / 1000);
String label = getString(mStats.isOnBattery()
? R.string.battery_stats_duration
//Synthetic comment -- @@ -316,7 +316,7 @@
powerCpuNormal[p] = mPowerProfile.getAveragePower(PowerProfile.POWER_CPU_ACTIVE, p);
}
final double averageCostPerByte = getAverageDataCost();
        final long uSecTime = mStats.computeBatteryRealtime(SystemClock.elapsedRealtime() * 1000, which);
mStatsPeriod = uSecTime;
updateStatsPeriod(uSecTime);
SparseArray<? extends Uid> uidStats = mStats.getUidStats();
//Synthetic comment -- @@ -421,15 +421,15 @@
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
long screenOnTimeMs = mStats.getScreenOnTime(uSecNow, mStatsType) / 1000;
power += screenOnTimeMs * mPowerProfile.getAveragePower(PowerProfile.POWER_SCREEN_ON);








//Synthetic comment -- diff --git a/src/com/android/settings/fuelgauge/Utils.java b/src/com/android/settings/fuelgauge/Utils.java
//Synthetic comment -- index 2ffc9de..881b384 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
* @return the formatted elapsed time
*/
public static String formatElapsedTime(Context context, double millis) {
        final StringBuilder sb = new StringBuilder();
int seconds = (int) Math.floor(millis / 1000);

int days = 0, hours = 0, minutes = 0;







