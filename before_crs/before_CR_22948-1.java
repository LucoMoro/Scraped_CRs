/*Fix Gallery3d sends Authentication Tokens in Clear Text for picasa

Switching picasa api to use https instead of http in order to protect the user from attacks aiming to get his authentication token.

Change-Id:Ifc7d7aa324d4572a7cf4d23c629227e3d759de7eSigned-off-by: andrea <andrea@inqmobile.com>*/
//Synthetic comment -- diff --git a/src/com/cooliris/picasa/GDataClient.java b/src/com/cooliris/picasa/GDataClient.java
//Synthetic comment -- index a2e2be2..48fecbc 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//Synthetic comment -- @@ -69,7 +70,7 @@

// Register HTTP protocol.
SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

// Create the connection manager.
HTTP_CONNECTION_MANAGER = new ThreadSafeClientConnManager(params, schemeRegistry);








//Synthetic comment -- diff --git a/src/com/cooliris/picasa/PicasaApi.java b/src/com/cooliris/picasa/PicasaApi.java
//Synthetic comment -- index 90f3db1..5a109fb 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
public static final int RESULT_ERROR = 2;

private static final String TAG = "PicasaAPI";
    private static final String BASE_URL = "http://picasaweb.google.com/data/feed/api/";
private static final String BASE_QUERY_STRING;

static {







