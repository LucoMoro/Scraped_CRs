/*Add XXHigh density (added in JB)

(cherry picked from commit 0ad91b091b3cafab2def31a049b2a65c3460c655)

Change-Id:I558633653d177b6f2cdba05c7c1671d1322f9fe2*/
//Synthetic comment -- diff --git a/common/src/com/android/resources/Density.java b/common/src/com/android/resources/Density.java
//Synthetic comment -- index 0584166..1f3fb52 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
* as well as other places needing to know the density values.
*/
public enum Density implements ResourceEnum {
XHIGH("xhdpi", "X-High Density", 320, 8), //$NON-NLS-1$
HIGH("hdpi", "High Density", 240, 4), //$NON-NLS-1$
TV("tvdpi", "TV Density", 213, 13), //$NON-NLS-1$







