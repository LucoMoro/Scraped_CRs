/*WebViewCore: Fix the issue of wrong zoom density when opening web pages with overview mode disabled.

 On JellyBean browser, when pages opened
 with "open pages in overview" disabled and "Default zoom" option set to
 "Far" or "Close", the zoom density is not correct.

 The issue is caused by the wrong scale factor calculated when setup
 viewport. it is fixed to 1.5 in JB. whereas it should be different for
 different "Default zoom" setting.

 This Patch will fix this issue. it set the scale factor to the
 defaultzoomscale calculated considering zoom density. it also add
 adjust process to guarantee the zoom density is right after navigating
 page with specified viewport "target-densitydpi".

 Author: Zang Lin <lin.zang@intel.com>
 Signed-off-by: Jack Ren <jack.ren@intel.com>
 Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>

Change-Id:I68dd193457c40ce9eb73635297ce7bb61b373bac*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewCore.java b/core/java/android/webkit/WebViewCore.java
//Synthetic comment -- index 728ddbf..7766923 100644

//Synthetic comment -- @@ -2545,8 +2545,12 @@
// adjust the default scale to match the densityDpi
float adjust = 1.0f;
if (mViewportDensityDpi == -1) {
	    if( mWebViewClassic != null && mWebViewClassic.getDefaultZoomScale() * mSettings.getDefaultZoom().value != mContext.getResources().getDisplayMetrics().density * 100)
	    	mWebViewClassic.adjustDefaultZoomDensity( mSettings.getDefaultZoom().value);	    
      	    if( mWebViewClassic != null && (int)( mWebViewClassic.getDefaultZoomScale() * 100) != 100) {
		adjust = mWebViewClassic.getDefaultZoomScale();
	    }	      
	} else if (mViewportDensityDpi > 0) {
adjust = (float) mContext.getResources().getDisplayMetrics().densityDpi
/ mViewportDensityDpi;
}







