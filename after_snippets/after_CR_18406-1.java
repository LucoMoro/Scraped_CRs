
//<Beginning of snippet n. 0>


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public String getShortDescription() {

if (mUiName != null && mUiName.length() > 0) {

            String host = "malformed source URL";

            try {
                URL u = new URL(mUrl);
                host = u.getHost();
            } catch (MalformedURLException e) {
            }

            return String.format("%1$s (%2$s)", mUiName, host);

}
return mUrl;
}

//<End of snippet n. 0>








