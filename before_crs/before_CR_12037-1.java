/*Add ViewInvalidationObserver to View class.

This makes it possible for a program to be notified when a view is invalidated
and needs to be redrawn. Can be used for example when drawing views to
off-screen buffers and using them on opengl surfaces. Without this feature
you have to rerender view off-screen every frame to be sure you dont miss any
updates.

(Have example of list on 3d surface up and running using this patch).*/
//Synthetic comment -- diff --git a/core/java/android/view/View.java b/core/java/android/view/View.java
//Synthetic comment -- index 7ed2712..659b279 100644

//Synthetic comment -- @@ -70,6 +70,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.WeakHashMap;

/**
* <p>
//Synthetic comment -- @@ -4688,6 +4689,10 @@
mParent.invalidateChild(this, r);
}
}
}

/**
//Synthetic comment -- @@ -4718,6 +4723,10 @@
p.invalidateChild(this, tmpr);
}
}
}

/**
//Synthetic comment -- @@ -4742,6 +4751,10 @@
p.invalidateChild(this, r);
}
}
}

/**
//Synthetic comment -- @@ -4919,6 +4932,10 @@
msg.obj = this;
mAttachInfo.mHandler.sendMessageDelayed(msg, delayMilliseconds);
}
}

/**
//Synthetic comment -- @@ -4950,6 +4967,10 @@
msg.obj = info;
mAttachInfo.mHandler.sendMessageDelayed(msg, delayMilliseconds);
}
}

/**
//Synthetic comment -- @@ -8748,4 +8769,95 @@
}
}
}
}








//Synthetic comment -- diff --git a/core/java/android/view/ViewGroup.java b/core/java/android/view/ViewGroup.java
//Synthetic comment -- index f7b7f02..3cd3457 100644

//Synthetic comment -- @@ -2391,6 +2391,11 @@
parent = parent.invalidateChildInParent(location, dirty);
} while (parent != null);
}
}

/**







