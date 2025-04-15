/*Add compatibility implementation of ActivityOptions and startActivity.

This will allow for constructing ActivityOptions and starting an activity
using them in a backwards-compatible way. The implementation is only used
on API 16 and newer. On platforms prior to API 16 the traditional
startActivity method is used.

Change-Id:I86c0df28dadc391b340303d448c70a25d33a76de*/




//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/ActivityCompat.java b/v4/java/android/support/v4/app/ActivityCompat.java
//Synthetic comment -- index c4aed2c..612a816 100644

//Synthetic comment -- @@ -17,7 +17,9 @@
package android.support.v4.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

/**
//Synthetic comment -- @@ -64,4 +66,29 @@
}
return false;
}

    /**
     * Start an activity with additional launch information, if able.
     *
     * <p>In Android 4.1+ additional options were introduced to allow for more
     * control on activity launch animations. Applications can use this method
     * along with {@link ActivityOptionsCompat} to use these animations when
     * available. When run on versions of the platform where this feature does
     * not exist the activity will be launched normally.</p>
     *
     * @param activity Context to launch activity from.
     * @param intent The description of the activity to start.
     * @param options Additional options for how the Activity should be started.
     *                May be null if there are no options. See
     *                {@link ActivityOptionsCompat} for how to build the Bundle
     *                supplied here; there are no supported definitions for
     *                building it manually.
     */
    public static void startActivity(Activity activity, Intent intent, Bundle options) {
        if (Build.VERSION.SDK_INT >= 16) {
            ActivityCompatJB.startActivity(activity, intent, options);
        } else {
            activity.startActivity(intent);
        }
    }
}








//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/ActivityOptionsCompat.java b/v4/java/android/support/v4/app/ActivityOptionsCompat.java
new file mode 100644
//Synthetic comment -- index 0000000..646b48c

//Synthetic comment -- @@ -0,0 +1,156 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

/**
 * Helper for accessing features in {@link android.app.ActivityOptions}
 * introduced in API level 16 in a backwards compatible fashion.
 */
public class ActivityOptionsCompat {
    /**
     * Create an ActivityOptions specifying a custom animation to run when the
     * activity is displayed.
     *
     * @param context Who is defining this. This is the application that the
     * animation resources will be loaded from.
     * @param enterResId A resource ID of the animation resource to use for the
     * incoming activity. Use 0 for no animation.
     * @param exitResId A resource ID of the animation resource to use for the
     * outgoing activity. Use 0 for no animation.
     * @return Returns a new ActivityOptions object that you can use to supply
     * these options as the options Bundle when starting an activity.
     */
    public static ActivityOptionsCompat makeCustomAnimation(Context context,
            int enterResId, int exitResId) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(
                ActivityOptionsCompatJB.makeCustomAnimation(context, enterResId, exitResId));
        }
        return new ActivityOptionsCompat();
    }

    /**
     * Create an ActivityOptions specifying an animation where the new activity is
     * scaled from a small originating area of the screen to its final full
     * representation.
     * <p/>
     * If the Intent this is being used with has not set its
     * {@link android.content.Intent#setSourceBounds(android.graphics.Rect)},
     * those bounds will be filled in for you based on the initial bounds passed
     * in here.
     *
     * @param source The View that the new activity is animating from. This
     * defines the coordinate space for startX and startY.
     * @param startX The x starting location of the new activity, relative to
     * source.
     * @param startY The y starting location of the activity, relative to source.
     * @param startWidth The initial width of the new activity.
     * @param startHeight The initial height of the new activity.
     * @return Returns a new ActivityOptions object that you can use to supply
     * these options as the options Bundle when starting an activity.
     */
    public static ActivityOptionsCompat makeScaleUpAnimation(View source,
            int startX, int startY, int startWidth, int startHeight) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(
                ActivityOptionsCompatJB.makeScaleUpAnimation(source, startX, startY,
                        startWidth, startHeight));
        }
        return new ActivityOptionsCompat();
    }

    /**
     * Create an ActivityOptions specifying an animation where a thumbnail is
     * scaled from a given position to the new activity window that is being
     * started.
     * <p/>
     * If the Intent this is being used with has not set its
     * {@link android.content.Intent#setSourceBounds(android.graphics.Rect)},
     * those bounds will be filled in for you based on the initial thumbnail
     * location and size provided here.
     *
     * @param source The View that this thumbnail is animating from. This
     * defines the coordinate space for startX and startY.
     * @param thumbnail The bitmap that will be shown as the initial thumbnail
     * of the animation.
     * @param startX The x starting location of the bitmap, relative to source.
     * @param startY The y starting location of the bitmap, relative to source.
     * @return Returns a new ActivityOptions object that you can use to supply
     * these options as the options Bundle when starting an activity.
     */
    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View source,
            Bitmap thumbnail, int startX, int startY) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(
                ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(source, thumbnail,
                        startX, startY));
        }
        return new ActivityOptionsCompat();
    }


    private static class ActivityOptionsImplJB extends ActivityOptionsCompat {
        private final ActivityOptionsCompatJB impl;

        ActivityOptionsImplJB(ActivityOptionsCompatJB impl) {
            this.impl = impl;
        }

        @Override
        public Bundle toBundle() {
            return impl.toBundle();
        }

        @Override
        public void update(ActivityOptionsCompat otherOptions) {
            if (otherOptions instanceof ActivityOptionsImplJB) {
                ActivityOptionsImplJB otherImpl = (ActivityOptionsImplJB)otherOptions;
                impl.update(otherImpl.impl);
            }
        }
    }


    protected ActivityOptionsCompat() {
    }

    /**
     * Returns the created options as a Bundle, which can be passed to
     * {@link ActivityCompat#startActivity(android.app.Activity, android.content.Intent, android.os.Bundle)}.
     * Note that the returned Bundle is still owned by the ActivityOptions
     * object; you must not modify it, but can supply it to the startActivity
     * methods that take an options Bundle.
     */
    public Bundle toBundle() {
        return null;
    }

    /**
     * Update the current values in this ActivityOptions from those supplied in
     * otherOptions. Any values defined in otherOptions replace those in the
     * base options.
     */
    public void update(ActivityOptionsCompat otherOptions) {
        // Do nothing.
    }
}








//Synthetic comment -- diff --git a/v4/jellybean/android/support/v4/app/ActivityCompatJB.java b/v4/jellybean/android/support/v4/app/ActivityCompatJB.java
new file mode 100644
//Synthetic comment -- index 0000000..a67c425

//Synthetic comment -- @@ -0,0 +1,27 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

class ActivityCompatJB {
    public static void startActivity(Context context, Intent intent, Bundle options) {
        context.startActivity(intent, options);
    }
}








//Synthetic comment -- diff --git a/v4/jellybean/android/support/v4/app/ActivityOptionsCompatJB.java b/v4/jellybean/android/support/v4/app/ActivityOptionsCompatJB.java
new file mode 100644
//Synthetic comment -- index 0000000..df9d987

//Synthetic comment -- @@ -0,0 +1,58 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.app;

import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

class ActivityOptionsCompatJB {

    public static ActivityOptionsCompatJB makeCustomAnimation(Context context,
            int enterResId, int exitResId) {
        return new ActivityOptionsCompatJB(
            ActivityOptions.makeCustomAnimation(context, enterResId, exitResId));
    }

    public static ActivityOptionsCompatJB makeScaleUpAnimation(View source,
            int startX, int startY, int startWidth, int startHeight) {
        return new ActivityOptionsCompatJB(
            ActivityOptions.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
    }

    public static ActivityOptionsCompatJB makeThumbnailScaleUpAnimation(View source,
            Bitmap thumbnail, int startX, int startY) {
        return new ActivityOptionsCompatJB(
            ActivityOptions.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
    }

    private final ActivityOptions mActivityOptions;

    private ActivityOptionsCompatJB(ActivityOptions activityOptions) {
        mActivityOptions = activityOptions;
    }

    public Bundle toBundle() {
        return mActivityOptions.toBundle();
    }

    public void update(ActivityOptionsCompatJB otherOptions) {
        mActivityOptions.update(otherOptions.mActivityOptions);
    }
}







