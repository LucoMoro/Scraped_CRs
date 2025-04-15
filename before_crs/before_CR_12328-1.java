/*Allow POST with auth (issue 4326). Previously, no request with a body could be resubmitted, even if the response was 401 or 407.*/
//Synthetic comment -- diff --git a/libcore/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnection.java b/libcore/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnection.java
//Synthetic comment -- index 6f5a2be..0677213 100644

//Synthetic comment -- @@ -1551,9 +1551,7 @@
int redirect = 0;
while (true) {
// send the request and process the results
            if (!sendRequest()) {
                return;
            }
// proxy authorization failed ?
if (responseCode == HTTP_PROXY_AUTH) {
if (!usingProxy()) {
//Synthetic comment -- @@ -1606,6 +1604,7 @@
// continue to send request
continue;
}
/*
* See if there is a server redirect to the URL, but only handle 1
* level of URL redirection from the server to avoid being caught in







