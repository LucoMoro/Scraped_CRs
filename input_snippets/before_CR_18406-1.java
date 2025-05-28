
//<Beginning of snippet n. 0>


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public String getShortDescription() {

        // TODO extract domain from URL and add to UiName if not present.

if (mUiName != null && mUiName.length() > 0) {
            return mUiName;
}
return mUrl;
}

//<End of snippet n. 0>








