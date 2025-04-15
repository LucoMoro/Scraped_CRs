/*SDK Manager: display URL host in source tree.

Change-Id:I2fd27d931fe9011cc2894c7179e2e14327867b94*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 034854a..2a917a1 100755

//Synthetic comment -- @@ -34,6 +34,7 @@
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
//Synthetic comment -- @@ -195,10 +196,18 @@

public String getShortDescription() {

if (mUiName != null && mUiName.length() > 0) {

            String host = "malformed URL";

            try {
                URL u = new URL(mUrl);
                host = u.getHost();
            } catch (MalformedURLException e) {
            }

            return String.format("%1$s (%2$s)", mUiName, host);

}
return mUrl;
}







