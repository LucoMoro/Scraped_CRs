/*Update the power control widget when the GPS state is changed.

When there is power control widget in the homescreen, update the
GPS state in Settings application, press back to the homescreen,
the GPS state indicated by power control widget is not updated.
This fix would solve the above problem.

Change-Id:I2d60f4b04bcaaead17e69cefaae48b0734405409*/




//Synthetic comment -- diff --git a/src/com/android/settings/SecuritySettings.java b/src/com/android/settings/SecuritySettings.java
//Synthetic comment -- index 1348d48..d68da4d 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.content.ContentQueryMap;
import android.content.ContentResolver;
import android.content.Context;
//Synthetic comment -- @@ -95,6 +96,7 @@
private final class SettingsObserver implements Observer {
public void update(Observable o, Object arg) {
updateToggles();
            updateWidget();
}
}

//Synthetic comment -- @@ -283,6 +285,20 @@
}
}

    /*
     * Update the widget when the state is changed
     */
    private void updateWidget() {
        // When changing the state in the Settings menu, and press the
        // "Back" key until the home screen the widget does not
        // reflect the update. Therefore we send an intent to the widget
        // to re-read the current state, and update the icon.
        Intent widgetUpdate = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetUpdate.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        sendBroadcast(widgetUpdate);
    }

private boolean isToggled(Preference pref) {
return ((CheckBoxPreference) pref).isChecked();
}







