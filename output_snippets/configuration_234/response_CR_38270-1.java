//<Beginning of snippet n. 0>

import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.util.XmlUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

IAndroidTarget[] targets = Sdk.getCurrent().getTargets();
for (IAndroidTarget target : targets) {
    if (!target.hasExtraApis()) {
        continue;
    }
    AndroidVersion version = target.getVersion();
    if (version.getApiLevel() == api) {
        return String.format("API %1$d: Android %2$s", api,
                target.getProperty("ro.build.version.release"));
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>

mBuildSdkCombo = new Combo(container, SWT.READ_ONLY);
mBuildSdkCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
IAndroidTarget[] targets = Sdk.getCurrent().getTargets();
mMinNameToApi = Maps.newHashMap();
List<String> labels = new ArrayList<String>(targets.length);
for (IAndroidTarget target : targets) {
    String targetLabel = target.getFullName();
    labels.add(targetLabel);
    mMinNameToApi.put(targetLabel, target.getVersion().getApiLevel());

    AndroidVersion version = target.getVersion();
    int apiLevel = version.getApiLevel();
    if (version.isPreview()) {
        String codeName = version.getCodename();
        String targetLabelPreview = "API " + apiLevel + ":" + codeName;
        labels.add(targetLabelPreview);
        mMinNameToApi.put(targetLabelPreview, apiLevel);
    } else if (target.isPlatform()) {
        // Existing logic is handled here
    }
}

private ControlDecoration createFieldDecoration(Control control, String description) {
    ControlDecoration dec = new ControlDecoration(control, SWT.LEFT);
    dec.setMarginWidth(2);
    if (mValues.target == null) {
        status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
                "Select a minimum SDK version");
    } else if (mValues.target.getVersion().getApiLevel() < mValues.minSdkLevel) {
        status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
                "The minimum SDK version is higher than the build target version");
    }
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
    }
    // additional logic should be provided here
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>

// AndroidVersion

public static final String VERSION_API_LEVEL = "AndroidVersion.ApiLevel";//$NON-NLS-1$
public static final String VERSION_CODENAME = "AndroidVersion.CodeName";//$NON-NLS-1$

// PlatformPackage

public static final String PLATFORM_VERSION = "Platform.Version";       //$NON-NLS-1$
public static final String PLATFORM_INCLUDED_ABI = "Platform.Included.Abi";  //$NON-NLS-1$

// ToolPackage

//<End of snippet n. 3>