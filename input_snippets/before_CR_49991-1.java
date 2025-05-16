
//<Beginning of snippet n. 0>


} else {
to = decode(mailToString.substring(length, index));
}
            addAddresses(mToView, to.split(" ,"));
} catch (UnsupportedEncodingException e) {
Log.e(Logging.LOG_TAG, e.getMessage() + " while decoding '" + mailToString + "'");
}

//<End of snippet n. 0>








