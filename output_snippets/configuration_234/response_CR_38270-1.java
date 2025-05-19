//<Beginning of snippet n. 0>

import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.util.XmlUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

for (IAndroidTarget target : Sdk.getCurrent().getTargets()) {
    if (target.isPlatform() && target.hasExtraApis()) {
        AndroidVersion version = target.getVersion();
        if (version != null) {
            if (version.getApiLevel() == api) {
                return String.format("API %1$d: Android %2$s", api,
                    target.getProperty("ro.build.version.release"));
            }
            if (version.isPreview()) {
                String codeName = version.getCodename();
                return String.format("API %1$d: Android %2$s (%3$s)", 
                    version.getApiLevel(), 
                    target.getProperty("ro.build.version.release"),
                    (codeName != null) ? codeName : "Unknown");
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
List<String> labels = new ArrayList<String>(targets.length);
List<String> codeNames = new ArrayList<String>();

for (IAndroidTarget target : targets) {
    AndroidVersion targetVersion = target.getVersion();
    if (targetVersion != null && targetVersion.getApiLevel() >= mMinSdkLevel && target.hasExtraApis()) {
        String targetLabel = target.getFullName();
        labels.add(targetLabel);
        mMinNameToApi.put(targetLabel, targetVersion.getApiLevel());
    
        int apiLevel = targetVersion.getApiLevel();
        if (targetVersion.isPreview()) {
            String codeName = targetVersion.getCodename();
            targetLabel = "API " + apiLevel + ":" + (codeName != null ? codeName : "Unknown");
            codeNames.add(targetLabel);
            mMinNameToApi.put(targetLabel, apiLevel);
        }
    }
}

private ControlDecoration createFieldDecoration(Control control, String description) {
    ControlDecoration dec = new ControlDecoration(control, SWT.LEFT);
    dec.setMarginWidth(2);
    if (mValues.target != null && mValues.target.getVersion() != null) {
        if (mValues.target.getVersion().getApiLevel() < mValues.minSdkLevel) {
            status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
                "The minimum SDK version (" + mValues.minSdkLevel + ") is higher than the build target version (" + mValues.target.getVersion().getApiLevel() + ")");
        } else {
            status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
                "Select a minimum SDK version");
        }
    } else {
        status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
            "Target or target version is not set");
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

return compareTo(o.mApiLevel, o.mCodename);
}

private int compareTo(int apiLevel, String codename) {
    if (mCodename != null || codename != null) {
        if (mCodename == null && codename != null) {
            return mApiLevel - apiLevel;
        }
    }
    // Additional comparison logic here
    return 0; 
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>

public static final String VERSION_API_LEVEL = "AndroidVersion.ApiLevel";//$NON-NLS-1$
public static final String VERSION_CODENAME = "AndroidVersion.CodeName";//$NON-NLS-1$

public static final String PLATFORM_VERSION = "Platform.Version"; //$NON-NLS-1$
public static final String PLATFORM_INCLUDED_ABI = "Platform.Included.Abi"; //$NON-NLS-1$

//<End of snippet n. 3>