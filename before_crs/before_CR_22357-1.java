/*Throw SyncException if local file is not found instead of FileNotFoundException.

Bug 16159

Change-Id:I13903487786b61398553a69ad09220defa78d588*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncException.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncException.java
//Synthetic comment -- index 1e9b692..5f84f64 100644

//Synthetic comment -- @@ -46,6 +46,8 @@
FILE_READ_ERROR("Reading local file failed!"),
/** attempting to push a directory. */
LOCAL_IS_DIRECTORY("Local path is a directory."),
/** when the target path of a multi file push is a file. */
REMOTE_IS_FILE("Remote path is a file."),
/** receiving too much data from the remove device at once */








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java
//Synthetic comment -- index f112a12..03a68aa 100644

//Synthetic comment -- @@ -23,7 +23,6 @@

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
//Synthetic comment -- @@ -231,8 +230,6 @@
*      if the unique entry is a folder, this should be a folder.
* @param monitor The progress monitor. Cannot be null.
* @throws SyncException
     * @throws FileNotFoundException if the file exists but is a directory, does not exist but
     *            cannot be created, or cannot be opened for any other reason.
* @throws IOException
* @throws TimeoutException
*
//Synthetic comment -- @@ -240,7 +237,7 @@
* @see #getNullProgressMonitor()
*/
public void pull(FileEntry[] entries, String localPath, ISyncProgressMonitor monitor)
            throws SyncException, FileNotFoundException, IOException, TimeoutException {

// first we check the destination is a directory and exists
File f = new File(localPath);
//Synthetic comment -- @@ -271,16 +268,15 @@
* @param localFilename The local destination.
* @param monitor The progress monitor. Cannot be null.
*
     * @throws SyncException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws TimeoutException
*
* @see FileListingService.FileEntry
* @see #getNullProgressMonitor()
*/
public void pullFile(FileEntry remote, String localFilename, ISyncProgressMonitor monitor)
            throws FileNotFoundException, IOException, SyncException, TimeoutException {
int total = remote.getSizeValue();
monitor.start(total);

//Synthetic comment -- @@ -326,14 +322,12 @@
* @param local An array of loca files to push
* @param remote the remote {@link FileEntry} representing a directory.
* @param monitor The progress monitor. Cannot be null.
     * @throws SyncException
     * @throws FileNotFoundException if the file exists but is a directory, does not exist but
     *            cannot be created, or cannot be opened for any other reason.
     * @throws IOException
     * @throws TimeoutException
*/
public void push(String[] local, FileEntry remote, ISyncProgressMonitor monitor)
            throws SyncException, FileNotFoundException, IOException, TimeoutException {
if (remote.isDirectory() == false) {
throw new SyncException(SyncError.REMOTE_IS_FILE);
}
//Synthetic comment -- @@ -361,18 +355,15 @@
* @param remote The remote filepath.
* @param monitor The progress monitor. Cannot be null.
*
     * @throws SyncException
     * @throws FileNotFoundException if the file exists but is a directory, does not exist but
     *            cannot be created, or cannot be opened for any other reason.
     * @throws IOException
     * @throws TimeoutException
     *
*/
public void pushFile(String local, String remote, ISyncProgressMonitor monitor)
            throws SyncException, FileNotFoundException, IOException, TimeoutException {
File f = new File(local);
if (f.exists() == false) {
            throw new FileNotFoundException();
}

if (f.isDirectory()) {
//Synthetic comment -- @@ -439,16 +430,13 @@
* @param fileListingService a FileListingService object to browse through remote directories.
* @param monitor the progress monitor. Must be started already.
*
     * @throws FileNotFoundException if the file exists but is a directory, does not exist but
     *            cannot be created, or cannot be opened for any other reason.
     * @throws IOException
     * @throws SyncException
     * @throws TimeoutException
*/
private void doPull(FileEntry[] entries, String localPath,
FileListingService fileListingService,
            ISyncProgressMonitor monitor) throws SyncException, FileNotFoundException, IOException,
            TimeoutException {

for (FileEntry e : entries) {
// check if we're cancelled
//Synthetic comment -- @@ -484,15 +472,12 @@
* @param remotePath the remote file (length max is 1024)
* @param localPath the local destination
* @param monitor the monitor. The monitor must be started already.
     * @throws FileNotFoundException if the file exists but is a directory, does not exist but
     *            cannot be created, or cannot be opened for any other reason.
     * @throws IOException
     * @throws SyncException
     * @throws TimeoutException
*/
private void doPullFile(String remotePath, String localPath,
            ISyncProgressMonitor monitor) throws FileNotFoundException, IOException, SyncException,
            TimeoutException {
byte[] msg = null;
byte[] pullResult = new byte[8];

//Synthetic comment -- @@ -581,14 +566,12 @@
* @param remotePath
* @param monitor
*
     * @throws SyncException
     * @throws FileNotFoundException if the file exists but is a directory, does not exist but
     *            cannot be created, or cannot be opened for any other reason.
     * @throws IOException
     * @throws TimeoutException
*/
private void doPush(File[] fileArray, String remotePath, ISyncProgressMonitor monitor)
            throws SyncException, FileNotFoundException, IOException, TimeoutException {
for (File f : fileArray) {
// check if we're canceled
if (monitor.isCanceled() == true) {
//Synthetic comment -- @@ -618,15 +601,12 @@
* @param remotePath the remote file (length max is 1024)
* @param monitor the monitor. The monitor must be started already.
*
     * @throws SyncException
     * @throws FileNotFoundException if the file exists but is a directory, does not exist but
     *            cannot be created, or cannot be opened for any other reason.
     * @throws IOException
     * @throws TimeoutException
*/
private void doPushFile(String localPath, String remotePath,
            ISyncProgressMonitor monitor) throws SyncException, FileNotFoundException, IOException,
            TimeoutException {
FileInputStream fis = null;
byte[] msg;

//Synthetic comment -- @@ -712,7 +692,7 @@
* @param result the current adb result. Must contain both FAIL and the length of the message.
* @param timeOut
* @return
     * @throws TimeoutException
* @throws IOException
*/
private String readErrorMessage(byte[] result, final int timeOut) throws TimeoutException,
//Synthetic comment -- @@ -739,7 +719,7 @@
* @return an Integer containing the mode if all went well or null
*      otherwise
* @throws IOException
     * @throws TimeoutException
*/
private Integer readMode(String path) throws TimeoutException, IOException {
// create the stat request message.







