/*Add use of volume rocker to navigate to next/previous message while
in message view.

Change-Id:I90297bf03a79c842bd179cc9bf2e0590314b0699*/




//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageView.java b/src/com/android/email/activity/MessageView.java
//Synthetic comment -- index 706b2ab..4f78edb 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
//Synthetic comment -- @@ -1697,4 +1698,32 @@
}
}
}

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
    	switch (keycode) {
    	    case KeyEvent.KEYCODE_VOLUME_DOWN:
    	    	moveToOlder();
    	    	break;
    	    case KeyEvent.KEYCODE_VOLUME_UP:
    	    	moveToNewer();
    	    	break;
    	    default:
    	    	return super.onKeyDown(keycode, event);
    	}
    	return true;
    }
    
    // get rid of volume rocker default sound effect
    @Override
    public boolean onKeyUp(int keycode, KeyEvent event) {
    	switch (keycode) {
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	    case KeyEvent.KEYCODE_VOLUME_UP:
	    	break;
	    default:
	    	return super.onKeyUp(keycode, event);
	}
    	return true;
    }
}







