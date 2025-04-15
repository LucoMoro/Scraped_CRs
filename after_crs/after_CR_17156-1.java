/*Fix application forced close when http connection is redirected to https

It happens that when user is using some Wifi AP which required user to enter the
account/password before they are able to access the internet or the authentication
is expired. When this happened, the Picasa http connection will be redirected a
unregistered https connection which will throws an IllegalStateException and make
application crash. Add catching the IllegalStateException when accessing GDataClient
to avoid this issue

Change-Id:Idc6a5629b6b52fd46856ffa8d92e3fb8cc8f2006*/




//Synthetic comment -- diff --git a/src/com/cooliris/picasa/GDataClient.java b/src/com/cooliris/picasa/GDataClient.java
//Synthetic comment -- index a2e2be2..5a95a31 100644

//Synthetic comment -- @@ -124,55 +124,62 @@
}

private void callMethod(HttpUriRequest request, Operation operation) throws IOException {
try {

            // Specify GData protocol version 2.0.
            request.addHeader("GData-Version", "2");

            // Indicate support for gzip-compressed responses.
            request.addHeader("Accept-Encoding", "gzip");

            // Specify authorization token if provided.
            String authToken = mAuthToken;
            if (!TextUtils.isEmpty(authToken)) {
                request.addHeader("Authorization", "GoogleLogin auth=" + authToken);
            }

            // Specify the ETag of a prior response, if available.
            String etag = operation.inOutEtag;
            if (etag != null) {
                request.addHeader("If-None-Match", etag);
            }

            // Execute the HTTP request.
            HttpResponse httpResponse = null;
            try {
                httpResponse = mHttpClient.execute(request);
            } catch (IOException e) {
                Log.w(TAG, "Request failed: " + request.getURI());
                throw e;
            }

            // Get the status code and response body.
            int status = httpResponse.getStatusLine().getStatusCode();
            InputStream stream = null;
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                // Wrap the entity input stream in a GZIP decoder if necessary.
                stream = entity.getContent();
                if (stream != null) {
                    Header header = entity.getContentEncoding();
                    if (header != null) {
                        if (header.getValue().contains("gzip")) {
                            stream = new GZIPInputStream(stream);
                        }
}
}
}

            // Return the stream if successful.
            Header etagHeader = httpResponse.getFirstHeader("ETag");
            operation.outStatus = status;
            operation.inOutEtag = etagHeader != null ? etagHeader.getValue() : null;
            operation.outBody = stream;

        } catch (java.lang.IllegalStateException e) {
            Log.e(TAG, "Unhandled IllegalStateException e: "+ e);
            throw new IOException("Unhandled IllegalStateException");
        }
}

private ByteArrayEntity getCompressedEntity(byte[] data) throws IOException {







