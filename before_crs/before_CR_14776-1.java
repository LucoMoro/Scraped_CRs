/*Allow POST with auth (issue 4326).

Previously, no request with a body could be resubmitted, even if the response was 401 or 407.

Change-Id:I5e0cc1c861a6b23ed9a59218bf5e8cfa7db97133*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnection.java b/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnection.java
//Synthetic comment -- index 2dea92b..428a425 100644

//Synthetic comment -- @@ -1555,9 +1555,7 @@
int redirect = 0;
while (true) {
// send the request and process the results
            if (!sendRequest()) {
                return;
            }
// proxy authorization failed ?
if (responseCode == HTTP_PROXY_AUTH) {
if (!usingProxy()) {
//Synthetic comment -- @@ -1610,6 +1608,7 @@
// continue to send request
continue;
}
/*
* See if there is a server redirect to the URL, but only handle 1
* level of URL redirection from the server to avoid being caught in







