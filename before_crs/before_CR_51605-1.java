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
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
}

/** An event log tracing the lifetime of this request; for debugging. */
private final MarkerLog mEventLog = MarkerLog.ENABLED ? new MarkerLog() : null;

    /**
     * Request method of this request.  Currently supports GET, POST, PUT, DELETE, HEAD, OPTIONS,
     * TRACE, and PATCH.
     */
private final int mMethod;

/** URL of this request. */








//Synthetic comment -- diff --git a/src/com/android/volley/toolbox/HttpClientStack.java b/src/com/android/volley/toolbox/HttpClientStack.java
//Synthetic comment -- index 377110e..f8dfdab 100644

//Synthetic comment -- @@ -27,11 +27,8 @@
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
//Synthetic comment -- @@ -39,7 +36,6 @@
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -126,18 +122,6 @@
setEntityIfNonEmptyBody(putRequest, request);
return putRequest;
}
            case Method.HEAD:
                return new HttpHead(request.getUrl());
            case Method.OPTIONS:
                return new HttpOptions(request.getUrl());
            case Method.TRACE:
                return new HttpTrace(request.getUrl());
            case Method.PATCH: {
                HttpPatch patchRequest = new HttpPatch(request.getUrl());
                patchRequest.addHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
                setEntityIfNonEmptyBody(patchRequest, request);
                return patchRequest;
            }
default:
throw new IllegalStateException("Unknown request method.");
}
//Synthetic comment -- @@ -160,35 +144,4 @@
protected void onPrepareRequest(HttpUriRequest request) throws IOException {
// Nothing.
}

    /**
     * The HttpPatch class does not exist in the Android framework, so this has been defined here.
     */
    public static final class HttpPatch extends HttpEntityEnclosingRequestBase {

        public final static String METHOD_NAME = "PATCH";

        public HttpPatch() {
            super();
        }

        public HttpPatch(final URI uri) {
            super();
            setURI(uri);
        }

        /**
         * @throws IllegalArgumentException if the uri is invalid.
         */
        public HttpPatch(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }

    }
}








//Synthetic comment -- diff --git a/src/com/android/volley/toolbox/HurlStack.java b/src/com/android/volley/toolbox/HurlStack.java
//Synthetic comment -- index eab849a..d669166 100644

//Synthetic comment -- @@ -187,19 +187,6 @@
addBodyIfExists(connection, request);
connection.setRequestMethod("PUT");
break;
            case Method.HEAD:
                connection.setRequestMethod("HEAD");
                break;
            case Method.OPTIONS:
                connection.setRequestMethod("OPTIONS");
                break;
            case Method.TRACE:
                connection.setRequestMethod("TRACE");
                break;
            case Method.PATCH:
                addBodyIfExists(connection, request);
                connection.setRequestMethod("PATCH");
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

    /** Test example of a HEAD request in the new style. */
    public static class Head extends Base {
        public Head() {
            super(Method.HEAD, TEST_URL, null);
        }
    }

    /** Test example of a OPTIONS request in the new style. */
    public static class Options extends Base {
        public Options() {
            super(Method.OPTIONS, TEST_URL, null);
        }
    }

    /** Test example of a TRACE request in the new style. */
    public static class Trace extends Base {
        public Trace() {
            super(Method.TRACE, TEST_URL, null);
        }
    }

    /** Test example of a PATCH request in the new style. */
    public static class Patch extends Base {
        public Patch() {
            super(Method.PATCH, TEST_URL, null);
        }
    }

    /** Test example of a PATCH request in the new style with a body. */
    public static class PatchWithBody extends Patch {
        private Map<String, String> mParams = new HashMap<String, String>();

        public PatchWithBody() {
            mParams = new HashMap<String, String>();
            mParams.put("testKey", "testValue");
        }

        @Override
        public Map<String, String> getParams() {
            return mParams;
        }
    }
}








//Synthetic comment -- diff --git a/tests/src/com/android/volley/toolbox/HttpClientStackTest.java b/tests/src/com/android/volley/toolbox/HttpClientStackTest.java
//Synthetic comment -- index c25821e..ac296aa 100644

//Synthetic comment -- @@ -18,15 +18,11 @@

import com.android.volley.Request.Method;
import com.android.volley.mock.TestRequest;
import com.android.volley.toolbox.HttpClientStack.HttpPatch;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;

import android.test.AndroidTestCase;
//Synthetic comment -- @@ -104,44 +100,4 @@
HttpUriRequest httpRequest = HttpClientStack.createHttpRequest(request, null);
assertTrue(httpRequest instanceof HttpDelete);
}

    public void testCreateHeadRequest() throws Exception {
        TestRequest.Head request = new TestRequest.Head();
        assertEquals(request.getMethod(), Method.HEAD);

        HttpUriRequest httpRequest = HttpClientStack.createHttpRequest(request, null);
        assertTrue(httpRequest instanceof HttpHead);
    }

    public void testCreateOptionsRequest() throws Exception {
        TestRequest.Options request = new TestRequest.Options();
        assertEquals(request.getMethod(), Method.OPTIONS);

        HttpUriRequest httpRequest = HttpClientStack.createHttpRequest(request, null);
        assertTrue(httpRequest instanceof HttpOptions);
    }

    public void testCreateTraceRequest() throws Exception {
        TestRequest.Trace request = new TestRequest.Trace();
        assertEquals(request.getMethod(), Method.TRACE);

        HttpUriRequest httpRequest = HttpClientStack.createHttpRequest(request, null);
        assertTrue(httpRequest instanceof HttpTrace);
    }

    public void testCreatePatchRequest() throws Exception {
        TestRequest.Patch request = new TestRequest.Patch();
        assertEquals(request.getMethod(), Method.PATCH);

        HttpUriRequest httpRequest = HttpClientStack.createHttpRequest(request, null);
        assertTrue(httpRequest instanceof HttpPatch);
    }

    public void testCreatePatchRequestWithBody() throws Exception {
        TestRequest.PatchWithBody request = new TestRequest.PatchWithBody();
        assertEquals(request.getMethod(), Method.PATCH);

        HttpUriRequest httpRequest = HttpClientStack.createHttpRequest(request, null);
        assertTrue(httpRequest instanceof HttpPatch);
    }
}








//Synthetic comment -- diff --git a/tests/src/com/android/volley/toolbox/HurlStackTest.java b/tests/src/com/android/volley/toolbox/HurlStackTest.java
//Synthetic comment -- index f35b9e5..477a341 100644

//Synthetic comment -- @@ -106,49 +106,4 @@
assertEquals("DELETE", mMockConnection.getRequestMethod());
assertFalse(mMockConnection.getDoOutput());
}

    public void testConnectionForHeadRequest() throws Exception {
        TestRequest.Head request = new TestRequest.Head();
        assertEquals(request.getMethod(), Method.HEAD);

        HurlStack.setConnectionParametersForRequest(mMockConnection, request);
        assertEquals("HEAD", mMockConnection.getRequestMethod());
        assertFalse(mMockConnection.getDoOutput());
    }

    public void testConnectionForOptionsRequest() throws Exception {
        TestRequest.Options request = new TestRequest.Options();
        assertEquals(request.getMethod(), Method.OPTIONS);

        HurlStack.setConnectionParametersForRequest(mMockConnection, request);
        assertEquals("OPTIONS", mMockConnection.getRequestMethod());
        assertFalse(mMockConnection.getDoOutput());
    }

    public void testConnectionForTraceRequest() throws Exception {
        TestRequest.Trace request = new TestRequest.Trace();
        assertEquals(request.getMethod(), Method.TRACE);

        HurlStack.setConnectionParametersForRequest(mMockConnection, request);
        assertEquals("TRACE", mMockConnection.getRequestMethod());
        assertFalse(mMockConnection.getDoOutput());
    }

    public void testConnectionForPatchRequest() throws Exception {
        TestRequest.Patch request = new TestRequest.Patch();
        assertEquals(request.getMethod(), Method.PATCH);

        HurlStack.setConnectionParametersForRequest(mMockConnection, request);
        assertEquals("PATCH", mMockConnection.getRequestMethod());
        assertFalse(mMockConnection.getDoOutput());
    }

    public void testConnectionForPatchWithBodyRequest() throws Exception {
        TestRequest.PatchWithBody request = new TestRequest.PatchWithBody();
        assertEquals(request.getMethod(), Method.PATCH);

        HurlStack.setConnectionParametersForRequest(mMockConnection, request);
        assertEquals("PATCH", mMockConnection.getRequestMethod());
        assertTrue(mMockConnection.getDoOutput());
    }
}







