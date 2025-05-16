```
<<Beginning of snippet n. 0>>
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import android.content.res.Resources;
import android.content.res.Configuration;

private static DateTimeFormatter sStatusTimeFormat;
private static Configuration sLastConfig;
private static final Object sLock = new Object();

private static void initFormatStrings() {
    Configuration cfg = Resources.getSystem().getConfiguration();
    if (sLastConfig == null || !sLastConfig.equals(cfg)) {
        synchronized (sLock) {
            if (sLastConfig == null || !sLastConfig.equals(cfg)) {
                sLastConfig = cfg;
                sStatusTimeFormat = DateTimeFormatter.ofPattern("hh:mm a")
                        .withZone(ZoneId.systemDefault());
            }
        }
    }
}

/** 
 * @hide 
 */
public static final CharSequence timeString(long millis) {
    try {
        initFormatStrings();
        return sStatusTimeFormat.format(Instant.ofEpochMilli(millis));
    } catch (Exception e) {
        // Handle error appropriately
        return "Error formatting time";
    }
}
<<End of snippet n. 0>>