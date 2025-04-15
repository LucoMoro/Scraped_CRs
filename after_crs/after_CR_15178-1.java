/*Properly handle float/double in loc control when locale is not en.

String.format uses the decimal point of the current locale which
we don't want since we pass the result to the emulator which expects
decimal point to be '.'

Double.parseDouble does not use the current locale and therefore
failed to read values put in the UI that used a decimal point
that is not '.'

Change-Id:I275018e6a967c3d4fa37f25a149a840350e40bb8*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java b/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java
//Synthetic comment -- index a37e03a..23d834f 100644

//Synthetic comment -- @@ -67,10 +67,7 @@
private final static String COMMAND_NETWORK_STATUS = "network status\r\n"; //$NON-NLS-1$
private final static String COMMAND_NETWORK_SPEED = "network speed %1$s\r\n"; //$NON-NLS-1$
private final static String COMMAND_NETWORK_LATENCY = "network delay %1$s\r\n"; //$NON-NLS-1$
    private final static String COMMAND_GPS = "geo fix %1$f %2$f %3$f\r\n"; //$NON-NLS-1$

private final static Pattern RE_KO = Pattern.compile("KO:\\s+(.*)"); //$NON-NLS-1$

//Synthetic comment -- @@ -522,35 +519,20 @@

public synchronized String sendLocation(double longitude, double latitude, double elevation) {

// need to make sure the string format uses dot and not comma
        try {
            Formatter formatter = new Formatter(Locale.US);
            formatter.format(COMMAND_GPS, longitude, latitude, elevation);

            String foo = formatter.toString();
            System.out.println(foo);

            return processCommand(foo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/location/CoordinateControls.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/location/CoordinateControls.java
//Synthetic comment -- index 578a7ac..0620f76 100644

//Synthetic comment -- @@ -25,6 +25,9 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
* Encapsulation of controls handling a location coordinate in decimal and sexagesimal.
* <p/>This handle the conversion between both modes automatically by using a {@link ModifyListener}
//Synthetic comment -- @@ -39,11 +42,12 @@
private Text mSexagesimalDegreeText;
private Text mSexagesimalMinuteText;
private Text mSexagesimalSecondText;
    private final DecimalFormat mDecimalFormat = new DecimalFormat();

/** Internal flag to prevent {@link ModifyEvent} to be sent when {@link Text#setText(String)}
* is called. This is an int instead of a boolean to act as a counter. */
private int mManualTextChange = 0;

/**
* ModifyListener for the 3 {@link Text} controls of the sexagesimal mode.
*/
//Synthetic comment -- @@ -56,14 +60,14 @@
mValue = getValueFromSexagesimalControls();
setValueIntoDecimalControl(mValue);
mValueValidity = true;
            } catch (ParseException e) {
// wrong format empty the decimal controls.
mValueValidity = false;
resetDecimalControls();
}
}
};

/**
* Creates the {@link Text} control for the decimal display of the coordinate.
* <p/>The control is expected to be placed in a Composite using a {@link GridLayout}.
//Synthetic comment -- @@ -76,10 +80,10 @@
return;
}
try {
                    mValue = mDecimalFormat.parse(mDecimalText.getText()).doubleValue();
setValueIntoSexagesimalControl(mValue);
mValueValidity = true;
                } catch (ParseException e) {
// wrong format empty the sexagesimal controls.
mValueValidity = false;
resetSexagesimalControls();
//Synthetic comment -- @@ -87,7 +91,7 @@
}
});
}

/**
* Creates the {@link Text} control for the "degree" display of the coordinate in sexagesimal
* mode.
//Synthetic comment -- @@ -97,7 +101,7 @@
public void createSexagesimalDegreeText(Composite parent) {
mSexagesimalDegreeText = createTextControl(parent, "-199", mSexagesimalListener); //$NON-NLS-1$
}

/**
* Creates the {@link Text} control for the "minute" display of the coordinate in sexagesimal
* mode.
//Synthetic comment -- @@ -117,7 +121,7 @@
public void createSexagesimalSecondText(Composite parent) {
mSexagesimalSecondText = createTextControl(parent, "99.999", mSexagesimalListener); //$NON-NLS-1$
}

/**
* Sets the coordinate into the {@link Text} controls.
* @param value the coordinate value to set.
//Synthetic comment -- @@ -128,7 +132,7 @@
setValueIntoDecimalControl(value);
setValueIntoSexagesimalControl(value);
}

/**
* Returns whether the value in the control(s) is valid.
*/
//Synthetic comment -- @@ -144,7 +148,7 @@
public double getValue() {
return mValue;
}

/**
* Enables or disables all the {@link Text} controls.
* @param enabled the enabled state.
//Synthetic comment -- @@ -155,7 +159,7 @@
mSexagesimalMinuteText.setEnabled(enabled);
mSexagesimalSecondText.setEnabled(enabled);
}

private void resetDecimalControls() {
mManualTextChange++;
mDecimalText.setText(""); //$NON-NLS-1$
//Synthetic comment -- @@ -169,7 +173,7 @@
mSexagesimalSecondText.setText(""); //$NON-NLS-1$
mManualTextChange--;
}

/**
* Creates a {@link Text} with a given parent, default string and a {@link ModifyListener}
* @param parent the parent {@link Composite}.
//Synthetic comment -- @@ -182,10 +186,10 @@
ModifyListener listener) {
// create the control
Text text = new Text(parent, SWT.BORDER | SWT.LEFT | SWT.SINGLE);

// add the standard listener to it.
text.addModifyListener(listener);

// compute its size/
mManualTextChange++;
text.setText(defaultString);
//Synthetic comment -- @@ -193,23 +197,23 @@
Point size = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
text.setText(""); //$NON-NLS-1$
mManualTextChange--;

GridData gridData = new GridData();
gridData.widthHint = size.x;
text.setLayoutData(gridData);

return text;
}

    private double getValueFromSexagesimalControls() throws ParseException {
        double degrees = mDecimalFormat.parse(mSexagesimalDegreeText.getText()).doubleValue();
        double minutes = mDecimalFormat.parse(mSexagesimalMinuteText.getText()).doubleValue();
        double seconds = mDecimalFormat.parse(mSexagesimalSecondText.getText()).doubleValue();

boolean isPositive = (degrees >= 0.);
degrees = Math.abs(degrees);

        double value = degrees + minutes / 60. + seconds / 3600.;
return isPositive ? value : - value;
}

//Synthetic comment -- @@ -218,21 +222,21 @@
mDecimalText.setText(String.format("%.6f", value));
mManualTextChange--;
}

private void setValueIntoSexagesimalControl(double value) {
// get the sign and make the number positive no matter what.
boolean isPositive = (value >= 0.);
value = Math.abs(value);

// get the degree
double degrees = Math.floor(value);

// get the minutes
double minutes = Math.floor((value - degrees) * 60.);

// get the seconds.
double seconds = (value - degrees) * 3600. - minutes * 60.;

mManualTextChange++;
mSexagesimalDegreeText.setText(
Integer.toString(isPositive ? (int)degrees : (int)- degrees));







