/*AVD Editor: support Enum Hardware Properties.

Used to support the enum touch screen type.

(cherry picked from commit bdb7dd94d76db3201773bdc593d72633daaaa07a)

Change-Id:Ic5d5a3d23ec4db1e00564373bdbd08c7c1957cf3*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java
//Synthetic comment -- index 88495e0..a7a3157 100644

//Synthetic comment -- @@ -33,36 +33,56 @@
private final static Pattern PATTERN_PROP = Pattern.compile(
"^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

    private final static String HW_PROP_NAME = "name";
    private final static String HW_PROP_TYPE = "type";
    private final static String HW_PROP_DEFAULT = "default";
    private final static String HW_PROP_ABSTRACT = "abstract";
    private final static String HW_PROP_DESC = "description";

private final static String BOOLEAN_YES = "yes";
private final static String BOOLEAN_NO = "no";
public final static String[] BOOLEAN_VALUES = new String[] { BOOLEAN_YES, BOOLEAN_NO };
    public final static Pattern DISKSIZE_PATTERN = Pattern.compile("\\d+[MK]B");

    public enum ValueType {
        INTEGER("integer"),
        BOOLEAN("boolean"),
        DISKSIZE("diskSize"),
        STRING("string");

        private String mValue;

        ValueType(String value) {
            mValue = value;
}

        public String getValue() {
            return mValue;
}

        public static ValueType getEnum(String value) {
            for (ValueType type : values()) {
                if (type.mValue.equals(value)) {
return type;
}
}
//Synthetic comment -- @@ -73,9 +93,11 @@

public static final class HardwareProperty {
private String mName;
        private ValueType mType;
/** the string representation of the default value. can be null. */
private String mDefault;
private String mAbstract;
private String mDescription;

//Synthetic comment -- @@ -88,29 +110,43 @@
mDescription = "";
}

public String getName() {
return mName;
}

        public ValueType getType() {
return mType;
}

public String getDefault() {
return mDefault;
}

public String getAbstract() {
return mAbstract;
}

public String getDescription() {
return mDescription;
}

public boolean isValidForUi() {
            // don't show display string type for now.
            return mType != ValueType.STRING;
}
}

//Synthetic comment -- @@ -134,10 +170,10 @@
if (line.length() > 0 && line.charAt(0) != '#') {
Matcher m = PATTERN_PROP.matcher(line);
if (m.matches()) {
                        String valueName = m.group(1);
String value = m.group(2);

                        if (HW_PROP_NAME.equals(valueName)) {
prop = new HardwareProperty();
prop.mName = value;
map.put(prop.mName, prop);
//Synthetic comment -- @@ -149,14 +185,42 @@
return null;
}

                        if (HW_PROP_TYPE.equals(valueName)) {
                            prop.mType = ValueType.getEnum(value);
                        } else if (HW_PROP_DEFAULT.equals(valueName)) {
prop.mDefault = value;
                        } else if (HW_PROP_ABSTRACT.equals(valueName)) {
prop.mAbstract = value;
                        } else if (HW_PROP_DESC.equals(valueName)) {
prop.mDescription = value;
}
} else {
log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 4e54da0..5d40a40 100644

//Synthetic comment -- @@ -619,6 +619,7 @@
protected void setValue(Object element, Object value) {
String hardwareName = (String)element;
HardwareProperty property = mHardwareMap.get(hardwareName);
switch (property.getType()) {
case INTEGER:
mProperties.put((String)element, (String)value);
//Synthetic comment -- @@ -629,9 +630,18 @@
}
break;
case BOOLEAN:
                        int index = (Integer)value;
mProperties.put((String)element, HardwareProperties.BOOLEAN_VALUES[index]);
break;
}
mHardwareViewer.refresh(element);
}
//Synthetic comment -- @@ -648,6 +658,17 @@
return value;
case BOOLEAN:
return HardwareProperties.getBooleanValueIndex(value);
}

return null;
//Synthetic comment -- @@ -667,6 +688,14 @@
return new ComboBoxCellEditor(hardwareTable,
HardwareProperties.BOOLEAN_VALUES,
SWT.READ_ONLY | SWT.DROP_DOWN);
}
return null;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java
//Synthetic comment -- index d92e0fb..a07768c 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.sdkuilib.internal.widgets;

import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.avd.HardwareProperties.ValueType;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -30,8 +30,11 @@

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
* Dialog to choose a hardware property
//Synthetic comment -- @@ -44,7 +47,8 @@
private Label mTypeLabel;
private Label mDescriptionLabel;

    HardwarePropertyChooser(Shell parentShell, Map<String, HardwareProperty> properties,
Collection<String> exceptProperties) {
super(parentShell, 2, false);
mProperties = properties;
//Synthetic comment -- @@ -63,7 +67,25 @@
final Combo c = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
// simple list for index->name resolution.
final ArrayList<String> indexToName = new ArrayList<String>();
        for (Entry<String, HardwareProperty> entry : mProperties.entrySet()) {
if (entry.getValue().isValidForUi() &&
mExceptProperties.contains(entry.getKey()) == false) {
c.add(entry.getValue().getAbstract());
//Synthetic comment -- @@ -111,9 +133,9 @@

if (mChosenProperty != null) {
desc = mChosenProperty.getDescription();
            ValueType vt = mChosenProperty.getType();
if (vt != null) {
                type = vt.getValue();
}
}








