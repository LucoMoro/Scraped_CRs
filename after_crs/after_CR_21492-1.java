/*Don't erase SDCard when editing existing AVD.

SDK Bug 14162

Change-Id:I22493443e706bda830916ed0fc09741a1d6d1f15*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 027897d..d3de4c9 100644

//Synthetic comment -- @@ -987,8 +987,9 @@
skin,
mSdkCommandLine.getParamSdCard(),
hardwareConfig,
mSdkCommandLine.getFlagSnapshot(),
                    removePrevious,
                    false, //edit existing
mSdkLog);

} catch (AndroidLocationException e) {








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java b/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java
//Synthetic comment -- index 3260024..b5bea69 100644

//Synthetic comment -- @@ -58,7 +58,16 @@

public void testCreateAvdWithoutSnapshot() {
mAvdManager.createAvd(
                mAvdFolder,
                this.getName(),
                mTarget,
                null,   // skinName
                null,   // sdName
                null,   // properties
                false,  // createSnapshot
                false,  // removePrevious
                false,  // editExisting
                mLog);

assertEquals("[P Created AVD '" + this.getName() + "' based on Android 0.0\n]",
mLog.toString());
//Synthetic comment -- @@ -77,8 +86,18 @@
}

public void testCreateAvdWithSnapshot() {

mAvdManager.createAvd(
                mAvdFolder,
                this.getName(),
                mTarget,
                null,   // skinName
                null,   // sdName
                null,   // properties
                true,   // createSnapshot
                false,  // removePrevious
                false,  // editExisting
                mLog);

assertEquals("[P Created AVD '" + this.getName() + "' based on Android 0.0\n]",
mLog.toString());








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index 29516e3..f5895e7 100644

//Synthetic comment -- @@ -68,7 +68,17 @@
Main main = new Main();
main.setLogger(mLog);
mAvdManager.createAvd(
                mAvdFolder,
                this.getName(),
                mTarget,
                null,   // skinName
                null,   // sdName
                null,   // properties
                false,  // createSnapshot
                false,  // removePrevious
                false,  // editExisting
                mLog);

mLog.clear();
main.displayAvdList(mAvdManager);
assertEquals(
//Synthetic comment -- @@ -84,8 +94,19 @@
public void testDisplayAvdListOfOneSnapshot() {
Main main = new Main();
main.setLogger(mLog);

mAvdManager.createAvd(
                mAvdFolder,
                this.getName(),
                mTarget,
                null,   // skinName
                null,   // sdName
                null,   // properties
                true,  // createSnapshot
                false,  // removePrevious
                false,  // editExisting
                mLog);

mLog.clear();
main.displayAvdList(mAvdManager);
assertEquals(








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index cd29c61..57cb1bf 100644

//Synthetic comment -- @@ -93,6 +93,7 @@
* This property is for UI purposes only. It is not used by the emulator.
*
* @see #SDCARD_SIZE_PATTERN
     * @see #parseSdcardSize(String, String[])
*/
public final static String AVD_INI_SDCARD_SIZE = "sdcard.size"; //$NON-NLS-1$
/**
//Synthetic comment -- @@ -138,18 +139,27 @@

/**
* Pattern for matching SD Card sizes, e.g. "4K" or "16M".
     * Callers should use {@link #parseSdcardSize(String, String[])} instead of using this directly.
*/
    private final static Pattern SDCARD_SIZE_PATTERN = Pattern.compile("(\\d+)([KMG])"); //$NON-NLS-1$

/**
* Minimal size of an SDCard image file in bytes. Currently 9 MiB.
*/

public static final long SDCARD_MIN_BYTE_SIZE = 9<<20;
/**
* Maximal size of an SDCard image file in bytes. Currently 1023 GiB.
*/
public static final long SDCARD_MAX_BYTE_SIZE = 1023L<<30;

    /** The sdcard string represents a valid number but the size is outside of the allowed range. */
    public final static int SDCARD_SIZE_NOT_IN_RANGE = 0;
    /** The sdcard string looks like a size number+suffix but the number failed to decode. */
    public final static int SDCARD_SIZE_INVALID = -1;
    /** The sdcard string doesn't look like a size, it might be a path instead. */
    public final static int SDCARD_NOT_SIZE_PATTERN = -2;

/** Regex used to validate characters that compose an AVD name. */
public final static Pattern RE_AVD_NAME = Pattern.compile("[a-zA-Z0-9._-]+"); //$NON-NLS-1$

//Synthetic comment -- @@ -437,6 +447,71 @@
}

/**
     * Parse the sdcard string to decode the size.
     * Returns:
     * <ul>
     * <li> The size in bytes > 0 if the sdcard string is a valid size in the allowed range.
     * <li> {@link #SDCARD_SIZE_NOT_IN_RANGE} (0)
     *          if the sdcard string is a valid size NOT in the allowed range.
     * <li> {@link #SDCARD_SIZE_INVALID} (-1)
     *          if the sdcard string is number that fails to parse correctly.
     * <li> {@link #SDCARD_NOT_SIZE_PATTERN} (-2)
     *          if the sdcard string is not a number, in which case it's probably a file path.
     * </ul>
     *
     * @param sdcard The sdcard string, which can be a file path, a size string or something else.
     * @param parsedStrings If non-null, an array of 2 strings. The first string will be
     *  filled with the parsed numeric size and the second one will be filled with the
     *  parsed suffix. This is filled even if the returned size is deemed out of range or
     *  failed to parse. The values are null if the sdcard is not a size pattern.
     * @return A size in byte if > 0, or {@link #SDCARD_SIZE_NOT_IN_RANGE},
     *  {@link #SDCARD_SIZE_INVALID} or {@link #SDCARD_NOT_SIZE_PATTERN} as error codes.
     */
    public static long parseSdcardSize(String sdcard, String[] parsedStrings) {

        if (parsedStrings != null) {
            assert parsedStrings.length == 2;
            parsedStrings[0] = null;
            parsedStrings[1] = null;
        }

        Matcher m = SDCARD_SIZE_PATTERN.matcher(sdcard);
        if (m.matches()) {
            if (parsedStrings != null) {
                assert parsedStrings.length == 2;
                parsedStrings[0] = m.group(1);
                parsedStrings[1] = m.group(2);
            }

            // get the sdcard values for checks
            try {
                long sdcardSize = Long.parseLong(m.group(1));

                String sdcardSizeModifier = m.group(2);
                if ("K".equals(sdcardSizeModifier)) {           //$NON-NLS-1$
                    sdcardSize <<= 10;
                } else if ("M".equals(sdcardSizeModifier)) {    //$NON-NLS-1$
                    sdcardSize <<= 20;
                } else if ("G".equals(sdcardSizeModifier)) {    //$NON-NLS-1$
                    sdcardSize <<= 30;
                }

                if (sdcardSize < SDCARD_MIN_BYTE_SIZE ||
                        sdcardSize > SDCARD_MAX_BYTE_SIZE) {
                    return SDCARD_SIZE_NOT_IN_RANGE;
                }

                return sdcardSize;
            } catch (NumberFormatException e) {
                // This could happen if the number is too large to fit in a long.
                return SDCARD_SIZE_INVALID;
            }
        }

        return SDCARD_NOT_SIZE_PATTERN;
    }

    /**
* Returns all the existing AVDs.
* @return a newly allocated array containing all the AVDs.
*/
//Synthetic comment -- @@ -586,19 +661,6 @@
}

/**
* Creates a new AVD. It is expected that there is no existing AVD with this name already.
*
* @param avdFolder the data folder for the AVD. It will be created as needed.
//Synthetic comment -- @@ -608,15 +670,25 @@
* @param sdcard the parameter value for the sdCard. Can be null. This is either a path to
*        an existing sdcard image or a sdcard size (\d+, \d+K, \dM).
* @param hardwareConfig the hardware setup for the AVD. Can be null to use defaults.
* @param createSnapshot If true copy a blank snapshot image into the AVD.
     * @param removePrevious If true remove any previous files.
     * @param editExisting If true, edit an existing AVD, changing only the minimum required.
     *          This won't remove files unless required or unless {@code removePrevious} is set.
* @param log the log object to receive action logs. Cannot be null.
* @return The new {@link AvdInfo} in case of success (which has just been added to the
*         internal list) or null in case of failure.
*/
    public AvdInfo createAvd(
            File avdFolder,
            String name,
            IAndroidTarget target,
            String skinName,
            String sdcard,
            Map<String,String> hardwareConfig,
            boolean createSnapshot,
            boolean removePrevious,
            boolean editExisting,
            ISdkLog log) {
if (log == null) {
throw new IllegalArgumentException("log cannot be null");
}
//Synthetic comment -- @@ -633,8 +705,9 @@
} catch (SecurityException e) {
log.error(e, "Failed to delete %1$s", avdFolder.getAbsolutePath());
}
                } else if (!editExisting) {
                    // AVD shouldn't already exist if removePrevious is false and
                    // we're not editing an existing AVD.
log.error(null,
"Folder %1$s is in the way. Use --force if you want to overwrite.",
avdFolder.getAbsolutePath());
//Synthetic comment -- @@ -643,6 +716,8 @@
} else {
// create the AVD folder.
avdFolder.mkdir();
                // We're not editing an existing AVD.
                editExisting = false;
}

// actually write the ini file
//Synthetic comment -- @@ -677,17 +752,23 @@

// Create the snapshot file
if (createSnapshot) {
File snapshotDest = new File(avdFolder, SNAPSHOTS_IMG);
                if (snapshotDest.isFile() && editExisting) {
                    log.printf("Snapshot image already present, was not changed.");

                } else {
                    String toolsLib = mSdkManager.getLocation() + File.separator
                            + SdkConstants.OS_SDK_TOOLS_LIB_EMULATOR_FOLDER;
                    File snapshotBlank = new File(toolsLib, SNAPSHOTS_IMG);
                    if (snapshotBlank.exists() == false) {
                        log.error(null,
                                "Unable to find a '%2$s%1$s' file to copy into the AVD folder.",
                                SNAPSHOTS_IMG, toolsLib);
                        needCleanup = true;
                        return null;
                    }
                    copyImageFile(snapshotBlank, snapshotDest);
                }
values.put(AVD_INI_SNAPSHOT_PRESENT, "true");
}

//Synthetic comment -- @@ -716,46 +797,48 @@
}

if (sdcard != null && sdcard.length() > 0) {
                // Sdcard is possibly a size. In that case we create a file called 'sdcard.img'
                // in the AVD folder, and do not put any value in config.ini.

                long sdcardSize = parseSdcardSize(sdcard, null/*parsedStrings*/);

                if (sdcardSize == SDCARD_SIZE_NOT_IN_RANGE) {
                    log.error(null, "SD Card size must be in the range 9 MiB..1023 GiB.");
                    needCleanup = true;
                    return null;

                } else if (sdcardSize == SDCARD_SIZE_INVALID) {
                    log.error(null, "Unable to parse SD Card size");
                    needCleanup = true;
                    return null;

                } else if (sdcardSize == SDCARD_NOT_SIZE_PATTERN) {
                    File sdcardFile = new File(sdcard);
                    if (sdcardFile.isFile()) {
                        // sdcard value is an external sdcard, so we put its path into the config.ini
                        values.put(AVD_INI_SDCARD_PATH, sdcard);
                    } else {
                        log.error(null, "'%1$s' is not recognized as a valid sdcard value.\n"
                                + "Value should be:\n" + "1. path to an sdcard.\n"
                                + "2. size of the sdcard to create: <size>[K|M]", sdcard);
                        needCleanup = true;
                        return null;
                    }
} else {
                    // create the sdcard.
                    File sdcardFile = new File(avdFolder, SDCARD_IMG);

                    boolean runMkSdcard = true;
                    if (sdcardFile.exists()) {
                        if (sdcardFile.length() == sdcardSize && editExisting) {
                            // There's already an sdcard file with the right size and we're
                            // not overriding it... so don't remove it.
                            runMkSdcard = false;
                            log.printf("SD Card already present with same size, was not changed.");
}
                    }

                    if (runMkSdcard) {
String path = sdcardFile.getAbsolutePath();

// execute mksdcard with the proper parameters.
//Synthetic comment -- @@ -775,17 +858,11 @@
return null; // mksdcard output has already been displayed, no need to
// output anything else.
}
}

                    // add a property containing the size of the sdcard for display purpose
                    // only when the dev does 'android list avd'
                    values.put(AVD_INI_SDCARD_SIZE, sdcard);
}
}

//Synthetic comment -- @@ -840,11 +917,21 @@
StringBuilder report = new StringBuilder();

if (target.isPlatform()) {
                if (editExisting) {
                    report.append(String.format("Updated AVD '%1$s' based on %2$s",
                            name, target.getName()));
                } else {
                    report.append(String.format("Created AVD '%1$s' based on %2$s",
                            name, target.getName()));
                }
} else {
                if (editExisting) {
                    report.append(String.format("Updated AVD '%1$s' based on %2$s (%3$s)", name,
                            target.getName(), target.getVendor()));
                } else {
                    report.append(String.format("Created AVD '%1$s' based on %2$s (%3$s)", name,
                            target.getName(), target.getVendor()));
                }
}

// display the chosen hardware config
//Synthetic comment -- @@ -868,14 +955,14 @@
AvdInfo oldAvdInfo = getAvd(name, false /*validAvdOnly*/);

synchronized (mAllAvdList) {
                if (oldAvdInfo != null && (removePrevious || editExisting)) {
mAllAvdList.remove(oldAvdInfo);
}
mAllAvdList.add(newAvdInfo);
mValidAvdList = mBrokenAvdList = null;
}

            if ((removePrevious || editExisting) &&
newAvdInfo != null &&
oldAvdInfo != null &&
!oldAvdInfo.getPath().equals(newAvdInfo.getPath())) {
//Synthetic comment -- @@ -1049,14 +1136,18 @@
IAndroidTarget target,
boolean removePrevious)
throws AndroidLocationException, IOException {
File iniFile = AvdInfo.getIniFile(name);

        if (removePrevious) {
            if (iniFile.isFile()) {
                iniFile.delete();
            } else if (iniFile.isDirectory()) {
                deleteContentOf(iniFile);
                iniFile.delete();
            }
}

        HashMap<String, String> values = new HashMap<String, String>();
values.put(AVD_INFO_PATH, avdFolder.getAbsolutePath());
values.put(AVD_INFO_TARGET, target.hashString());
writeIniFile(iniFile, values);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 3f42414..7666b50 100644

//Synthetic comment -- @@ -98,6 +98,10 @@
private final ArrayList<String> mEditedProperties = new ArrayList<String>();
private final ImageFactory mImageFactory;
private final ISdkLog mSdkLog;
    /**
     * The original AvdInfo if we're editing an existing AVD.
     * Null when we're creating a new AVD.
     */
private final AvdInfo mEditAvdInfo;

private Text mAvdName;
//Synthetic comment -- @@ -154,6 +158,9 @@
public void modifyText(ModifyEvent e) {
String name = mAvdName.getText().trim();
if (mEditAvdInfo == null || !name.equals(mEditAvdInfo.getName())) {
                // Case where we're creating a new AVD or editing an existing one
                // and the AVD name has been changed... check for name uniqueness.

Pair<AvdConflict, String> conflict = mAvdManager.isAvdNameConflicting(name);
if (conflict.getFirst() != AvdManager.AvdConflict.NO_CONFLICT) {
// If we're changing the state from disabled to enabled, make sure
//Synthetic comment -- @@ -169,8 +176,10 @@
mForceCreation.setSelection(false);
}
} else {
                // Case where we're editing an existing AVD with the name unchanged.

mForceCreation.setEnabled(false);
                mForceCreation.setSelection(false);
}
validatePage();
}
//Synthetic comment -- @@ -677,71 +686,78 @@
}

Map<String, String> props = mEditAvdInfo.getProperties();
        if (props != null) {
            // First try the skin name and if it doesn't work fallback on the skin path
            nextSkin: for (int s = 0; s < 2; s++) {
                String skin = props.get(s == 0 ? AvdManager.AVD_INI_SKIN_NAME
                                               : AvdManager.AVD_INI_SKIN_PATH);
                if (skin != null && skin.length() > 0) {
                    Matcher m = AvdManager.NUMERIC_SKIN_SIZE.matcher(skin);
                    if (m.matches() && m.groupCount() == 2) {
                        enableSkinWidgets(false);
                        mSkinListRadio.setSelection(false);
                        mSkinSizeRadio.setSelection(true);
                        mSkinSizeWidth.setText(m.group(1));
                        mSkinSizeHeight.setText(m.group(2));
                        break nextSkin;
                    } else {
                        enableSkinWidgets(true);
                        mSkinSizeRadio.setSelection(false);
                        mSkinListRadio.setSelection(true);

                        int n = mSkinCombo.getItemCount();
                        for (int i = 0; i < n; i++) {
                            if (skin.equals(mSkinCombo.getItem(i))) {
                                mSkinCombo.select(i);
                                break nextSkin;
                            }
}
}
}
}

            String sdcard = props.get(AvdManager.AVD_INI_SDCARD_PATH);
            if (sdcard != null && sdcard.length() > 0) {
                enableSdCardWidgets(false);
                mSdCardSizeRadio.setSelection(false);
                mSdCardFileRadio.setSelection(true);
                mSdCardFile.setText(sdcard);
            }

            sdcard = props.get(AvdManager.AVD_INI_SDCARD_SIZE);
            if (sdcard != null && sdcard.length() > 0) {

                String[] values = new String[2];
                long sdcardSize = AvdManager.parseSdcardSize(sdcard, values);

                if (sdcardSize != AvdManager.SDCARD_NOT_SIZE_PATTERN) {
                    enableSdCardWidgets(true);
                    mSdCardFileRadio.setSelection(false);
                    mSdCardSizeRadio.setSelection(true);

                    mSdCardSize.setText(values[0]);

                    String suffix = values[1];
                    int n = mSdCardSizeCombo.getItemCount();
                    for (int i = 0; i < n; i++) {
                        if (mSdCardSizeCombo.getItem(i).startsWith(suffix)) {
                            mSdCardSizeCombo.select(i);
                        }
}
}
}

            String snapshots = props.get(AvdManager.AVD_INI_SNAPSHOT_PRESENT);
            if (snapshots != null && snapshots.length() > 0) {
                mSnapshotCheck.setSelection(snapshots.equals("true"));
            }
}

mProperties.clear();

        if (props != null) {
            mProperties.putAll(props);
        }

// Cleanup known non-hardware properties
mProperties.remove(AvdManager.AVD_INI_SKIN_PATH);
//Synthetic comment -- @@ -939,6 +955,22 @@
}
if (value <= 0) {
error = "SD Card size is invalid. Range is 9 MiB..1023 GiB.";
                    } else if (mEditAvdInfo != null) {
                        // When editing an existing AVD, compare with the existing
                        // sdcard size, if any. It only matters if there was an sdcard setting
                        // before.
                        Map<String, String> props = mEditAvdInfo.getProperties();
                        if (props != null) {
                            String original =
                                mEditAvdInfo.getProperties().get(AvdManager.AVD_INI_SDCARD_SIZE);
                            if (original != null && original.length() > 0) {
                                long originalSize =
                                    AvdManager.parseSdcardSize(original, null/*parsedStrings*/);
                                if (originalSize > 0 && value != originalSize) {
                                    warning = "A new SD Card file will be created.\nThe current SD Card file will be lost.";
                                }
                            }
                        }
}
}
}
//Synthetic comment -- @@ -1193,8 +1225,9 @@
skinName,
sdName,
mProperties,
snapshot,
                force,
                mEditAvdInfo != null, //edit existing
log);

success = avdInfo != null;







