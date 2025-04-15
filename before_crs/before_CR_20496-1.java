/*Move ValueResourceParser into a better package.

Change-Id:I15ae7efd2d482bd5bc368983a1745d9026f964b2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java
//Synthetic comment -- index 247e371..fc5ec4f 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.rendering.ValueResourceParser;
import com.android.ide.common.rendering.ValueResourceParser.IValueResourceRepository;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/ValueResourceParser.java b/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java
similarity index 99%
rename from ide_common/src/com/android/ide/common/rendering/ValueResourceParser.java
rename to ide_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index 93303ba..2e4cf8e 100644

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.ide.common.rendering;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;







