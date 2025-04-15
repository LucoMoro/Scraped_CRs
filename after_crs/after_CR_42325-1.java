/*MediaFormat: Explain that KEY_COLOR_FORMAT is used with decoders as well

If reading the decoded pixel data (instead of letting the system
render it), this is critical for interpreting the data correctly.

Change-Id:I9bfd295a95dd3e7d1fa3189886fd243b07bb82cc*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaFormat.java b/media/java/android/media/MediaFormat.java
//Synthetic comment -- index 4414191..1723ed6 100644

//Synthetic comment -- @@ -40,7 +40,7 @@
* <tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
* <tr><td>{@link #KEY_WIDTH}</td><td>Integer</td><td></td></tr>
* <tr><td>{@link #KEY_HEIGHT}</td><td>Integer</td><td></td></tr>
 * <tr><td>{@link #KEY_COLOR_FORMAT}</td><td>Integer</td><td>set by the user for encoders, readable in the output format of decoders</b></td></tr>
* <tr><td>{@link #KEY_FRAME_RATE}</td><td>Integer or Float</td><td><b>encoder-only</b></td></tr>
* <tr><td>{@link #KEY_I_FRAME_INTERVAL}</td><td>Integer</td><td><b>encoder-only</b></td></tr>
* </table>







