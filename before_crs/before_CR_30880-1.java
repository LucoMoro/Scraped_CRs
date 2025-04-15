/*Observer for GPS is never added back.

mSettingsObserver will be added only once  to mContentQueryMap on first start,
but if you bring activity back to top, it will not be added any more.

Signed-off-by: Vladimir Baryshnikov <vovkab@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/LocationSettings.java b/src/com/android/settings/LocationSettings.java
//Synthetic comment -- index ef438e6..fe2052c 100644

//Synthetic comment -- @@ -123,8 +123,9 @@
updateLocationToggles();
}
};
            mContentQueryMap.addObserver(mSettingsObserver);
}
}

@Override







