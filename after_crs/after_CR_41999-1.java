/*Preserve fatal severity for Lint XML reports

The Lint CLI does not distinguish between errors and fatal errors in
the error output. However, changing the severity was happening when
the issue was reported rather in the output reporter itself, which
meant that it accidentally changed the severity in the XML report,
where all metadata should be preserved.

Change-Id:Ib3ee9a20a7b1d2413ed85e1952f5f3d1783a8826*/




//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index 20825cb..47d00d2 100644

//Synthetic comment -- @@ -1053,12 +1053,7 @@
return;
}

        if (severity == Severity.ERROR || severity == Severity.FATAL) {
mHasErrors = true;
mErrorCount++;
} else {








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/TextReporter.java b/lint/cli/src/com/android/tools/lint/TextReporter.java
//Synthetic comment -- index 7bce91f..4f2c8b4 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Position;
import com.android.tools.lint.detector.api.Severity;
import com.google.common.annotations.Beta;

import java.io.IOException;
//Synthetic comment -- @@ -75,7 +76,13 @@
}
}

                Severity severity = warning.severity;
                if (severity == Severity.FATAL) {
                    // Treat the fatal error as an error such that we don't display
                    // both "Fatal:" and "Error:" etc in the error output.
                    severity = Severity.ERROR;
                }
                output.append(severity.getDescription());
output.append(':');
output.append(' ');








