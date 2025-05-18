//<Beginning of snippet n. 0>



/**
* Returns the minimal possible size of the
     * GenericStreamCipher structure under this
* connection state.
*/
protected int getMinFragmentSize() {
}

/**
     * Returns the size of the GenericBlockCipher structure
* corresponding to the content data of specified size.
*/
protected int getFragmentSize(int content_size) {
}

/**
* Returns the minimal upper bound of the content size enclosed
     * into the GenericBlockCipher structure of specified size.
* For stream ciphers the returned value will be the exact value.
*/
protected int getContentSize(int generic_cipher_size) {
}

//<End of snippet n. 0>