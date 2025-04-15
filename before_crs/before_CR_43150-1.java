/*37546: Graphical layout in Eclipse does not render unicode characters

Change-Id:I1fd07245e68b39af16a38bcec8a711867e87e5c6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 40d5e6f..cf79c90 100644

//Synthetic comment -- @@ -1360,4 +1360,47 @@
return AdtPlugin.getDisplay() != null
&& AdtPlugin.getDisplay().getThread() == Thread.currentThread();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 0eee47a..c30574b 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.SdkConstants;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;

import org.kxml2.io.KXmlParser;
//Synthetic comment -- @@ -118,6 +119,11 @@
return VALUE_FILL_PARENT;
}

return value;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 53f1e6b..de09d00 100644

//Synthetic comment -- @@ -29,6 +29,7 @@

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.FragmentMenu;
//Synthetic comment -- @@ -393,6 +394,11 @@
return VALUE_FILL_PARENT;
}

return value;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
//Synthetic comment -- index e0ebdbc..0d4e02e 100644

//Synthetic comment -- @@ -174,4 +174,17 @@
Locale.setDefault(originalDefaultLocale);
}
}
}







