/*SDK Manager: fix HTTP Auth cancel.

When the SDK Manager prompts the user for HTTP Auth
or NTLM auth, the API says the result should be null
if the user cancelled.
It wasn't, it was instead empty string fields and
the download would abort the a "failed: no content"
error in the log.
This fixes the UI to respect the convention.

It also makes sure that if the HttpClient call
fails due to the user cancelling, we don't try
to fallback on the java.UrlConnection method.

Change-Id:Ibf1807f4becd40dbced17f851b3fdc5b394788ea*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index 1f44e5a..dd0761c 100644

//Synthetic comment -- @@ -184,6 +184,10 @@
} catch (IOException e) {
throw e;

        } catch (CanceledByUserException e) {
            // HTTP Basic Auth or NTLM login was canceled by user.
            throw e;

} catch (Exception e) {
// If the protocol is not supported by HttpClient (e.g. file:///),
// revert to the standard java.net.Url.open.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index 5bac5f3..612f74e 100755

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.FileOp;
//Synthetic comment -- @@ -405,6 +406,10 @@

return true;

        } catch (CanceledByUserException e) {
            // HTTP Basic Auth or NTLM login was canceled by user.
            // Don't output an error in the log.

} catch (FileNotFoundException e) {
// The FNF message is just the URL. Make it a bit more useful.
monitor.logError("File not found: %1$s", e.getMessage());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressTaskDialog.java
//Synthetic comment -- index 856f2ec..50f1e57 100755

//Synthetic comment -- @@ -437,14 +437,11 @@
GetUserCredentialsTask task = new GetUserCredentialsTask(mDialogShell, title, message);
display.syncExec(task);

        return task.getUserCredentials();
}

private static class GetUserCredentialsTask implements Runnable {
        private UserCredentials mResult = null;

private Shell mShell;
private String mTitle;
//Synthetic comment -- @@ -462,12 +459,17 @@
mTitle, mMessage);
int dlgResult= authenticationDialog.open();
if(dlgResult == GridDialog.OK) {
                mResult = new UserCredentials(
                        authenticationDialog.getLogin(),
                        authenticationDialog.getPassword(),
                        authenticationDialog.getWorkstation(),
                        authenticationDialog.getDomain());
}
}

        public UserCredentials getUserCredentials() {
            return mResult;
        }
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index 4691ec6..8987351 100755

//Synthetic comment -- @@ -33,6 +33,8 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import java.util.concurrent.atomic.AtomicReference;


/**
* Implements a "view" that uses an existing progress bar, status button and
//Synthetic comment -- @@ -347,7 +349,8 @@
@Override
public UserCredentials
displayLoginCredentialsPrompt(final String title, final String message) {
        final AtomicReference<UserCredentials> result = new AtomicReference<UserCredentials>(null);

// open dialog and request login and password
syncExec(mProgressBar, new Runnable() {
@Override
//Synthetic comment -- @@ -358,18 +361,16 @@
message);
int dlgResult = authenticationDialog.open();
if (dlgResult == GridDialog.OK) {
                    result.set(new UserCredentials(
                        authenticationDialog.getLogin(),
                        authenticationDialog.getPassword(),
                        authenticationDialog.getWorkstation(),
                        authenticationDialog.getDomain()));
}
}
});

        return result.get();
}
}








