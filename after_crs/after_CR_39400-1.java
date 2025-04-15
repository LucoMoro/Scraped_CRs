/*Settings: correct kernel version display

The regex assumes only one set of parenthesis for the toolchain eg (GCC)
the new 4.6 toolchain contains two sets eg (prerelease) (GCC) which breaks
the regex. This patch makes it less specific so it should work with all
toolchains with any kind of versioning scheme.
We also need to hide SMP so the regex doesnt break again, and
display "SMP PREEMPT" on the date line

Signed-off-by: Andrew Sutherland <dr3wsuth3rland@gmail.com>

Change-Id:I81d3ad1bb637672cb268d87990603bbeb81d4693*/




//Synthetic comment -- diff --git a/src/com/android/settings/DeviceInfoSettings.java b/src/com/android/settings/DeviceInfoSettings.java
//Synthetic comment -- index c25a466..ca1b8ce 100644

//Synthetic comment -- @@ -194,8 +194,9 @@
"\\w+\\s+" + /* ignore: version */
"([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
"\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
                "(?:\\(gcc.*\\)\\s+\\))?\\s+" + /* ignore: (gcc ..) */
"([^\\s]+)\\s+" + /* group 3: #26 */
                "(?:SMP\\s+)?" + /* ignore: SMP (optional) */
"(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
"(.+)"; /* group 4: date */








