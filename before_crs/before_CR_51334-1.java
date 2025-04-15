/*Add in additional http request types

Specifically, these are HEAD, OPTIONS, TRACE, and PATCH.

Change-Id:Ie33fdc059ca7bb0658de7e270e2af56f54a00481*/
//Synthetic comment -- diff --git a/src/com/android/volley/Request.java b/src/com/android/volley/Request.java
//Synthetic comment -- index f37727d..574982c 100644

//Synthetic comment -- @@ -48,12 +48,19 @@
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
//Synthetic comment -- index f8dfdab..377110e 100644

//Synthetic comment -- @@ -27,8 +27,11 @@
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
//Synthetic comment -- @@ -36,6 +39,7 @@
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -122,6 +126,18 @@
setEntityIfNonEmptyBody(putRequest, request);
return putRequest;
}
default:
throw new IllegalStateException("Unknown request method.");
}
//Synthetic comment -- @@ -144,4 +160,35 @@
protected void onPrepareRequest(HttpUriRequest request) throws IOException {
// Nothing.
}
}








//Synthetic comment -- diff --git a/src/com/android/volley/toolbox/HurlStack.java b/src/com/android/volley/toolbox/HurlStack.java
//Synthetic comment -- index d669166..eab849a 100644

//Synthetic comment -- @@ -187,6 +187,19 @@
addBodyIfExists(connection, request);
connection.setRequestMethod("PUT");
break;
default:
throw new IllegalStateException("Unknown method type.");
}








//Synthetic comment -- diff --git a/tests/src/com/android/volley/mock/TestRequest.java b/tests/src/com/android/volley/mock/TestRequest.java
//Synthetic comment -- index ff62009..dfc4dc1 100644

//Synthetic comment -- @@ -133,4 +133,47 @@
super(Method.DELETE, TEST_URL, null);
}
}
}








//Synthetic comment -- diff --git a/tests/src/com/android/volley/toolbox/HttpClientStackTest.java b/tests/src/com/android/volley/toolbox/HttpClientStackTest.java
//Synthetic comment -- index ac296aa..c25821e 100644

//Synthetic comment -- @@ -18,11 +18,15 @@

import com.android.volley.Request.Method;
import com.android.volley.mock.TestRequest;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import android.test.AndroidTestCase;
//Synthetic comment -- @@ -100,4 +104,44 @@
HttpUriRequest httpRequest = HttpClientStack.createHttpRequest(request, null);
assertTrue(httpRequest instanceof HttpDelete);
}
}








//Synthetic comment -- diff --git a/tests/src/com/android/volley/toolbox/HurlStackTest.java b/tests/src/com/android/volley/toolbox/HurlStackTest.java
//Synthetic comment -- index 477a341..f35b9e5 100644

//Synthetic comment -- @@ -106,4 +106,49 @@
assertEquals("DELETE", mMockConnection.getRequestMethod());
assertFalse(mMockConnection.getDoOutput());
}
}







