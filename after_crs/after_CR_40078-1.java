/*Use float comparison for setTargetHeapUtilization constants.

(cherry-pick of f655188036ceab8968a510ae67455a27917546cf.)

Change-Id:I587ad497ee9ac38f4052cdfdd861e8b24dd80712*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/VMRuntime.java b/dalvik/src/main/java/dalvik/system/VMRuntime.java
//Synthetic comment -- index c37290d..71098be 100644

//Synthetic comment -- @@ -91,7 +91,7 @@
* @throws IllegalArgumentException if newTarget is &lt;= 0.0 or &gt;= 1.0
*/
public float setTargetHeapUtilization(float newTarget) {
        if (newTarget <= 0.0f || newTarget >= 1.0f) {
throw new IllegalArgumentException(newTarget +
" out of range (0,1)");
}







