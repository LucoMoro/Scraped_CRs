
//<Beginning of snippet n. 0>


* <p/>This provider uses a custom keystore to create and store a key with a known password.
*/
public class DebugKeyProvider {
    
public interface IKeyGenOutput {
public void out(String message);
public void err(String message);
}
    
private static final String PASSWORD_STRING = "android";
private static final char[] PASSWORD_CHAR = PASSWORD_STRING.toCharArray();
private static final String DEBUG_ALIAS = "AndroidDebugKey";
    
// Certificate CN value. This is a hard-coded value for the debug key.
// Android Market checks against this value in order to refuse applications signed with
// debug keys.
private static final String CERTIFICATE_DESC = "CN=Android Debug,O=Android,C=US";
    
private KeyStore.PrivateKeyEntry mEntry;
    
public static class KeytoolException extends Exception {
/** default serial uid */
private static final long serialVersionUID = 1L;
private String mJavaHome = null;
private String mCommandLine = null;
        
KeytoolException(String message) {
super(message);
}

KeytoolException(String message, String javaHome, String commandLine) {
super(message);
            
mJavaHome = javaHome;
mCommandLine = commandLine;
}
        
public String getJavaHome() {
return mJavaHome;
}
        
public String getCommandLine() {
return mCommandLine;
}
}
    
/**
* Creates a provider using a keystore at the given location.
* <p/>The keystore, and a new random android debug key are created if they do not yet exist.
* @param output an optional {@link IKeyGenOutput} object to get the stdout and stderr
* of the keytool process call.
* @throws KeytoolException If the creation of the debug key failed.
     * @throws AndroidLocationException 
*/
public DebugKeyProvider(String osKeyStorePath, String storeType, IKeyGenOutput output)
throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
UnrecoverableEntryException, IOException, KeytoolException, AndroidLocationException {
        
if (osKeyStorePath == null) {
osKeyStorePath = getDefaultKeyStoreOsPath();
}
        
if (loadKeyEntry(osKeyStorePath, storeType) == false) {
// create the store with the key
createNewStore(osKeyStorePath, storeType, output);

/**
* Returns the OS path to the default debug keystore.
     * 
* @return The OS path to the default debug keystore.
* @throws KeytoolException
* @throws AndroidLocationException
if (mEntry != null) {
return mEntry.getPrivateKey();
}
        
return null;
}


return null;
}
    
/**
* Loads the debug key from the keystore.
* @param osKeyStorePath the OS path to the keystore.
} catch (FileNotFoundException e) {
return false;
}
        
return true;
}

private void createNewStore(String osKeyStorePath, String storeType, IKeyGenOutput output)
throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
UnrecoverableEntryException, IOException, KeytoolException {
        
if (KeystoreHelper.createNewStore(osKeyStorePath, storeType, PASSWORD_STRING, DEBUG_ALIAS,
                PASSWORD_STRING, CERTIFICATE_DESC, 1 /* validity*/, output)) {
loadKeyEntry(osKeyStorePath, storeType);
}
}

//<End of snippet n. 0>








