//<Beginning of snippet n. 0>


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public String getShortDescription() {
    if (mUrl == null || mUrl.isEmpty()) {
        return mUiName;
    }

    String domain = "";
    try {
        URL url = new URL(mUrl);
        domain = url.getHost();
    } catch (MalformedURLException e) {
        return mUiName;
    }

    if (mUiName != null && mUiName.length() > 0 && !mUiName.contains(domain)) {
        mUiName += " " + domain;
    }

    return mUiName;
}

//<End of snippet n. 0>