/*AVD Editor: support Enum Hardware Properties.

Used to support the enum touch screen type.

(cherry picked from commit bdb7dd94d76db3201773bdc593d72633daaaa07a)

Change-Id:Ic5d5a3d23ec4db1e00564373bdbd08c7c1957cf3*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java
//Synthetic comment -- index 88495e0..a7a3157 100644

//Synthetic comment -- @@ -33,36 +33,56 @@
private final static Pattern PATTERN_PROP = Pattern.compile(
"^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

    /** Property name in the generated avd config file; String; e.g. "hw.screen" */
    private final static String HW_PROP_NAME = "name";              //$NON-NLS-1$
    /** Property type, one of {@link HardwarePropertyType} */
    private final static String HW_PROP_TYPE = "type";              //$NON-NLS-1$
    /** Default value of the property. String matching the property type. */
    private final static String HW_PROP_DEFAULT = "default";        //$NON-NLS-1$
    /** User-visible name of the property. String. */
    private final static String HW_PROP_ABSTRACT = "abstract";      //$NON-NLS-1$
    /** User-visible description of the property. String. */
    private final static String HW_PROP_DESC = "description";       //$NON-NLS-1$
    /** Comma-separate values for a property of type "enum" */
    private final static String HW_PROP_ENUM = "enum";              //$NON-NLS-1$

private final static String BOOLEAN_YES = "yes";
private final static String BOOLEAN_NO = "no";
public final static String[] BOOLEAN_VALUES = new String[] { BOOLEAN_YES, BOOLEAN_NO };
    public final static Pattern DISKSIZE_PATTERN = Pattern.compile("\\d+[MK]B"); //$NON-NLS-1$

    /** Represents the type of a hardware property value. */
    public enum HardwarePropertyType {
        INTEGER     ("integer",  false /*isEnum*/),     //$NON-NLS-1$
        BOOLEAN     ("boolean",  false /*isEnum*/),     //$NON-NLS-1$
        DISKSIZE    ("diskSize", false /*isEnum*/),     //$NON-NLS-1$
        STRING      ("string",   false /*isEnum*/),     //$NON-NLS-1$
        INTEGER_ENUM("integer",  true  /*isEnum*/),     //$NON-NLS-1$
        STRING_ENUM ("string",   true  /*isEnum*/);     //$NON-NLS-1$


        private String mName;
        private boolean mIsEnum;

        HardwarePropertyType(String name, boolean isEnum) {
            mName = name;
            mIsEnum = isEnum;
}

        /** Returns the name of the type (e.g. "string", "boolean", etc.) */
        public String getName() {
            return mName;
}

        /** Indicates whether this type is an enum (e.g. "enum of strings"). */
        public boolean isEnum() {
            return mIsEnum;
        }

        /** Returns the internal HardwarePropertyType object matching the given type name. */
        public static HardwarePropertyType getEnum(String name, boolean isEnum) {
            for (HardwarePropertyType type : values()) {
                if (type.mName.equals(name) && type.mIsEnum == isEnum) {
return type;
}
}
//Synthetic comment -- @@ -73,9 +93,11 @@

public static final class HardwareProperty {
private String mName;
        private HardwarePropertyType mType;
/** the string representation of the default value. can be null. */
private String mDefault;
        /** the choices for an enum. Null if not an enum. */
        private String[] mEnum;
private String mAbstract;
private String mDescription;

//Synthetic comment -- @@ -88,29 +110,43 @@
mDescription = "";
}

        /** Returns the hardware config name of the property, e.g. "hw.screen" */
public String getName() {
return mName;
}

        /** Returns the property type, one of {@link HardwarePropertyType} */
        public HardwarePropertyType getType() {
return mType;
}

        /**
         * Returns the default value of the property.
         * String matching the property type.
         * Can be null.
         */
public String getDefault() {
return mDefault;
}

        /** Returns the user-visible name of the property. */
public String getAbstract() {
return mAbstract;
}

        /** Returns the user-visible description of the property. */
public String getDescription() {
return mDescription;
}

        /** Returns the possible values for an enum property. Can be null. */
        public String[] getEnum() {
            return mEnum;
        }

public boolean isValidForUi() {
            // don't display single string type for now.
            return mType != HardwarePropertyType.STRING || mType.isEnum();
}
}

//Synthetic comment -- @@ -134,10 +170,10 @@
if (line.length() > 0 && line.charAt(0) != '#') {
Matcher m = PATTERN_PROP.matcher(line);
if (m.matches()) {
                        String key = m.group(1);
String value = m.group(2);

                        if (HW_PROP_NAME.equals(key)) {
prop = new HardwareProperty();
prop.mName = value;
map.put(prop.mName, prop);
//Synthetic comment -- @@ -149,14 +185,42 @@
return null;
}

                        if (HW_PROP_TYPE.equals(key)) {
                            // Note: we don't know yet whether this type is an enum.
                            // This is indicated by the "enum = value" line that is parsed later.
                            prop.mType = HardwarePropertyType.getEnum(value, false);
                            assert (prop.mType != null);
                        } else if (HW_PROP_DEFAULT.equals(key)) {
prop.mDefault = value;
                        } else if (HW_PROP_ABSTRACT.equals(key)) {
prop.mAbstract = value;
                        } else if (HW_PROP_DESC.equals(key)) {
prop.mDescription = value;
                        } else if (HW_PROP_ENUM.equals(key)) {
                            if (!prop.mType.isEnum()) {
                                // Change the type to an enum, if valid.
                                prop.mType = HardwarePropertyType.getEnum(prop.mType.getName(),
                                                                          true);
                                assert (prop.mType != null);
                            }

                            // Sanitize input: trim spaces, ignore empty entries.
                            String[] v = value.split(",");
                            int n = 0;
                            for (int i = 0; i < v.length; i++) {
                                String s = v[i] = v[i].trim();
                                if (s.length() > 0) {
                                    n++;
                                }
                            }
                            prop.mEnum = new String[n];
                            n = 0;
                            for (int i = 0; i < v.length; i++) {
                                String s = v[i];
                                if (s.length() > 0) {
                                    prop.mEnum[n++] = s;
                                }
                            }
}
} else {
log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 4e54da0..5d40a40 100644

//Synthetic comment -- @@ -619,6 +619,7 @@
protected void setValue(Object element, Object value) {
String hardwareName = (String)element;
HardwareProperty property = mHardwareMap.get(hardwareName);
                int index;
switch (property.getType()) {
case INTEGER:
mProperties.put((String)element, (String)value);
//Synthetic comment -- @@ -629,9 +630,18 @@
}
break;
case BOOLEAN:
                        index = (Integer)value;
mProperties.put((String)element, HardwareProperties.BOOLEAN_VALUES[index]);
break;
                    case STRING_ENUM:
                    case INTEGER_ENUM:
                        // For a combo, value is the index of the enum to use.
                        index = (Integer)value;
                        String[] values = property.getEnum();
                        if (values != null && values.length > index) {
                            mProperties.put((String)element, values[index]);
                        }
                        break;
}
mHardwareViewer.refresh(element);
}
//Synthetic comment -- @@ -648,6 +658,17 @@
return value;
case BOOLEAN:
return HardwareProperties.getBooleanValueIndex(value);
                    case STRING_ENUM:
                    case INTEGER_ENUM:
                        // For a combo, we need to return the index of the value in the enum
                        String[] values = property.getEnum();
                        if (values != null) {
                            for (int i = 0; i < values.length; i++) {
                                if (values[i].equals(value)) {
                                    return i;
                                }
                            }
                        }
}

return null;
//Synthetic comment -- @@ -667,6 +688,14 @@
return new ComboBoxCellEditor(hardwareTable,
HardwareProperties.BOOLEAN_VALUES,
SWT.READ_ONLY | SWT.DROP_DOWN);
                    case STRING_ENUM:
                    case INTEGER_ENUM:
                        String[] values = property.getEnum();
                        if (values != null && values.length > 0) {
                            return new ComboBoxCellEditor(hardwareTable,
                                    values,
                                    SWT.READ_ONLY | SWT.DROP_DOWN);
                        }
}
return null;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java
//Synthetic comment -- index d92e0fb..a07768c 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.sdkuilib.internal.widgets;

import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.avd.HardwareProperties.HardwarePropertyType;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -30,8 +30,11 @@

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
* Dialog to choose a hardware property
//Synthetic comment -- @@ -44,7 +47,8 @@
private Label mTypeLabel;
private Label mDescriptionLabel;

    HardwarePropertyChooser(Shell parentShell,
            Map<String, HardwareProperty> properties,
Collection<String> exceptProperties) {
super(parentShell, 2, false);
mProperties = properties;
//Synthetic comment -- @@ -63,7 +67,25 @@
final Combo c = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
// simple list for index->name resolution.
final ArrayList<String> indexToName = new ArrayList<String>();

        // Sort the combo entries by display name if available, otherwise by hardware name.
        Set<Entry<String, HardwareProperty>> entries =
            new TreeSet<Map.Entry<String,HardwareProperty>>(
                    new Comparator<Map.Entry<String,HardwareProperty>>() {
                @Override
                public int compare(Entry<String, HardwareProperty> entry0,
                                   Entry<String, HardwareProperty> entry1) {
                    String s0 = entry0.getValue().getAbstract();
                    String s1 = entry1.getValue().getAbstract();
                    if (s0 != null && s1 != null) {
                        return s0.compareTo(s1);
                    }
                    return entry0.getKey().compareTo(entry1.getKey());
                }
            });
        entries.addAll(mProperties.entrySet());

        for (Entry<String, HardwareProperty> entry : entries) {
if (entry.getValue().isValidForUi() &&
mExceptProperties.contains(entry.getKey()) == false) {
c.add(entry.getValue().getAbstract());
//Synthetic comment -- @@ -111,9 +133,9 @@

if (mChosenProperty != null) {
desc = mChosenProperty.getDescription();
            HardwarePropertyType vt = mChosenProperty.getType();
if (vt != null) {
                type = vt.getName();
}
}








