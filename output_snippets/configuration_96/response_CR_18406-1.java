//<Beginning of snippet n. 0>


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public String getShortDescription() {

        String domain = null;

        if (mUrl != null && !mUrl.isEmpty()) {
            try {
                URL url = new URL(mUrl);
                domain = url.getHost();
            } catch (MalformedURLException e) {
                // Handle malformed URL exception
                domain = null;
            }
        }

        if (mUiName == null) {
            mUiName = "";
        }

        if (domain != null && !mUiName.contains(domain)) {
            mUiName += " " + domain;
        }

        return mUiName.length() > 0 ? mUiName : mUrl;
}

//<End of snippet n. 0>