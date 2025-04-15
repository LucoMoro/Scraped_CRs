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
import java.util.Iterator;

/**
* <p>
//Synthetic comment -- @@ -4688,6 +4689,10 @@
mParent.invalidateChild(this, r);
}
}
	
	// Notify any invalidationObservers that this view has been invalidated	
	dispatchViewInvalidation();

}

/**
//Synthetic comment -- @@ -4718,6 +4723,10 @@
p.invalidateChild(this, tmpr);
}
}
    
	// Notify any invalidationObservers that this view has been invalidated	
	dispatchViewInvalidation();
	    
}

/**
//Synthetic comment -- @@ -4742,6 +4751,10 @@
p.invalidateChild(this, r);
}
}
	
	// Notify any invalidationObservers that this view has been invalidated	
	dispatchViewInvalidation();

}

/**
//Synthetic comment -- @@ -4919,6 +4932,10 @@
msg.obj = this;
mAttachInfo.mHandler.sendMessageDelayed(msg, delayMilliseconds);
}
    
	// Notify any invalidationObservers that this view will be invalidated
	postViewInvalidation(delayMilliseconds);

}

/**
//Synthetic comment -- @@ -4950,6 +4967,10 @@
msg.obj = info;
mAttachInfo.mHandler.sendMessageDelayed(msg, delayMilliseconds);
}
	
	// Notify any invalidationObservers that this view will be invalidated
	postViewInvalidation(delayMilliseconds);

}

/**
//Synthetic comment -- @@ -8748,4 +8769,95 @@
}
}
}
   
      
    /**
     * <p>mViewInvalidationObservers holds a list of observers wanting to be
     * notified when this view is invalidated.</p>
     *
     */
    private ArrayList<ViewInvalidationObserver> mInvalidationObservers = null;
   
    /**
     * <p>Notify any invalidationObservers that the view has been invalidated.
     *
     */
    void dispatchViewInvalidation() {
	
	if (mInvalidationObservers != null){

	    Iterator<ViewInvalidationObserver> it = mInvalidationObservers.iterator();
	    while (it.hasNext ()) {
	 	ViewInvalidationObserver observer = it.next();
		observer.ViewInvalidated(this);		
	    }

	}

    }

    /**
     * <p>Notify any invalidationObservers that the view will be invalidated in delay seconds.
     *    
     * @param delay when to notify observers
     *
     */
    void postViewInvalidation(long delay) {
	
	if (mInvalidationObservers != null){

	    Runnable r = new Runnable() { 
	    	public void run() {
		    dispatchViewInvalidation();
	        };
	    };

	    getHandler().postDelayed(r, delay);

	}

    }

    /**
     * <p>Interface that a invalidationobserver needs to implement.
     *
     */
    public interface ViewInvalidationObserver {

	public void ViewInvalidated(View view);

    }

    /**
     * <p>Add a invalidationObserver to this view.
     *    
     */
    public void addInvalidationObserver(ViewInvalidationObserver observer) {

	if (mInvalidationObservers == null){
	    mInvalidationObservers = new ArrayList<ViewInvalidationObserver>();
	}
	mInvalidationObservers.add(observer);

    }
    
    /**
     * <p>Remove a previousle added invalidationObserver from this view.
     *    
     */
    public void removeInvalidationObserver(ViewInvalidationObserver observer) {

	int i;

	if (mInvalidationObservers == null){
	    return;
	}

	i = mInvalidationObservers.lastIndexOf(observer);
	if (i != -1){
	    mInvalidationObservers.remove(i);
	}

    }

}








//Synthetic comment -- diff --git a/core/java/android/view/ViewGroup.java b/core/java/android/view/ViewGroup.java
//Synthetic comment -- index f7b7f02..3cd3457 100644

//Synthetic comment -- @@ -2391,6 +2391,11 @@
parent = parent.invalidateChildInParent(location, dirty);
} while (parent != null);
}

	// A child has been invalidated => this ViewGroup is also invalidated => Notify any invalidationObservers that this ViewGroup
	// has been invalidated.
	dispatchViewInvalidation();

}

/**







