
//<Beginning of snippet n. 0>


// and it's not worth it to pay the penalty of checking every time.
HttpConnectionParams.setStaleCheckingEnabled(params, false);

        // Default connection and socket timeout of 20 seconds.  Tweak to taste.
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
HttpConnectionParams.setSocketBufferSize(params, 8192);

// Don't handle redirects -- return them to the caller.  Our code

//<End of snippet n. 0>








