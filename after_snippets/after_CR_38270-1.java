
//<Beginning of snippet n. 0>


import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.repository.PkgProps;
import com.android.util.XmlUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
if (target.isPlatform()) {
AndroidVersion version = target.getVersion();
if (version.getApiLevel() == api) {
                                String codename = target.getProperty(PkgProps.PLATFORM_CODENAME);
                                if (codename != null) {
                                    return String.format("API %1$d: Android %2$s (%3$s)", api,
                                            target.getProperty("ro.build.version.release"), //$NON-NLS-1$
                                            codename);
                                }
return String.format("API %1$d: Android %2$s", api,
                                        target.getProperty("ro.build.version.release")); //$NON-NLS-1$
}
}
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



mBuildSdkCombo = new Combo(container, SWT.READ_ONLY);
mBuildSdkCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        IAndroidTarget[] targets = getCompilationTargets();
mMinNameToApi = Maps.newHashMap();
List<String> labels = new ArrayList<String>(targets.length);
for (IAndroidTarget target : targets) {
            String targetLabel = String.format("%1$s (API %2$s)", target.getFullName(),
                    target.getVersion().getApiString());
labels.add(targetLabel);
mMinNameToApi.put(targetLabel, target.getVersion().getApiLevel());

int apiLevel = version.getApiLevel();
if (version.isPreview()) {
String codeName = version.getCodename();
                String targetLabel = codeName + " Preview";
codeNames.add(targetLabel);
mMinNameToApi.put(targetLabel, apiLevel);
} else if (target.isPlatform()

}

    private IAndroidTarget[] getCompilationTargets() {
        IAndroidTarget[] targets = Sdk.getCurrent().getTargets();
        List<IAndroidTarget> list = new ArrayList<IAndroidTarget>();

        for (IAndroidTarget target : targets) {
            if (target.isPlatform() == false &&
                    (target.getOptionalLibraries() == null ||
                            target.getOptionalLibraries().length == 0)) {
                continue;
            }
            list.add(target);
        }

        return list.toArray(new IAndroidTarget[list.size()]);
    }

private ControlDecoration createFieldDecoration(Control control, String description) {
ControlDecoration dec = new ControlDecoration(control, SWT.LEFT);
dec.setMarginWidth(2);
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
"Select a minimum SDK version");
} else {
                    AndroidVersion version = mValues.target.getVersion();
                    if (version.isPreview()) {
                        if (version.getCodename().equals(mValues.minSdk) == false) {
                            status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                            "Preview platforms require the min SDK version to match their codenames.");
                       }
                    } else if (mValues.target.getVersion().compareTo(
                            mValues.minSdkLevel, mValues.minSdk) < 0) {
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
"The minimum SDK version is higher than the build target version");
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


return compareTo(o.mApiLevel, o.mCodename);
}

    public int compareTo(int apiLevel, String codename) {
if (mCodename == null) {
if (codename == null) {
return mApiLevel - apiLevel;

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


// AndroidVersion

public static final String VERSION_API_LEVEL        = "AndroidVersion.ApiLevel";//$NON-NLS-1$
    /** Code name of the platform if the platform is not final */
public static final String VERSION_CODENAME         = "AndroidVersion.CodeName";//$NON-NLS-1$


// PlatformPackage

public static final String PLATFORM_VERSION         = "Platform.Version";       //$NON-NLS-1$
    /** Code name of the platform. This has no bearing on the package being a preview or not. */
    public static final String PLATFORM_CODENAME        = "Platform.CodeName";      //$NON-NLS-1$
public static final String PLATFORM_INCLUDED_ABI    = "Platform.Included.Abi";  //$NON-NLS-1$

// ToolPackage

//<End of snippet n. 3>








