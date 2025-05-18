//<Beginning of snippet n. 0>

import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.util.XmlUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

IAndroidTarget[] targets = Sdk.getCurrent().getTargets();
for (IAndroidTarget target : targets) {
    if (target.isPlatform() && target.hasExtraApis()) {
        AndroidVersion version = target.getVersion();
        if (version != null) {
            if (version.getApiLevel() == api) {
                return String.format("API %1$d: Android %2$s", api,
                        target.getProperty("ro.build.version.release"));
            }
            if (version.isPreview()) {
                String codeName = version.getCodename();
                if (codeName != null) {
                    return String.format("API %1$d: Android %2$s", version.getApiLevel(), codeName);
                }
            }
        }
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

mBuildSdkCombo = new Combo(container, SWT.READ_ONLY);
mBuildSdkCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
IAndroidTarget[] targets = Sdk.getCurrent().getTargets();
mMinNameToApi = Maps.newHashMap();
List<String> labels = new ArrayList<>(targets.length);
for (IAndroidTarget target : targets) {
    AndroidVersion version = target.getVersion();
    if (version != null) {
        String targetLabel = target.getFullName();
        labels.add(targetLabel);
        mMinNameToApi.put(targetLabel, version.getApiLevel());

        int apiLevel = version.getApiLevel();
        if (version.isPreview()) {
            String codeName = version.getCodename();
            if (codeName != null) {
                String targetLabelPreview = "API " + apiLevel + ":" + codeName;
                labels.add(targetLabelPreview);
                mMinNameToApi.put(targetLabelPreview, apiLevel);
            }
        } else if (target.isPlatform() && target.hasExtraApis()) {
            // additional logic if necessary
        }
    }
}

private ControlDecoration createFieldDecoration(Control control, String description) {
    ControlDecoration dec = new ControlDecoration(control, SWT.LEFT);
    dec.setMarginWidth(2);
    Status status = null;
    if (mValues.target == null) {
        status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID, "Select a minimum SDK version");
    } else if (mValues.target.getVersion() == null) {
        status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID, "The selected target does not have a valid version");
    } else {
        int apiLevelValue = mValues.target.getVersion().getApiLevel();
        if (apiLevelValue < mValues.minSdkLevel) {
            status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID, "The minimum SDK version is higher than the build target version");
        }
    }
    dec.setDescriptionText(description);
    return dec;
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

return compareTo(o.mApiLevel, o.mCodename);
}

private int compareTo(int apiLevel, String codename) {
    if (mCodename == null) {
        if (codename == null) {
            return mApiLevel - apiLevel;
        }
        return -1;
    }
    if (codename == null) {
        return 1;
    }
    return mCodename.compareTo(codename);
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>

public static final String VERSION_API_LEVEL = "AndroidVersion.ApiLevel";//$NON-NLS-1$
public static final String VERSION_CODENAME = "AndroidVersion.CodeName";//$NON-NLS-1$
public static final String PLATFORM_VERSION = "Platform.Version"; //$NON-NLS-1$
public static final String PLATFORM_INCLUDED_ABI = "Platform.Included.Abi"; //$NON-NLS-1$

//<End of snippet n. 3>