/*Fix Generic[Stream|Generic]Cipher to Generic[Stream|Block]Cipher in comments

Change-Id:Iee2a86c764e5bd19135b371b100865fab2690965*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ConnectionState.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ConnectionState.java
//Synthetic comment -- index 069d5db..eed6dee 100644

//Synthetic comment -- @@ -61,7 +61,7 @@

/**
* Returns the minimal possible size of the
     * Generic[Stream|Generic]Cipher structure under this
* connection state.
*/
protected int getMinFragmentSize() {
//Synthetic comment -- @@ -70,7 +70,7 @@
}

/**
     * Returns the size of the Generic[Stream|Generic]Cipher structure
* corresponding to the content data of specified size.
*/
protected int getFragmentSize(int content_size) {
//Synthetic comment -- @@ -79,7 +79,7 @@

/**
* Returns the minimal upper bound of the content size enclosed
     * into the Generic[Stream|Generic]Cipher structure of specified size.
* For stream ciphers the returned value will be exact value.
*/
protected int getContentSize(int generic_cipher_size) {







