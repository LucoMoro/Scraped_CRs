```
<<Beginning of snippet n. 0>>
HttpConnectionParams.setStaleCheckingEnabled(params, false);

// Default connection and socket timeout of 60 seconds.  Tweak to taste.
HttpConnectionParams.setConnectionTimeout(params, 60 * 1000);
HttpConnectionParams.setSoTimeout(params, 60 * 1000);
HttpConnectionParams.setSocketBufferSize(params, 8192);

// Don't handle redirects -- return them to the caller.  Our code
<<End of snippet n. 0>>