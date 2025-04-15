/*Fix for a hang when attempting to stream a non-existing content.

 -- StreamStarter Activity is binding the the MediaPlaybackService. As part of this, it is registering for the intent "ASYNC_OPEN_COMPLETE".
 -- In the normal scenario, after prepareAsync completes, the OnPreparedListener is fired. This ends up broadcasting the "ASYNC_OPEN_COMPLETE" intent.
 -- Since the StreamStarter Activity has registered for the intent, it will receive the even on it's listener, "onReceive". Here, the MediaPlaybackService "play" is called which starts the actual playback. And, the PLAYBACK_VIEWER intent is sent. This starts the MediaPlaybackActivity. And, "finish" is called in the StreamStarter Activity that will kill the activity. From this point, the MediaPlayback Activity takes over (this activity is the screen where you see the progress bar, and the metadata stuff).
 -- Now, in this scenario, there is a failure in the "prepareAsync" command. Because of this the ASYNC_OPEN_COMPLETE intent is never broadcasted, and hence the StreamStarterActivity never finishes.

- Proposed solution:
  -- Register the StreamStarter Activity to another intent (PLAYBACK_COMPLETE).
  -- In OnReceive call, add a condition that when the received intent is PLAYBACK_COMPLETE, "finish" the activity.
  -- In the MediaPlaybackService, in the implementation of onError(), broadcast the event "PLAYBACK_COMPLETE" when you receive an error. BUT, this intent needs to be broadcasted only if this is a single attempt playback, i.e., if "mOneShot" is "true".*/
//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index cfad551..2c7c006 100644

//Synthetic comment -- @@ -1702,6 +1702,7 @@

MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
public boolean onError(MediaPlayer mp, int what, int extra) {
switch (what) {
case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
mIsInitialized = false;
//Synthetic comment -- @@ -1712,11 +1713,14 @@
mMediaPlayer = new MediaPlayer(); 
mMediaPlayer.setWakeMode(MediaPlaybackService.this, PowerManager.PARTIAL_WAKE_LOCK);
mHandler.sendMessageDelayed(mHandler.obtainMessage(SERVER_DIED), 2000);
                    return true;
default:
break;
}
                return false;
}
};









//Synthetic comment -- diff --git a/src/com/android/music/StreamStarter.java b/src/com/android/music/StreamStarter.java
//Synthetic comment -- index 0537bad..b56cf37 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.os.RemoteException;
import android.view.Window;
import android.widget.TextView;

public class StreamStarter extends Activity
{
//Synthetic comment -- @@ -57,6 +58,7 @@
try {
IntentFilter f = new IntentFilter();
f.addAction(MediaPlaybackService.ASYNC_OPEN_COMPLETE);
registerReceiver(mStatusListener, new IntentFilter(f));
MusicUtils.sService.openfileAsync(getIntent().getData().toString());
} catch (RemoteException ex) {
//Synthetic comment -- @@ -71,6 +73,15 @@
private BroadcastReceiver mStatusListener = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
try {
MusicUtils.sService.play();
intent = new Intent("com.android.music.PLAYBACK_VIEWER");







