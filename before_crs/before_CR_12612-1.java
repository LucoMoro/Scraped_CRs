/*Move the org.kxml2.wap.Wbxml class into the EAS code, the only user.

(I notice that you already have, for example, an END constant, but with
a different value. Bug?)

Bug: 2249953*/
//Synthetic comment -- diff --git a/src/com/android/exchange/adapter/Parser.java b/src/com/android/exchange/adapter/Parser.java
//Synthetic comment -- index ffe8039..ca99a77 100644

//Synthetic comment -- @@ -21,8 +21,6 @@
import com.android.exchange.EasException;
import com.android.exchange.utility.FileLogger;

import org.kxml2.wap.Wbxml;

import android.content.Context;
import android.util.Log;

//Synthetic comment -- @@ -499,4 +497,4 @@
outputStream.close();
return res;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/src/com/android/exchange/adapter/Serializer.java b/src/com/android/exchange/adapter/Serializer.java
//Synthetic comment -- index 32909bf..f314772 100644

//Synthetic comment -- @@ -26,8 +26,6 @@
import com.android.exchange.Eas;
import com.android.exchange.utility.FileLogger;

import org.kxml2.wap.Wbxml;

import android.util.Log;

import java.io.ByteArrayOutputStream;
//Synthetic comment -- @@ -187,4 +185,4 @@
out.write(data);
out.write(0);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/src/com/android/exchange/adapter/Wbxml.java b/src/com/android/exchange/adapter/Wbxml.java
new file mode 100644
//Synthetic comment -- index 0000000..89e6287

//Synthetic comment -- @@ -0,0 +1,49 @@







