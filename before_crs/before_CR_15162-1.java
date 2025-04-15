/*Enforce english formatting for the geo string sent to the emulator.

Change-Id:I1fc90e332fff41e40d2fce740bce8e07d88586ce*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java b/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java
//Synthetic comment -- index 6ac019d..a37e03a 100644

//Synthetic comment -- @@ -25,7 +25,9 @@
import java.nio.channels.SocketChannel;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -540,13 +542,15 @@

double latMinute = (absLat - Math.floor(absLat)) * 60;

        String command = String.format(COMMAND_GPS,
c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND),
latDegree, latMinute, latDirection,
longDegree, longMinute, longDirection);

        return processCommand(command);
}

/**







