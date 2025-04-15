/*Fix value of dynamic resource IDs used when rendering.

Change-Id:Ic6c0fad53d06e48dcd8ab59c5aa0c2a530e36767*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 9627d27..7598da1 100644

//Synthetic comment -- @@ -51,9 +51,10 @@
*</ul>
*/
public class ProjectResources extends ResourceRepository {
    private final static int DYNAMIC_ID_SEED_START = 0; // this should not conflict with any
                                                        // project IDs that start at a much higher
                                                        // value

/** Map of (name, id) for resources of type {@link ResourceType#ID} coming from R.java */
private Map<ResourceType, Map<String, Integer>> mResourceValueMap;







