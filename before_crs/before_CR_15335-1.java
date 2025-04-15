/*replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:Icc7b1b6948fda1a5980a8407a3dc9b0b6ced79bb*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 84a67cf..0c13554 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
//Synthetic comment -- @@ -1042,7 +1043,7 @@

private void pruneDeadThumbnailFiles() {
HashSet<String> existingFiles = new HashSet<String>();
        String directory = "/sdcard/DCIM/.thumbnails";
String [] files = (new File(directory)).list();
if (files == null)
files = new String[0];








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/MediaFrameworkTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/MediaFrameworkTest.java
//Synthetic comment -- index 9fb49b1..4958c2b 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Downloads;
import android.util.Log;
import android.util.Log;
//Synthetic comment -- @@ -44,25 +45,25 @@
import java.io.FileDescriptor;
import java.net.InetAddress;

 
public class MediaFrameworkTest extends Activity {
    
//public static Surface video_sf;
public static SurfaceView mSurfaceView;
private MediaController mMediaController;
private String urlpath;
private MediaPlayer mpmidi;
private MediaPlayer mpmp3;
    private String testfilepath = "/sdcard/awb.awb";
    
public static AssetFileDescriptor midiafd;
public static AssetFileDescriptor mp3afd;
    
    
public MediaFrameworkTest() {
}

    
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle icicle) {
//Synthetic comment -- @@ -71,14 +72,14 @@
mSurfaceView = (SurfaceView)findViewById(R.id.surface_view);
ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
//Get the midi fd
midiafd = this.getResources().openRawResourceFd(R.raw.testmidi);
        
//Get the mp3 fd
mp3afd = this.getResources().openRawResourceFd(R.raw.testmp3);
}
    
public void startPlayback(String filename){
String mimetype = "audio/mpeg";
Uri path = Uri.parse(filename);
//Synthetic comment -- @@ -86,7 +87,7 @@
intent.setDataAndType(path, mimetype);
startActivity(intent);
}
    
@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_0:
//Synthetic comment -- @@ -99,50 +100,50 @@
mp.start();
}catch (Exception e){}
break;
          
          //start the music player intent with the test URL from PV    
case KeyEvent.KEYCODE_1:
startPlayback(MediaNames.STREAM_MP3_1);
break;
          
case KeyEvent.KEYCODE_2:
startPlayback(MediaNames.STREAM_MP3_2);
break;
          
case KeyEvent.KEYCODE_3:
startPlayback(MediaNames.STREAM_MP3_3);
break;
          
case KeyEvent.KEYCODE_4:
startPlayback(MediaNames.STREAM_MP3_4);
break;
          
case KeyEvent.KEYCODE_5:
startPlayback(MediaNames.STREAM_MP3_5);
break;
          
case KeyEvent.KEYCODE_6:
startPlayback(MediaNames.STREAM_MP3_6);
break;
          
case KeyEvent.KEYCODE_7:
startPlayback(MediaNames.STREAM_MP3_7);
break;
          
case KeyEvent.KEYCODE_8:
startPlayback(MediaNames.STREAM_MP3_8);
break;
          
case KeyEvent.KEYCODE_9:
startPlayback(MediaNames.STREAM_MP3_9);
break;
          
              
              
}
return super.onKeyDown(keyCode, event);
     
  }  

public static boolean checkStreamingServer() throws Exception {
InetAddress address = InetAddress.getByAddress(MediaNames.STREAM_SERVER);








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/MediaNames.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/MediaNames.java
//Synthetic comment -- index 5127255..2151a09 100755

//Synthetic comment -- @@ -16,26 +16,29 @@

package com.android.mediaframeworktest;

/**
 * 
 * This class has the names of the all the activity name and variables 
* in the instrumentation test.
*
*/
public class MediaNames {
//A directory to hold all kinds of media files
    public static final String MEDIA_SAMPLE_POOL = "/sdcard/media_api/samples/";
//Audio files
    public static final String MP3CBR = "/sdcard/media_api/music/MP3_256kbps_2ch.mp3";
    public static final String MP3VBR = "/sdcard/media_api/music/MP3_256kbps_2ch_VBR.mp3";
    public static final String SHORTMP3 = "/sdcard/media_api/music/SHORTMP3.mp3";
    public static final String MIDI = "/sdcard/media_api/music/ants.mid";
    public static final String WMA9 = "/sdcard/media_api/music/WMA9.wma";
    public static final String WMA10 = "/sdcard/media_api/music/WMA10.wma";
    public static final String WAV = "/sdcard/media_api/music/rings_2ch.wav";
    public static final String AMR = "/sdcard/media_api/music/test_amr_ietf.amr";
    public static final String OGG = "/sdcard/media_api/music/Revelation.ogg";
  
public static final int MP3CBR_LENGTH = 71000;
public static final int MP3VBR_LENGTH = 71000;
public static final int SHORTMP3_LENGTH = 286;
//Synthetic comment -- @@ -45,462 +48,462 @@
public static final int AMR_LENGTH = 37000;
public static final int OGG_LENGTH = 4000;
public static final int SEEK_TIME = 10000;
  
public static final long PAUSE_WAIT_TIME = 3000;
public static final long WAIT_TIME = 2000;
public static final long WAIT_LONG = 4000;
  
//Streaming Video
    public static final String VIDEO_HTTP3GP = "http://pvs.pv.com/jj/lipsync0.3gp";  
public static final String VIDEO_RTSP3GP = "rtsp://63.241.31.203/public/jj/md.3gp";
public static final String VIDEO_RTSP3GP2 = "rtsp://pvs.pv.com/public/live_dvd1.3gp";
    public static final String VIDEO_RTSP3GP3 = 
"rtsp://ehug.rtsp-youtube.l.google.com/" +
"Ci4LENy73wIaJQmeRVCJq4HuQBMYDSANFEIJbXYtZ29vZ2xlSARSB2RldGFpbHMM/0/0/0/video.3gp";
//public static final String VIDEO_RTSP3GP = "rtsp://193.159.241.21/sp/alizee05.3gp";
  
//local video
    public static final String VIDEO_MP4 = "/sdcard/media_api/video/MPEG4_320_AAC_64.mp4";
    public static final String VIDEO_LONG_3GP = "/sdcard/media_api/video/radiohead.3gp";
    public static final String VIDEO_SHORT_3GP = "/sdcard/media_api/video/short.3gp";
    public static final String VIDEO_LARGE_SIZE_3GP = "/sdcard/media_api/video/border_large.3gp";
    public static final String VIDEO_H263_AAC = "/sdcard/media_api/video/H263_56_AAC_24.3gp";
    public static final String VIDEO_H263_AMR = "/sdcard/media_api/video/H263_56_AMRNB_6.3gp";
    public static final String VIDEO_H264_AAC = "/sdcard/media_api/video/H264_320_AAC_64.3gp";
    public static final String VIDEO_H264_AMR = "/sdcard/media_api/video/H264_320_AMRNB_6.3gp";
    public static final String VIDEO_WMV = "/sdcard/media_api/video/bugs.wmv";
    public static final String VIDEO_HIGHRES_H263 = "/sdcard/media_api/video/H263_500_AMRNB_12.3gp";
    public static final String VIDEO_HIGHRES_MP4 = "/sdcard/media_api/video/H264_500_AAC_128.3gp";
    
//ringtone
    public static final String ringtone = "/sdcard/media_api/ringtones/F1_NewVoicemail.mp3";

//streaming mp3
    public static final String STREAM_MP3_1 = 
"http://wms.pv.com:7070/MediaDownloadContent/mp3/chadthi_jawani_128kbps.mp3";
    public static final String STREAM_MP3_2 = 
"http://wms.pv.com:7070/MediaDownloadContent/mp3/dualStereo.mp3";
    public static final String STREAM_MP3_3 = 
"http://wms.pv.com:7070/mediadownloadcontent/UserUploads/15%20Keep%20Holding%20On.mp3";
    public static final String STREAM_MP3_4 = 
"http://wms.pv.com:7070/mediadownloadcontent/UserUploads/1%20-%20Apologize.mp3";
    public static final String STREAM_MP3_5 = 
"http://wms.pv.com:7070/mediadownloadcontent/UserUploads/" +
"03%20You're%20Gonna%20Miss%20This.mp3";
    public static final String STREAM_MP3_6 = 
"http://wms.pv.com:7070/mediadownloadcontent/UserUploads" +
"/02%20Looney%20Tunes%20%C3%82%C2%B7%20Light%20Cavalry%20Overture%20(LP%20Version).mp3";
    public static final String STREAM_MP3_7 = 
"http://wms.pv.com:7070/mediadownloadcontent/UserUploads" +
"/01%20Love%20Song%20(Album%20Version).mp3";
    public static final String STREAM_MP3_8 = 
"http://wms.pv.com:7070/MediaDownloadContent/UserUploads/1%20-%20Apologize.mp3";
    public static final String STREAM_MP3_9 = 
"http://wms.pv.com:7070/MediaDownloadContent/UserUploads" +
"/1%20-%20Smile%20(Explicit%20Version).mp3";
    public static final String STREAM_MP3_10 = 
"http://wms.pv.com:7070/MediaDownloadContent/UserUploads/beefcake.mp3";

//Sonivox
    public static String MIDIFILES[] = { 
        "/sdcard/media_api/music/Leadsol.mxmf",
        "/sdcard/media_api/music/abba.imy", "/sdcard/media_api/music/ants.mid",
        "/sdcard/media_api/music/greensleeves.rtttl", "/sdcard/media_api/music/test.ota"};
  
//Performance measurement
    public static String[] WAVFILES = { 
        "/sdcard/media_api/music_perf/WAV/M1F1-AlawWE-AFsp.wav",
        "/sdcard/media_api/music_perf/WAV/M1F1-float64-AFsp.wav",
        "/sdcard/media_api/music_perf/WAV/song.wav",
        "/sdcard/media_api/music_perf/WAV/WAVEtest.wav",
        "/sdcard/media_api/music_perf/WAV/WAVEtest_out.wav",
        "/sdcard/media_api/music_perf/WAV/test_out.wav"};
        
    public static String[] AMRNBFILES = { 
        "/sdcard/media_api/music_perf/AMR/AI_AMR-NB_5.9kbps_6.24kbps_8khz_mono_NMC.amr",
        "/sdcard/media_api/music_perf/AMR/AI_AMR-NB_5.15kbps_5.46kbps_8khz_mono_NMC.amr",
        "/sdcard/media_api/music_perf/AMR/AI_AMR-NB_7.4kbps_7.80kbps_8khz_mono_NMC.amr",
        "/sdcard/media_api/music_perf/AMR/AI_AMR-NB_7.95kbps_9.6kbps_8khz_mono_NMC.amr",
        "/sdcard/media_api/music_perf/AMR/AI_AMR-NB_10.2kbps_10.48kbps_8khz_mono_NMC.amr"};
  
    public static String[] AMRWBFILES = { 
        "/sdcard/media_api/music_perf/AMRWB/NIN_AMR-WB_15.85kbps_16kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/NIN_AMR-WB_18.25kbps_18kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/NIN_AMR-WB_19.85kbps_20kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/NIN_AMR-WB_23.05kbps_23kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/NIN_AMR-WB_23.85kbps_24kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/PD_AMR-WB_19.85kbps_20kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/PD_AMR-WB_23.05kbps_23kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/PD_AMR-WB_23.85kbps_24kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/WC_AMR-WB_23.05kbps_23kbps.amr",
        "/sdcard/media_api/music_perf/AMRWB/WC_AMR-WB_23.85kbps_24kbps.amr", };
 
    public static String[] MP3FILES = { 
        "/sdcard/media_api/music_perf/MP3/NIN_56kbps_32khz_stereo_VBR_MCA.MP3",
        "/sdcard/media_api/music_perf/MP3/NIN_80kbps_32khz_stereo_VBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_80kbps_44.1khz_stereo_VBR_MCA.mp3", 
        "/sdcard/media_api/music_perf/MP3/NIN_80kbps_48khz_stereo_VBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_112kbps_32khz_stereo_VBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_112kbps_44.1khz_stereo_VBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_112kbps_48khz_stereo_VBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_192kbps_32khz_mono_CBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_192kbps_44.1khz_mono_CBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_192kbps_48khz_mono_CBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_256kbps_44.1khz_mono_CBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/NIN_256kbps_48khz_mono_CBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/PD_112kbps_32khz_stereo_VBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/PD_112kbps_44.1khz_stereo_VBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/PD_112kbps_48khz_stereo_VBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/PD_192kbps_32khz_mono_CBR_DPA.mp3",
        "/sdcard/media_api/music_perf/MP3/PD_256kbps_44.1khz_mono_CBR_DPA.mp3",
        "/sdcard/media_api/music_perf/MP3/PD_256kbps_48khz_mono_CBR_MCA.mp3",
        "/sdcard/media_api/music_perf/MP3/WC_256kbps_44.1khz_mono_CBR_DPA.mp3",
        "/sdcard/media_api/music_perf/MP3/WC_256kbps_48khz_mono_CBR_DPA.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/Apologize.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/Because_Of_You.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/Complicated.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/Glamorous.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/Im_With_You.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/Smile.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/Suddenly_I_See.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/When You Say Nothing At All.mp3",
        "/sdcard/media_api/music_perf/regular_album_photo/my_happy_ending.mp3"};
  
    public static String[] AACFILES = { 
        "/sdcard/media_api/music_perf/AAC/AI_AAC_24kbps_12khz_Mono_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/AI_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/AI_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/AI_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/AI_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/AI_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/NIN_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/NIN_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/NIN_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/NIN_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/NIN_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PD_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PD_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PD_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PD_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PD_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PV_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PV_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PV_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PV_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/PV_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/WC_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/WC_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/WC_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/WC_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        "/sdcard/media_api/music_perf/AAC/WC_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",      
};
    
    public static String[] VIDEOFILES = { "/sdcard/media_api/video_perf/AI_CTO_Mpeg4_32kbps_10fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
      "/sdcard/media_api/video_perf/AI_CTO_Mpeg4_32kbps_12fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
      "/sdcard/media_api/video_perf/AI_CTO_Mpeg4_32kbps_15fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
      "/sdcard/media_api/video_perf/AI_CTO_Mpeg4_32kbps_5fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
      "/sdcard/media_api/video_perf/AI_CTO_Mpeg4_32kbps_5fps_SQCIF_128x96+AAC_8kbps_8khz_mono_SSE.mp4",
      "/sdcard/media_api/video_perf/AI_CTO_Mpeg4_32kbps_7.5fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
      "/sdcard/media_api/video_perf/AI_WMV_1024kbps_20fps_QCIF_176x144_noaudio_SSE.wmv",
      "/sdcard/media_api/video_perf/AI_WMV_1024kbps_25fps_QCIF_176x144_noaudio_SSE.wmv",
      "/sdcard/media_api/video_perf/Chicken.wmv",
      "/sdcard/media_api/video_perf/MP_qcif_15fps_100kbps_48kHz_192kbps_30secs.wmv",
      "/sdcard/media_api/video_perf/NIN_CTO_H264_123kbps_5fps_QCIF_176x144+AMR_12.2kbps_8khz_mono_QTE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_H264_96kbps_10.2fps_QCIF_176x144+AMR_12.2kbps_8khz_mono_QTE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_H264_96kbps_12fps_QCIF_176x144+AMR_12.2kbps_8khz_mono_QTE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_H264_96kbps_15fps_QCIF_176x144+AMR_12.2kbps_8khz_mono_QTE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_Mpeg4_123kbps_15fps_QCIF_176x144+AAC_32kbps_22khz_mono_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_Mpeg4_123kbps_7.5fps_QCIF_176x144+AAC_32kbps_22khz_stereo_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_Mpeg4_128kbps_10fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_Mpeg4_128kbps_12fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_Mpeg4_128kbps_15fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_Mpeg4_128kbps_5fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_CTO_Mpeg4_128kbps_7.5fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_H263_128kbps_10fps_QCIF_174x144_noaudio_SSE.mp4",
      "/sdcard/media_api/video_perf/NIN_H263_128kbps_15fps_QCIF_174x144_noaudio_SSE.mp4",
      "/sdcard/media_api/video_perf/NIN_H263_48kbps_10fps_QCIF_174x144_noaudio_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_H263_48kbps_12fps_QCIF_174x144_noaudio_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_H264_123kbps_15fps_QCIF_176x144+AAC_32kbps_22khz_stereo_SSE.3gp",
      "/sdcard/media_api/video_perf/NIN_H264_123kbps_7.5fps_QCIF_176x144+AAC_32kbps_22khz_stereo_SSE.3gp",
      "/sdcard/media_api/video_perf/PV_H264_2000kbps_20fps_CIF_352x288+AAC_96kbps_48khz_stereo_SSE.mp4",
      "/sdcard/media_api/video_perf/PV_H264_2000kbps_25fps_CIF_352x288+AAC_96kbps_48khz_stereo_SSE.mp4",
      "/sdcard/media_api/video_perf/PV_H264_2000kbps_30fps_CIF_352x288+AAC_128kbps_48khz_stereo_SSE.mp4",
      "/sdcard/media_api/video_perf/Stevie-1.wmv",
      "/sdcard/media_api/video_perf/WC_H264_1600kbps_20fps_QCIF_176x144+AAC_96kbps_48khz_mono_SSE.mp4",
      "/sdcard/media_api/video_perf/WC_H264_1600kbps_25fps_QCIF_176x144+AAC_96kbps_48khz_mono_SSE.mp4",
      "/sdcard/media_api/video_perf/WC_H264_1600kbps_30fps_QCIF_176x144+AAC_96kbps_48khz_mono_SSE.mp4",
      "/sdcard/media_api/video_perf/bugs.wmv",
      "/sdcard/media_api/video_perf/niceday.wmv",
      "/sdcard/media_api/video_perf/eaglesatopnflpe.wmv",
     
};
    
//wma - only support up to wma 9
public static String[] WMASUPPORTED = {
      "/sdcard/media_api/music_perf/WMASUPPORTED/AI_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/AI_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/NIN_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/NIN_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/PD_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/PD_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/PV_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/PV_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/WC_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMASUPPORTED/WC_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma"
      
};
    
    public static String[] WMAUNSUPPORTED = { 
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_127kbps_48khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_128kbps_44.1khz_stereo_2pVBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_128kbps_48khz_stereo_2pVBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_128kbps_88khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_128kbps_96khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_192kbps_44.1khz_stereo_2pVBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_192kbps_88khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_192kbps_96khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_256kbps_44khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_256kbps_48khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_256kbps_88khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_256kbps_96khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_384kbps_44khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_384kbps_48khz_stereo_CBR_DPA.wma",
      "/sdcard/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_384kbps_88khz_stereo_CBR_DPA.wma"
};
    
//Media Recorder
    public static final String RECORDER_OUTPUT = "/sdcard/media_api/recorderOutput.amr";
    
//video thumbnail
    public static final String THUMBNAIL_OUTPUT = "/sdcard/media_api/videoThumbnail.png";
    public static final String GOLDEN_THUMBNAIL_OUTPUT = "/sdcard/media_api/goldenThumbnail.png";
    
//Metadata Utility
public static final String[] THUMBNAIL_CAPTURE_TEST_FILES = {
      "/sdcard/media_api/metadata/test.mp4",
      "/sdcard/media_api/metadata/test1.3gp",
      "/sdcard/media_api/metadata/test2.3gp",
      "/sdcard/media_api/metadata/test3.3gp",
      "/sdcard/media_api/metadata/test4.3gp",
      "/sdcard/media_api/metadata/test5.3gp",
      "/sdcard/media_api/metadata/test6.3gp",
      "/sdcard/media_api/metadata/test7.3gp",
      "/sdcard/media_api/metadata/test8.3gp",
      "/sdcard/media_api/metadata/test9.3gp",
      "/sdcard/media_api/metadata/test10.3gp",
      "/sdcard/media_api/metadata/test11.3gp",
      "/sdcard/media_api/metadata/test12.3gp",
      "/sdcard/media_api/metadata/test13.3gp",
      "/sdcard/media_api/metadata/test14.3gp",
      "/sdcard/media_api/metadata/test15.3gp",
      "/sdcard/media_api/metadata/test16.3gp",
      "/sdcard/media_api/metadata/test17.3gp",
      "/sdcard/media_api/metadata/test18.3gp",
      "/sdcard/media_api/metadata/test19.3gp",
      "/sdcard/media_api/metadata/test20.3gp",
      "/sdcard/media_api/metadata/test21.3gp",
      "/sdcard/media_api/metadata/test22.3gp",
      "/sdcard/media_api/metadata/test23.3gp",
      "/sdcard/media_api/metadata/test24.3gp",
      "/sdcard/media_api/metadata/test25.3gp",
      "/sdcard/media_api/metadata/test26.3gp",
      "/sdcard/media_api/metadata/test27.3gp",
      "/sdcard/media_api/metadata/test28.3gp",
      "/sdcard/media_api/metadata/test29.3gp",
      "/sdcard/media_api/metadata/test30.3gp",
      "/sdcard/media_api/metadata/test31.3gp",
      "/sdcard/media_api/metadata/test32.3gp",
      "/sdcard/media_api/metadata/test33.3gp",
      "/sdcard/media_api/metadata/test35.mp4",
      "/sdcard/media_api/metadata/test36.m4v",
      "/sdcard/media_api/metadata/test34.wmv",
      "/sdcard/media_api/metadata/test_metadata.mp4",
};
  
public static final String[] METADATA_RETRIEVAL_TEST_FILES = {
// Raw AAC is not supported
      // "/sdcard/media_api/test_raw.aac",
      // "/sdcard/media_api/test_adts.aac",
      // "/sdcard/media_api/test_adif.aac",
      "/sdcard/media_api/metadata/test_metadata.mp4",
      "/sdcard/media_api/metadata/WMA10.wma",
      "/sdcard/media_api/metadata/Leadsol_out.wav",
      "/sdcard/media_api/metadata/test_aac.mp4",
      "/sdcard/media_api/metadata/test_amr.mp4",
      "/sdcard/media_api/metadata/test_avc_amr.mp4",
      "/sdcard/media_api/metadata/test_metadata.mp4",
      "/sdcard/media_api/metadata/test_vbr.mp3",
      "/sdcard/media_api/metadata/test_cbr.mp3",
      "/sdcard/media_api/metadata/metadata_test1.mp3",
      "/sdcard/media_api/metadata/test33.3gp",
      "/sdcard/media_api/metadata/test35.mp4",
      "/sdcard/media_api/metadata/test36.m4v",
      "/sdcard/media_api/metadata/test_m4v_amr.mp4",
      "/sdcard/media_api/metadata/test_h263_amr.mp4",
      "/sdcard/media_api/metadata/test34.wmv",
};
  
public static final String[] ALBUMART_TEST_FILES = {
      "/sdcard/media_api/album_photo/test_22_16_mp3.mp3",
      "/sdcard/media_api/album_photo/PD_256kbps_48khz_mono_CBR_MCA.mp3",
      "/sdcard/media_api/album_photo/PD_256kbps_44.1khz_mono_CBR_DPA.mp3",
      "/sdcard/media_api/album_photo/PD_192kbps_32khz_mono_CBR_DPA.mp3",
      "/sdcard/media_api/album_photo/NIN_256kbps_48khz_mono_CBR_MCA.mp3",
      "/sdcard/media_api/album_photo/NIN_256kbps_44.1khz_mono_CBR_MCA.mp3",
      "/sdcard/media_api/album_photo/NIN_112kbps(96kbps)_48khz_stereo_VBR_MCA.mp3",
      "/sdcard/media_api/album_photo/NIN_112kbps(96kbps)_44.1khz_stereo_VBR_MCA.mp3",
      "/sdcard/media_api/album_photo/lightGreen1.mp3",
      "/sdcard/media_api/album_photo/babyBlue2 1.mp3",
      "/sdcard/media_api/album_photo/2-01 01 NIN_56kbps(64kbps)_32khz_stereo_VBR_MCA.mp3",
      "/sdcard/media_api/album_photo/02_NIN_112kbps(80kbps)_32khz_stereo_VBR_MCA.mp3",
      "/sdcard/media_api/album_photo/No_Woman_No_Cry_128K.wma",
      "/sdcard/media_api/album_photo/Beethoven_2.wma",
};

//TEST_PATH_1: is a video and contains metadata for key "num-tracks"
// TEST_PATH_2: any valid media file.
// TEST_PATH_3: invalid media file
  public static final String TEST_PATH_1 = "/sdcard/media_api/metadata/test.mp4";
  public static final String TEST_PATH_3 = "/sdcard/media_api/data.txt";
public static final String TEST_PATH_4 = "somenonexistingpathname";
public static final String TEST_PATH_5 = "mem://012345";
  
//Meta data expected result
//The expected tag result in the following order
//cd_track_number, album, artist, author, composer, date, genre
//title, years, duration
public static final String META_DATA_MP3 [][] = {
      {"/sdcard/media_api/metaDataTestMedias/MP3/ID3V1_ID3V2.mp3", "1/10", "ID3V2.3 Album", "ID3V2.3 Artist",
"ID3V2.3 Lyricist", "ID3V2.3 Composer", null, "Blues",
"ID3V2.3 Title", "1234", "295", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/ID3V2.mp3", "1/10", "ID3V2.3 Album", "ID3V2.3 Artist",
          "ID3V2.3 Lyricist", "ID3V2.3 Composer", null, "Blues", 
"ID3V2.3 Title", "1234", "287", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/ID3V1.mp3", "1", "test ID3V1 Album", "test ID3V1 Artist",
null, null, null, "255", "test ID3V1 Title", "1234", "231332", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V1.mp3" , null, null, null,
null, null, null, null, null, null, "231330", "1", null},
//The corrupted TALB field in id3v2 would not switch to id3v1 tag automatically
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TALB.mp3", "01", null, "ID3V2.3 Artist",
          "ID3V2.3 Lyricist", "ID3V2.3 Composer", null, 
"Blues", "ID3V2.3 Title", "1234", "295", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TCOM.mp3", "01", "ID3V2.3 Album", 
           "ID3V2.3 Artist", "ID3V2.3 Lyricist", null, null, 
"Blues", "ID3V2.3 Title", "1234", "295", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TCOM_2.mp3", "01", "ID3V2.3 Album", 
"ID3V2.3 Artist", null, null, null, "Blues", "ID3V2.3 Title", "1234", "295", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TRCK.mp3", "dd", "ID3V2.3 Album", 
"ID3V2.3 Artist", "ID3V2.3 Lyricist", "ID3V2.3 Composer", null,
"Blues", "ID3V2.3 Title", "1234", "295", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TRCK_2.mp3", "01", "ID3V2.3 Album", 
"ID3V2.3 Artist", null, null, null, "255", "ID3V2.3 Title", "1234", "295", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TYER.mp3", "01", "ID3V2.3 Album",
"ID3V2.3 Artist", null, null, null, null, "ID3V2.3 Title", "9999", "295", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TYER_2.mp3", "01", "ID3V2.3 Album",
           "ID3V2.3 Artist", "ID3V2.3 Lyricist", "ID3V2.3 Composer", null, 
"Blues", "ID3V2.3 Title", null, "295", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TIT.mp3", null, null, null,
null, null, null, null, null, null, "295", "1", null}
};

public static final String META_DATA_OTHERS [][] = {
      {"/sdcard/media_api/metaDataTestMedias/3GP/cat.3gp", null, null, null,
null, null, "20080309T002415.000Z", null,
null, null, "63916", "2", null},
      {"/sdcard/media_api/metaDataTestMedias/AMR/AMR_NB.amr", null, null, null,
null, null, null, null,
null, null, "126540", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/AMRWB/AMR_WB.amr", null, null, null,
null, null, null, null,
null, null, "231180", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/M4A/Jaws Of Life_ver1.m4a", "1/8", "Suspended Animation",
          "John Petrucci", null, null, "20070510T125223.000Z", 
"13", "Jaws Of Life", "2005", "449329", "1", "m4a composer"},
      {"/sdcard/media_api/metaDataTestMedias/M4V/sample_iPod.m4v", null, null, 
          null, null, null, "20051220T202015.000Z", 
null, null, null, "85500", "2", null},
      {"/sdcard/media_api/metaDataTestMedias/MIDI/MIDI.mid", null, "Suspended Animation", 
          "John Petrucci", null, null, "20070510T125223.000Z", 
null, null, "2005", "231180", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/MP4/kung_fu_panda_h264.mp4", "2/0", "mp4 album Kung Fu Panda",
          "mp4 artist Kung Fu Panda", null, null, "20080517T091451.000Z", 
"41", "Kung Fu Panda", "2008", "128521", "2", "mp4 composer"},
      {"/sdcard/media_api/metaDataTestMedias/OGG/Ring_Classic_02.ogg", null, "Suspended Animation", 
          "John Petrucci", null, null, "20070510T125223.000Z", 
null, null, "2005", "231180", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/OGG/When You Say Nothing At All.ogg", 
          null, "Suspended Animation", "John Petrucci", 
null, null, "20070510T125223.000Z", null, null, "2005", "231180", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/WAV/Im With You.wav", null, null, 
          null, null, null, null, 
null, null, null, "224000", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/WMA/WMA9.wma", "6", "Ten Songs in the Key of Betrayal", 
          "Alien Crime Syndicate", "Alien Crime Syndicate", 
          "wma 9 Composer", "20040521T175729.483Z", 
"Rock", "Run for the Money", "2004", "134479", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/WMA/WMA10.wma", "09", "wma 10 Album", 
          "wma 10 Album Artist", "wma 10 Artist", "wma 10 Composer", "20070705T063625.097Z", 
"Acid Jazz", "wma 10 Title", "2010", "126574", "1", null},
      {"/sdcard/media_api/metaDataTestMedias/WMV/bugs.wmv", "8", "wmv 9 Album", 
          null, "wmv 9 Artist ", null, "20051122T155247.540Z", 
null, "Looney Tunes - Hare-Breadth Hurry", "2005", "193482", "2", null},
      {"/sdcard/media_api/metaDataTestMedias/WMV/clips_ver7.wmv", "50", "wmv 7 Album", 
          null, "Hallau Shoots & Company", null, "20020226T170045.891Z", 
null, "CODEC Shootout", "1986", "43709", "2", null}
};
  
//output recorded video
  
  public static final String RECORDED_HVGA_H263 = "/sdcard/HVGA_H263.3gp";
  public static final String RECORDED_QVGA_H263 = "/sdcard/QVGA_H263.3gp";
  public static final String RECORDED_SQVGA_H263 = "/sdcard/SQVGA_H263.3gp";
  public static final String RECORDED_CIF_H263 = "/sdcard/CIF_H263.3gp";
  public static final String RECORDED_QCIF_H263 = "/sdcard/QCIF_H263.3gp";
  public static final String RECORDED_PORTRAIT_H263 = "/sdcard/QCIF_mp4.3gp";
  
  public static final String RECORDED_HVGA_MP4 = "/sdcard/HVGA_mp4.mp4";
  public static final String RECORDED_QVGA_MP4 = "/sdcard/QVGA_mp4.mp4";
  public static final String RECORDED_SQVGA_MP4 = "/sdcard/SQVGA_mp4.mp4";
  public static final String RECORDED_CIF_MP4 = "/sdcard/CIF_mp4.mp4";
  public static final String RECORDED_QCIF_MP4 = "/sdcard/QCIF_mp4.mp4";
  
  public static final String RECORDED_VIDEO_3GP = "/sdcard/temp.3gp";
  
  public static final String INVALD_VIDEO_PATH = "/sdcard/media_api/filepathdoesnotexist" +
"/filepathdoesnotexist/temp.3gp";
  
 
public static final long RECORDED_TIME = 5000;
public static final long VALID_VIDEO_DURATION = 2000;
  
//Videos for the mediaplayer stress test
  public static String[] H263_STRESS = { 
      "/sdcard/media_api/video_stress/h263/H263_CIF.3gp",
      "/sdcard/media_api/video_stress/h263/H263_QCIF.3gp",
      "/sdcard/media_api/video_stress/h263/H263_QVGA.3gp",
      "/sdcard/media_api/video_stress/h263/H263_SQVGA.3gp"
};
  
  public static String[] MPEG4_STRESS = { 
    "/sdcard/media_api/video_stress/h263/mpeg4_CIF.mp4",
    "/sdcard/media_api/video_stress/h263/mpeg4_QCIF.3gp",
    "/sdcard/media_api/video_stress/h263/mpeg4_QVGA.3gp",
    "/sdcard/media_api/video_stress/h263/mpeg4_SQVGA.mp4"
};
  
//Streaming test files
public static final byte [] STREAM_SERVER = new byte[] {(byte)75,(byte)17,(byte)48,(byte)204};
  public static final String STREAM_H264_480_360_1411k = 
"http://75.17.48.204:10088/yslau/stress_media/h264_regular.mp4";
  public static final String STREAM_WMV = 
"http://75.17.48.204:10088/yslau/stress_media/bugs.wmv";
  public static final String STREAM_H263_176x144_325k = 
"http://75.17.48.204:10088/yslau/stress_media/h263_regular.3gp";
  public static final String STREAM_H264_352x288_1536k = 
"http://75.17.48.204:10088/yslau/stress_media/h264_highBitRate.mp4";
  public static final String STREAM_MP3= 
"http://75.17.48.204:10088/yslau/stress_media/mp3_regular.mp3";
  public static final String STREAM_MPEG4_QVGA_128k = 
"http://75.17.48.204:10088/yslau/stress_media/mpeg4_qvga_24fps.3gp";
public static final int STREAM_H264_480_360_1411k_DURATION = 46000;
public static final int VIDEO_H263_AAC_DURATION = 501000;








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/CameraTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/CameraTest.java
//Synthetic comment -- index e66e560..2bf1e66 100644

//Synthetic comment -- @@ -30,47 +30,48 @@
import android.util.Log;
import android.view.SurfaceHolder;

import android.os.Looper;

import android.test.suitebuilder.annotation.LargeTest;

/**
* Junit / Instrumentation test case for the camera api
 
 */  
public class CameraTest extends ActivityInstrumentationTestCase<MediaFrameworkTest> {    
private String TAG = "CameraTest";
    
private boolean rawPreviewCallbackResult = false;
private boolean shutterCallbackResult = false;
private boolean rawPictureCallbackResult = false;
private boolean jpegPictureCallbackResult = false;
    
private static int WAIT_FOR_COMMAND_TO_COMPLETE = 10000;  // Milliseconds.
    
private RawPreviewCallback mRawPreviewCallback = new RawPreviewCallback();
private TestShutterCallback mShutterCallback = new TestShutterCallback();
private RawPictureCallback mRawPictureCallback = new RawPictureCallback();
private JpegPictureCallback mJpegPictureCallback = new JpegPictureCallback();
    
private boolean mInitialized = false;
private Looper mLooper = null;
private final Object lock = new Object();
private final Object previewDone = new Object();
    
Camera mCamera;
Context mContext;
  
public CameraTest() {
super("com.android.mediaframeworktest", MediaFrameworkTest.class);
}

protected void setUp() throws Exception {
        super.setUp(); 
}
   
/*
     * Initializes the message looper so that the Camera object can 
* receive the callback messages.
*/
private void initializeMessageLooper() {
//Synthetic comment -- @@ -81,10 +82,10 @@
// Set up a looper to be used by camera.
Looper.prepare();
Log.v(TAG, "start loopRun");
                // Save the looper so that we can terminate this thread 
// after we are done with it.
                mLooper = Looper.myLooper();                
                mCamera = Camera.open();                                
synchronized (lock) {
mInitialized = true;
lock.notify();
//Synthetic comment -- @@ -94,7 +95,7 @@
}
}.start();
}
    
/*
* Terminates the message looper thread.
*/
//Synthetic comment -- @@ -108,11 +109,11 @@
}
mCamera.release();
}
    
//Implement the previewCallback
    private final class RawPreviewCallback implements PreviewCallback { 
        public void onPreviewFrame(byte [] rawData, Camera camera) {         
            Log.v(TAG, "Preview callback start");            
int rawDataLength = 0;
if (rawData != null) {
rawDataLength = rawData.length;
//Synthetic comment -- @@ -126,11 +127,11 @@
Log.v(TAG, "notify the preview callback");
previewDone.notify();
}
            
Log.v(TAG, "Preview callback stop");
}
};
    
//Implement the shutterCallback
private final class TestShutterCallback implements ShutterCallback {
public void onShutter() {
//Synthetic comment -- @@ -138,9 +139,9 @@
Log.v(TAG, "onShutter called");
}
};
    
//Implement the RawPictureCallback
    private final class RawPictureCallback implements PictureCallback { 
public void onPictureTaken(byte [] rawData, Camera camera) {
// no support for raw data - success if we get the callback
rawPictureCallbackResult = true;
//Synthetic comment -- @@ -152,16 +153,16 @@
Log.v(TAG, "RawPictureCallback callback");
}
};
    
//Implement the JpegPictureCallback
    private final class JpegPictureCallback implements PictureCallback {   
        public void onPictureTaken(byte [] rawData, Camera camera) {           
            try {         
if (rawData != null) {
int rawDataLength = rawData.length;
                    File rawoutput = new File("/sdcard/test.bmp");
FileOutputStream outstream = new FileOutputStream(rawoutput);
                    outstream.write(rawData);                   
Log.v(TAG, "JpegPictureCallback rawDataLength = " + rawDataLength);
jpegPictureCallbackResult = true;
} else {
//Synthetic comment -- @@ -173,9 +174,9 @@
}
}
};
   
    
    private void checkTakePicture() { 
SurfaceHolder mSurfaceHolder;
try {
mSurfaceHolder = MediaFrameworkTest.mSurfaceView.getHolder();
//Synthetic comment -- @@ -195,16 +196,16 @@
Thread.sleep(MediaNames.WAIT_LONG);
} catch (Exception e) {
Log.v(TAG, e.toString());
        }      
}
    
    private void checkPreviewCallback() { 
SurfaceHolder mSurfaceHolder;
try {
mSurfaceHolder = MediaFrameworkTest.mSurfaceView.getHolder();
mCamera.setPreviewDisplay(mSurfaceHolder);
Log.v(TAG, "start preview");
            mCamera.startPreview();                      
synchronized (previewDone) {
try {
previewDone.wait(WAIT_FOR_COMMAND_TO_COMPLETE);
//Synthetic comment -- @@ -216,19 +217,19 @@
mCamera.setPreviewCallback(null);
} catch (Exception e) {
Log.v(TAG, e.toString());
        }      
}
    
/*
     * TODO(yslau): Need to setup the golden rawData and compare the 
     * the new captured rawData with the golden one.       
     * 
* Test case 1: Take a picture and verify all the callback
* functions are called properly.
*/
// TODO: add this back to LargeTest once bug 2141755 is fixed
// @LargeTest
    public void testTakePicture() throws Exception {  
synchronized (lock) {
initializeMessageLooper();
try {
//Synthetic comment -- @@ -244,13 +245,13 @@
assertTrue("rawPictureCallbackResult", rawPictureCallbackResult);
assertTrue("jpegPictureCallbackResult", jpegPictureCallbackResult);
}
    
/*
     * Test case 2: Set the preview and 
* verify the RawPreviewCallback is called
*/
@LargeTest
    public void testCheckPreview() throws Exception {  
synchronized (lock) {
initializeMessageLooper();
try {
//Synthetic comment -- @@ -260,7 +261,7 @@
}
}
mCamera.setPreviewCallback(mRawPreviewCallback);
        checkPreviewCallback();     
terminateMessageLooper();
assertTrue("RawPreviewCallbackResult", rawPreviewCallbackResult);
}








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/MediaMimeTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/MediaMimeTest.java
//Synthetic comment -- index ddf5e0b..0fa6e4d 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
//Synthetic comment -- @@ -45,10 +46,11 @@
-w com.android.mediaframeworktest/.MediaFrameworkTestRunner
*
*/
public class MediaMimeTest extends ActivityInstrumentationTestCase2<MediaFrameworkTest> {    
private final String TAG = "MediaMimeTest";
private Context mContext;
    private final String MP3_FILE = "/sdcard/media_api/music/SHORTMP3.mp3";
private final String MEDIA_PLAYBACK_NAME = "com.android.music.MediaPlaybackActivity";

public MediaMimeTest() {
//Synthetic comment -- @@ -62,10 +64,10 @@
// Checks you have all the test files on your SDCARD.
assertTrue(new File(MP3_FILE).exists());
}
    
    @Override 
    protected void tearDown() throws Exception {     
        super.tearDown();              
}

// ----------------------------------------------------------------------
//Synthetic comment -- @@ -83,7 +85,7 @@
assertMediaPlaybackActivityHandles("audio/*");
}

    // TODO: temporarily remove from medium suite because it hangs whole suite 
// @MediumTest
// Checks the MediaPlaybackActivity handles application/itunes. Some servers
// set the Content-type header to application/iTunes (with capital T, but








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/MediaRecorderTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/MediaRecorderTest.java
//Synthetic comment -- index fdc5970..60942a3 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.test.ActivityInstrumentationTestCase;
import android.util.Log;
import android.view.SurfaceHolder;
//Synthetic comment -- @@ -36,14 +37,14 @@


/**
 * Junit / Instrumentation test case for the media recorder api 
 */  
public class MediaRecorderTest extends ActivityInstrumentationTestCase<MediaFrameworkTest> {    
private String TAG = "MediaRecorderTest";
private int mOutputDuration =0;
private int mOutputVideoWidth = 0;
private int mOutputVideoHeight= 0 ;
    
private SurfaceHolder mSurfaceHolder = null;
private MediaRecorder mRecorder;

//Synthetic comment -- @@ -51,19 +52,19 @@

Context mContext;
Camera mCamera;
  
public MediaRecorderTest() {
super("com.android.mediaframeworktest", MediaFrameworkTest.class);
       
}

protected void setUp() throws Exception {
        super.setUp(); 
Log.v(TAG,"create the media recorder");
mRecorder = new MediaRecorder();
}
 
    private void recordVideo(int frameRate, int width, int height, 
int videoFormat, int outFormat, String outFile, boolean videoOnly) {
Log.v(TAG,"startPreviewAndPrepareRecording");
try {
//Synthetic comment -- @@ -73,7 +74,7 @@
}
mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
mRecorder.setOutputFormat(outFormat);
            Log.v(TAG, "output format " + outFormat);          
mRecorder.setOutputFile(outFile);
mRecorder.setVideoFrameRate(frameRate);
mRecorder.setVideoSize(width, height);
//Synthetic comment -- @@ -98,7 +99,7 @@
mRecorder.release();
}
}
    
private boolean recordVideoWithPara(String encoder, String audio, String quality){
boolean recordSuccess = false;
int videoEncoder = MediaProfileReader.OUTPUT_FORMAT_TABLE.get(encoder);
//Synthetic comment -- @@ -117,7 +118,8 @@
videoFps = MIN_VIDEO_FPS;
}
mSurfaceHolder = MediaFrameworkTest.mSurfaceView.getHolder();
        String filename = ("/sdcard/" + encoder + "_" + audio + "_" + quality + ".3gp");
try {
Log.v(TAG, "video encoder :" + videoEncoder);
Log.v(TAG, "audio encoder :" + audioEncoder);
//Synthetic comment -- @@ -163,7 +165,7 @@
return recordSuccess;
}

    private boolean invalidRecordSetting(int frameRate, int width, int height, 
int videoFormat, int outFormat, String outFile, boolean videoOnly) {
try {
if (!videoOnly) {
//Synthetic comment -- @@ -172,7 +174,7 @@
}
mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
mRecorder.setOutputFormat(outFormat);
            Log.v(TAG, "output format " + outFormat);          
mRecorder.setOutputFile(outFile);
mRecorder.setVideoFrameRate(frameRate);
mRecorder.setVideoSize(width, height);
//Synthetic comment -- @@ -200,9 +202,9 @@
}
return false;
}
    
    
    
private void getOutputVideoProperty(String outputFilePath) {
MediaPlayer mediaPlayer = new MediaPlayer();
try {
//Synthetic comment -- @@ -219,18 +221,18 @@
mOutputVideoWidth = mediaPlayer.getVideoWidth();
//mOutputVideoHeight = CodecTest.videoHeight(outputFilePath);
//mOutputVideoWidth = CodecTest.videoWidth(outputFilePath);
            mediaPlayer.release();    
} catch (Exception e) {
Log.v(TAG, e.toString());
mediaPlayer.release();
        }       
}
    
private void removeFile(String filePath) {
File fileRemove = new File(filePath);
        fileRemove.delete();     
}
    
private boolean validateVideo(String filePath, int width, int height) {
boolean validVideo = false;
getOutputVideoProperty(filePath);
//Synthetic comment -- @@ -242,69 +244,69 @@
//removeFile(filePath);
return validVideo;
}
    
  
//Format: HVGA h263
@Suppress
    public void testHVGAH263() throws Exception {  
boolean videoRecordedResult = false;
        recordVideo(15, 480, 320, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_HVGA_H263, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_HVGA_H263, 480, 320);
assertTrue("HVGAH263", videoRecordedResult);
}
    
//Format: QVGA h263
@LargeTest
    public void testQVGAH263() throws Exception {  
boolean videoRecordedResult = false;
        recordVideo(15, 320, 240, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_QVGA_H263, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_QVGA_H263, 320, 240);
assertTrue("QVGAH263", videoRecordedResult);
}
    
//Format: SQVGA h263
@LargeTest
    public void testSQVGAH263() throws Exception {  
boolean videoRecordedResult = false;
        recordVideo(15, 240, 160, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_SQVGA_H263, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_SQVGA_H263, 240, 160);
assertTrue("SQVGAH263", videoRecordedResult);
}
    
//Format: QCIF h263
@LargeTest
public void testQCIFH263() throws Exception {
        boolean videoRecordedResult = false; 
        recordVideo(15, 176, 144, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_QCIF_H263, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_QCIF_H263, 176, 144);
assertTrue("QCIFH263", videoRecordedResult);
    }    
    
//Format: CIF h263
@LargeTest
    public void testCIFH263() throws Exception {       
boolean videoRecordedResult = false;
        recordVideo(15, 352, 288, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_CIF_H263, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_CIF_H263, 352, 288);
assertTrue("CIFH263", videoRecordedResult);
}
      
    
   
@LargeTest
    public void testVideoOnly() throws Exception {       
boolean videoRecordedResult = false;
        recordVideo(15, 176, 144, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.MPEG_4, MediaNames.RECORDED_VIDEO_3GP, true);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_VIDEO_3GP, 176, 144);
assertTrue("QCIFH263 Video Only", videoRecordedResult);
}
    
@LargeTest
/*
* This test case set the camera in portrait mode.
//Synthetic comment -- @@ -322,7 +324,7 @@
mRecorder.setCamera(mCamera);
Thread.sleep(1000);
recordVideo(15, 352, 288, MediaRecorder.VideoEncoder.H263,
                    MediaRecorder.OutputFormat.THREE_GPP, 
MediaNames.RECORDED_PORTRAIT_H263, true);
mCamera.lock();
mCamera.release();
//Synthetic comment -- @@ -333,97 +335,97 @@
}
assertTrue("PortraitH263", videoRecordedResult);
}
    
@Suppress
    public void testHVGAMP4() throws Exception {  
boolean videoRecordedResult = false;
        recordVideo(15, 480, 320, MediaRecorder.VideoEncoder.MPEG_4_SP, 
               MediaRecorder.OutputFormat.MPEG_4, MediaNames.RECORDED_HVGA_MP4, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_HVGA_MP4, 480, 320);
assertTrue("HVGAMP4", videoRecordedResult);
}
     
@LargeTest
    public void testQVGAMP4() throws Exception {  
boolean videoRecordedResult = false;
        recordVideo(15, 320, 240, MediaRecorder.VideoEncoder.MPEG_4_SP, 
               MediaRecorder.OutputFormat.MPEG_4, MediaNames.RECORDED_QVGA_MP4, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_QVGA_MP4, 320, 240);
assertTrue("QVGAMP4", videoRecordedResult);
}
    
@LargeTest
    public void testSQVGAMP4() throws Exception {  
boolean videoRecordedResult = false;
        recordVideo(15, 240, 160, MediaRecorder.VideoEncoder.MPEG_4_SP, 
               MediaRecorder.OutputFormat.MPEG_4, MediaNames.RECORDED_SQVGA_MP4, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_SQVGA_MP4, 240, 160);
assertTrue("SQVGAMP4", videoRecordedResult);
}
    
//Format: QCIF MP4
@LargeTest
    public void testQCIFMP4() throws Exception {       
boolean videoRecordedResult = false;
        recordVideo(15, 176, 144, MediaRecorder.VideoEncoder.MPEG_4_SP, 
               MediaRecorder.OutputFormat.MPEG_4, MediaNames.RECORDED_QCIF_MP4, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_QCIF_MP4, 176, 144);
assertTrue("QCIFMP4", videoRecordedResult);
    }    
    
    
//Format: CIF MP4
@LargeTest
    public void testCIFMP4() throws Exception {       
boolean videoRecordedResult = false;
        recordVideo(15, 352, 288, MediaRecorder.VideoEncoder.MPEG_4_SP, 
               MediaRecorder.OutputFormat.MPEG_4, MediaNames.RECORDED_CIF_MP4, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_CIF_MP4, 352, 288);
assertTrue("CIFMP4", videoRecordedResult);
}
    
    
//Format: CIF MP4 output format 3gpp
@LargeTest
    public void testCIFMP43GPP() throws Exception {       
boolean videoRecordedResult = false;
        recordVideo(15, 352, 288, MediaRecorder.VideoEncoder.MPEG_4_SP, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_VIDEO_3GP, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_VIDEO_3GP, 352, 288);
assertTrue("CIFMP4 3GPP", videoRecordedResult);
}
    
@LargeTest
    public void testQCIFH2633GPP() throws Exception {       
boolean videoRecordedResult = false;
        recordVideo(15, 176, 144, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_VIDEO_3GP, false);      
videoRecordedResult = validateVideo(MediaNames.RECORDED_VIDEO_3GP, 176, 144);
assertTrue("QCIFH263 3GPP", videoRecordedResult);
}
    
@LargeTest
    public void testInvalidVideoPath() throws Exception {       
boolean isTestInvalidVideoPathSuccessful = false;
        isTestInvalidVideoPathSuccessful = invalidRecordSetting(15, 176, 144, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.INVALD_VIDEO_PATH, false);      
assertTrue("Invalid outputFile Path", isTestInvalidVideoPathSuccessful);
}
    
@Suppress
    public void testInvalidVideoSize() throws Exception {       
boolean isTestInvalidVideoSizeSuccessful = false;
        isTestInvalidVideoSizeSuccessful = invalidRecordSetting(15, 800, 600, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_VIDEO_3GP, false);      
assertTrue("Invalid video Size", isTestInvalidVideoSizeSuccessful);
}

@Suppress
@LargeTest
    public void testInvalidFrameRate() throws Exception {       
boolean isTestInvalidFrameRateSuccessful = false;
        isTestInvalidFrameRateSuccessful = invalidRecordSetting(50, 176, 144, MediaRecorder.VideoEncoder.H263, 
               MediaRecorder.OutputFormat.THREE_GPP, MediaNames.RECORDED_VIDEO_3GP, false);      
assertTrue("Invalid FrameRate", isTestInvalidFrameRateSuccessful);
}









//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/performance/MediaPlayerPerformance.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/performance/MediaPlayerPerformance.java
//Synthetic comment -- index 88e171d..c03ffdd 100644

//Synthetic comment -- @@ -1,12 +1,12 @@
/*
* Copyright (C) 2008 The Android Open Source Project
 * 
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
 * 
* http://www.apache.org/licenses/LICENSE-2.0
 * 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
//Synthetic comment -- @@ -22,6 +22,7 @@
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
//Synthetic comment -- @@ -43,7 +44,7 @@
import com.android.mediaframeworktest.MediaProfileReader;

/**
 * Junit / Instrumentation - performance measurement for media player and 
* recorder
*/
public class MediaPlayerPerformance extends ActivityInstrumentationTestCase<MediaFrameworkTest> {
//Synthetic comment -- @@ -56,7 +57,7 @@
private static final int NUM_PLAYBACk_IN_EACH_LOOP = 20;
private static final long MEDIA_STRESS_WAIT_TIME = 5000; //5 seconds
private static final String MEDIA_MEMORY_OUTPUT =
        "/sdcard/mediaMemOutput.txt";

//the tolerant memory leak
private static final int MAX_ACCEPTED_MEMORY_LEAK_KB = 150;
//Synthetic comment -- @@ -76,8 +77,9 @@
}

public void createDB() {
        mDB = SQLiteDatabase.openOrCreateDatabase("/sdcard/perf.db", null);
        mDB.execSQL("CREATE TABLE IF NOT EXISTS perfdata (_id INTEGER PRIMARY KEY," + 
"file TEXT," + "setdatatime LONG," + "preparetime LONG," +
"playtime LONG" + ");");
//clean the table before adding new data








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/stress/MediaRecorderStressTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/stress/MediaRecorderStressTest.java
//Synthetic comment -- index b6a1bfa..eaaa46a 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
//Synthetic comment -- @@ -35,15 +36,15 @@

/**
* Junit / Instrumentation test case for the media player api
 
 */  
public class MediaRecorderStressTest extends ActivityInstrumentationTestCase2<MediaFrameworkTest> {    
    
  
private String TAG = "MediaRecorderStressTest";
private MediaRecorder mRecorder;
private Camera mCamera;
   
private static final int NUMBER_OF_CAMERA_STRESS_LOOPS = 100;
private static final int NUMBER_OF_RECORDER_STRESS_LOOPS = 100;
private static final int NUMBER_OF_RECORDERANDPLAY_STRESS_LOOPS = 50;
//Synthetic comment -- @@ -52,10 +53,11 @@
private static final long WAIT_TIME_RECORDER_TEST = 6000;  // 6 second
private static final long WAIT_TIME_RECORD = 10000;  // 10 seconds
private static final long WAIT_TIME_PLAYBACK = 6000;  // 6 second
    private static final String OUTPUT_FILE = "/sdcard/temp";
private static final String OUTPUT_FILE_EXT = ".3gp";
private static final String MEDIA_STRESS_OUTPUT =
        "/sdcard/mediaStressOutput.txt";
private Looper mCameraLooper = null;
private Looper mRecorderLooper = null;
private final Object lock = new Object();
//Synthetic comment -- @@ -70,7 +72,7 @@

protected void setUp() throws Exception {
getActivity();
        super.setUp();      
}

private final class CameraErrorCallback implements android.hardware.Camera.ErrorCallback {








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/unit/MediaRecorderStateUnitTestTemplate.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/unit/MediaRecorderStateUnitTestTemplate.java
//Synthetic comment -- index 9edc9aa..2c7a809 100644

//Synthetic comment -- @@ -18,25 +18,27 @@

import android.util.Log;
import android.media.MediaRecorder;
import android.test.AndroidTestCase;

/**
* A template class for running a method under test in all possible
* states of a MediaRecorder object.
 * 
* @see com.android.mediaframeworktest.unit.MediaRecorderStopStateUnitTest
* for an example of using this class.
 * 
 * A typical concrete unit test class would implement the 
* MediaRecorderMethodUnderTest interface and have a reference to an object of
* this class. Then it calls runTestOnMethod() to actually perform the unit
* tests. It is recommended that the toString() method of the concrete unit test
 * class be overridden to use the actual method name under test for logging 
* purpose.
 * 
*/
class MediaRecorderStateUnitTestTemplate extends AndroidTestCase {
    public static final String RECORD_OUTPUT_PATH = "/sdcard/recording.3gp";
public static final int OUTPUT_FORMAT= MediaRecorder.OutputFormat.THREE_GPP;
public static final int AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;
public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
//Synthetic comment -- @@ -45,11 +47,11 @@
private MediaRecorder mMediaRecorder = new MediaRecorder();
private MediaRecorderStateErrors.MediaRecorderState mMediaRecorderState = null;
private MediaRecorderMethodUnderTest mMethodUnderTest = null;
    
/**
* Runs the given method under test in all possible states of a MediaRecorder
* object.
     * 
* @param testMethod the method under test.
*/
public void runTestOnMethod(MediaRecorderMethodUnderTest testMethod) {
//Synthetic comment -- @@ -60,10 +62,10 @@
cleanUp();
}
}
    
/*
* Calls method under test in the given state of the MediaRecorder object.
     * 
* @param state the MediaRecorder state in which the method under test is called.
*/
private void callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState state) {
//Synthetic comment -- @@ -80,7 +82,7 @@
/*
* The following setMediaRecorderToXXXStateXXX methods sets the MediaRecorder
* object to the corresponding state, given the assumption that reset()
     * always resets the MediaRecorder object to Initial (after reset) state. 
*/
private void setMediaRecorderToInitialStateAfterReset() {
try {
//Synthetic comment -- @@ -94,7 +96,7 @@
// In the past, stop() == reset().
// However, this is no longer true. The plan is to have a STOPPED state.
// and from STOPPED state, start can be called without the need to
    // do the recording configuration again. 
private void setMediaRecorderToInitialStateAfterStop() {
try {
mMediaRecorder.reset();
//Synthetic comment -- @@ -111,7 +113,7 @@
fail("setMediaRecorderToInitialStateAfterReset: Exception " + e.getClass().getName() + " was thrown.");
}
}
    
private void setMediaRecorderToInitializedState() {
try {
mMediaRecorder.reset();
//Synthetic comment -- @@ -122,7 +124,7 @@
fail("setMediaRecorderToInitializedState: Exception " + e.getClass().getName() + " was thrown.");
}
}
    
private void setMediaRecorderToPreparedState() {
try {
mMediaRecorder.reset();
//Synthetic comment -- @@ -135,7 +137,7 @@
fail("setMediaRecorderToPreparedState: Exception " + e.getClass().getName() + " was thrown.");
}
}
    
private void setMediaRecorderToRecordingState() {
try {
mMediaRecorder.reset();
//Synthetic comment -- @@ -149,13 +151,13 @@
fail("setMediaRecorderToRecordingState: Exception " + e.getClass().getName() + " was thrown.");
}
}
    
private void setMediaRecorderToDataSourceConfiguredState() {
try {
mMediaRecorder.reset();
mMediaRecorder.setAudioSource(AUDIO_SOURCE);
mMediaRecorder.setOutputFormat(OUTPUT_FORMAT);
            
/* Skip setAudioEncoder() and setOutputFile() calls if
* the method under test is setAudioEncoder() since this
* method can only be called once even in the DATASOURCECONFIGURED state
//Synthetic comment -- @@ -163,7 +165,7 @@
if (mMethodUnderTest.toString() != "setAudioEncoder()") {
mMediaRecorder.setAudioEncoder(AUDIO_ENCODER);
}
            
if (mMethodUnderTest.toString() != "setOutputFile()") {
mMediaRecorder.setOutputFile(RECORD_OUTPUT_PATH);
}
//Synthetic comment -- @@ -171,7 +173,7 @@
fail("setMediaRecorderToDataSourceConfiguredState: Exception " + e.getClass().getName() + " was thrown.");
}
}
    
/*
* There are a lot of ways to force the MediaRecorder object to enter
* the Error state. We arbitrary choose one here.
//Synthetic comment -- @@ -179,25 +181,25 @@
private void setMediaRecorderToErrorState() {
try {
mMediaRecorder.reset();
            
/* Skip setAudioSource() if the method under test is setAudioEncoder()
* Because, otherwise, it is valid to call setAudioEncoder() after
             * start() since start() will fail, and then the mMediaRecorder 
* won't be set to the Error state
             */ 
if (mMethodUnderTest.toString() != "setAudioEncoder()") {
mMediaRecorder.setAudioSource(AUDIO_SOURCE);
}
            
/* Skip setOutputFormat if the method under test is setOutputFile()
*  Because, otherwise, it is valid to call setOutputFile() after
             * start() since start() will fail, and then the mMediaRecorder 
* won't be set to the Error state
             */ 
if (mMethodUnderTest.toString() != "setOutputFile()") {
mMediaRecorder.setOutputFormat(OUTPUT_FORMAT);
}
            
mMediaRecorder.start();
} catch(Exception e) {
if (!(e instanceof IllegalStateException)) {
//Synthetic comment -- @@ -206,10 +208,10 @@
}
Log.v(TAG, "setMediaRecorderToErrorState: done.");
}
    
/*
* Sets the state of the MediaRecorder object to the specified one.
     * 
* @param state the state of the MediaRecorder object.
*/
private void setMediaRecorderToState(MediaRecorderStateErrors.MediaRecorderState state) {
//Synthetic comment -- @@ -241,10 +243,10 @@
break;
}
}
    
/*
* Sets the error value of the corresponding state to the given error.
     * 
* @param state the state of the MediaRecorder object.
* @param error the value of the state error to be set.
*/
//Synthetic comment -- @@ -280,11 +282,11 @@
private void checkInitialState() {
callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState.INITIAL);
}
    
private void checkInitialStateAfterReset() {
callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState.INITIAL_AFTER_RESET);
}
    
private void checkInitialStateAfterStop() {
callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState.INITIAL_AFTER_STOP);
}
//Synthetic comment -- @@ -292,19 +294,19 @@
private void checkInitializedState() {
callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState.INITIALIZED);
}
    
private void checkPreparedState() {
callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState.PREPARED);
}
    
private void checkRecordingState() {
callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState.RECORDING);
}
    
private void checkDataSourceConfiguredState() {
callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState.DATASOURCECONFIGURED);
}
    
private void checkErrorState() {
callMediaRecorderMethodUnderTestInState(MediaRecorderStateErrors.MediaRecorderState.ERROR);
}
//Synthetic comment -- @@ -314,9 +316,9 @@
*/
private void checkMethodUnderTestInAllPossibleStates() {
// Must be called first.
        checkInitialState(); 
        
        // The sequence of the following method calls should not 
// affect the test results.
checkErrorState();
checkInitialStateAfterReset();
//Synthetic comment -- @@ -326,7 +328,7 @@
checkDataSourceConfiguredState();
checkPreparedState();
}
    
/*
* Cleans up all the internal object references.
*/







