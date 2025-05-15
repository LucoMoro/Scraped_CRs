
//<Beginning of snippet n. 0>


import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
/**
return sb.toString();
}

}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
public void testEndsWithIgnoreCase() {
wrapped);
}


}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.ui.GridDialog;
import com.android.utils.ILogger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
}

int dpi = Integer.parseInt(dpiStr);
        float size = Float.parseFloat(sizeStr);
/*
* We are trying to emulate the following device:
* resolution: 'mSize1'x'mSize2'
if (scale == 0.f) {
mScaleField.setText("default");  //$NON-NLS-1$
} else {
            mScaleField.setText(String.format("%.2f", scale));  //$NON-NLS-1$
}
}

if (d != null) {
double screenSize =
d.getDefaultHardware().getScreen().getDiagonalLength();
                    return String.format("%.1f", screenSize);
}
}
}

//<End of snippet n. 2>








