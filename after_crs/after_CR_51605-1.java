/*Revert "Add in additional http request types"

This seems to have caused a regression with account sign-up.

This reverts commit bece5836e16897df162e4f4db302b6eb14ac71b2

Change-Id:I019f98b984e496558e1830fec4d810fd589652db*/




//Synthetic comment -- diff --git a/src/com/android/volley/Request.java b/src/com/android/volley/Request.java
//Synthetic comment -- index 574982c..f37727d 100644

//Synthetic comment -- @@ -48,19 +48,12 @@
int POST = 1;
int PUT = 2;
int DELETE = 3;
}

/** An event log tracing the lifetime of this request; for debugging. */
private final MarkerLog mEventLog = MarkerLog.ENABLED ? new MarkerLog() : null;

    /** Request method of this request.  Currently supports GET, POST, PUT, and DELETE. */
private final int mMethod;

/** URL of this request. */








//Synthetic comment -- diff --git a/src/com/android/volley/toolbox/HttpClientStack.java b/src/com/android/volley/toolbox/HttpClientStack.java
//Synthetic comment -- index 377110e..f8dfdab 100644

//Synthetic comment -- @@ -27,11 +27,8 @@
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
//Synthetic comment -- @@ -39,7 +36,6 @@
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -126,18 +122,6 @@
setEntityIfNonEmptyBody(putRequest, request);
return putRequest;
}
default:
throw new IllegalStateException("Unknown request method.");
}
//Synthetic comment -- @@ -160,35 +144,4 @@
protected void onPrepareRequest(HttpUriRequest request) throws IOException {
// Nothing.
}
}








//Synthetic comment -- diff --git a/src/com/android/volley/toolbox/HurlStack.java b/src/com/android/volley/toolbox/HurlStack.java
//Synthetic comment -- index eab849a..d669166 100644

//Synthetic comment -- @@ -187,19 +187,6 @@
addBodyIfExists(connection, request);
connection.setRequestMethod("PUT");
break;
default:
throw new IllegalStateException("Unknown method type.");
}








//Synthetic comment -- diff --git a/tests/src/com/android/volley/mock/TestRequest.java b/tests/src/com/android/volley/mock/TestRequest.java
//Synthetic comment -- index dfc4dc1..ff62009 100644

//Synthetic comment -- @@ -133,47 +133,4 @@
super(Method.DELETE, TEST_URL, null);
}
}
}








//Synthetic comment -- diff --git a/tests/src/com/android/volley/toolbox/HttpClientStackTest.java b/tests/src/com/android/volley/toolbox/HttpClientStackTest.java
//Synthetic comment -- index c25821e..ac296aa 100644

//Synthetic comment -- @@ -18,15 +18,11 @@

import com.android.volley.Request.Method;
import com.android.volley.mock.TestRequest;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import android.test.AndroidTestCase;
//Synthetic comment -- @@ -104,44 +100,4 @@
HttpUriRequest httpRequest = HttpClientStack.createHttpRequest(request, null);
assertTrue(httpRequest instanceof HttpDelete);
}
}








//Synthetic comment -- diff --git a/tests/src/com/android/volley/toolbox/HurlStackTest.java b/tests/src/com/android/volley/toolbox/HurlStackTest.java
//Synthetic comment -- index f35b9e5..477a341 100644

//Synthetic comment -- @@ -106,49 +106,4 @@
assertEquals("DELETE", mMockConnection.getRequestMethod());
assertFalse(mMockConnection.getDoOutput());
}
}







