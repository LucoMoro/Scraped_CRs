//<Beginning of snippet n. 0>


// and it's not worth it to pay the penalty of checking every time.
HttpConnectionParams.setStaleCheckingEnabled(params, false);

// Updated connection and socket timeout to 60 seconds.
HttpConnectionParams.setConnectionTimeout(params, 60 * 1000);
HttpConnectionParams.setSoTimeout(params, 60 * 1000);
HttpConnectionParams.setSocketBufferSize(params, 8192);

// Don't handle redirects -- return them to the caller.  Our code

//<End of snippet n. 0>