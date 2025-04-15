/*replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:Icc7b1b6948fda1a5980a8407a3dc9b0b6ced79bb*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 3333268..61900ee 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
//Synthetic comment -- @@ -1054,7 +1055,7 @@

private void pruneDeadThumbnailFiles() {
HashSet<String> existingFiles = new HashSet<String>();
        String directory = Environment.getExternalStorageDirectory() + "/DCIM/.thumbnails";
String [] files = (new File(directory)).list();
if (files == null)
files = new String[0];








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/MediaFrameworkTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/MediaFrameworkTest.java
//Synthetic comment -- index 9fb49b1..def45ad 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Downloads;
import android.util.Log;
import android.util.Log;
//Synthetic comment -- @@ -53,7 +54,7 @@
private String urlpath;
private MediaPlayer mpmidi;
private MediaPlayer mpmp3;
    private String testfilepath = Environment.getExternalStorageDirectory() + "/awb.awb";

public static AssetFileDescriptor midiafd;
public static AssetFileDescriptor mp3afd;








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/MediaNames.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/MediaNames.java
//Synthetic comment -- index 9a48c92..1040a3f 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.mediaframeworktest;

import android.os.Environment;

/**
* 
* This class has the names of the all the activity name and variables 
//Synthetic comment -- @@ -23,18 +25,29 @@
*
*/
public class MediaNames {
    public static final String EXTERNAL_DIR =
        Environment.getExternalStorageDirectory().toString();
//A directory to hold all kinds of media files
    public static final String MEDIA_SAMPLE_POOL = EXTERNAL_DIR + "/media_api/samples/";
//Audio files
    public static final String MP3CBR =
        EXTERNAL_DIR + "/media_api/music/MP3_256kbps_2ch.mp3";
    public static final String MP3VBR =
        EXTERNAL_DIR + "/media_api/music/MP3_256kbps_2ch_VBR.mp3";
    public static final String SHORTMP3 =
        EXTERNAL_DIR + "/media_api/music/SHORTMP3.mp3";
    public static final String MIDI =
        EXTERNAL_DIR + "/media_api/music/ants.mid";
    public static final String WMA9 =
        EXTERNAL_DIR + "/media_api/music/WMA9.wma";
    public static final String WMA10 =
        EXTERNAL_DIR + "/media_api/music/WMA10.wma";
    public static final String WAV =
        EXTERNAL_DIR + "/media_api/music/rings_2ch.wav";
    public static final String AMR =
        EXTERNAL_DIR + "/media_api/music/test_amr_ietf.amr";
    public static final String OGG =
        EXTERNAL_DIR + "/media_api/music/Revelation.ogg";

public static final int MP3CBR_LENGTH = 71000;
public static final int MP3VBR_LENGTH = 71000;
//Synthetic comment -- @@ -51,7 +64,7 @@
public static final long WAIT_SNAPSHOT_TIME = 5000;

//Streaming Video
    public static final String VIDEO_HTTP3GP = "http://pvs.pv.com/jj/lipsync0.3gp";
public static final String VIDEO_RTSP3GP = "rtsp://63.241.31.203/public/jj/md.3gp";
public static final String VIDEO_RTSP3GP2 = "rtsp://pvs.pv.com/public/live_dvd1.3gp";
public static final String VIDEO_RTSP3GP3 = 
//Synthetic comment -- @@ -60,20 +73,32 @@
//public static final String VIDEO_RTSP3GP = "rtsp://193.159.241.21/sp/alizee05.3gp";

//local video
    public static final String VIDEO_MP4 =
        EXTERNAL_DIR + "/media_api/video/MPEG4_320_AAC_64.mp4";
    public static final String VIDEO_LONG_3GP =
        EXTERNAL_DIR + "/media_api/video/radiohead.3gp";
    public static final String VIDEO_SHORT_3GP =
        EXTERNAL_DIR + "/media_api/video/short.3gp";
    public static final String VIDEO_LARGE_SIZE_3GP =
        EXTERNAL_DIR + "/media_api/video/border_large.3gp";
    public static final String VIDEO_H263_AAC =
        EXTERNAL_DIR + "/media_api/video/H263_56_AAC_24.3gp";
    public static final String VIDEO_H263_AMR =
        EXTERNAL_DIR + "/media_api/video/H263_56_AMRNB_6.3gp";
    public static final String VIDEO_H264_AAC =
        EXTERNAL_DIR + "/media_api/video/H264_320_AAC_64.3gp";
    public static final String VIDEO_H264_AMR =
        EXTERNAL_DIR + "/media_api/video/H264_320_AMRNB_6.3gp";
    public static final String VIDEO_WMV =
        EXTERNAL_DIR + "/media_api/video/bugs.wmv";
    public static final String VIDEO_HIGHRES_H263 =
        EXTERNAL_DIR + "/media_api/video/H263_500_AMRNB_12.3gp";
    public static final String VIDEO_HIGHRES_MP4 =
        EXTERNAL_DIR + "/media_api/video/H264_500_AAC_128.3gp";

//ringtone
    public static final String ringtone =
        EXTERNAL_DIR + "/media_api/ringtones/F1_NewVoicemail.mp3";

//streaming mp3
public static final String STREAM_MP3_1 = 
//Synthetic comment -- @@ -102,267 +127,331 @@
"http://wms.pv.com:7070/MediaDownloadContent/UserUploads/beefcake.mp3";

//Sonivox
    public static String MIDIFILES[] = {
        EXTERNAL_DIR + "/media_api/music/Leadsol.mxmf",
        EXTERNAL_DIR + "/media_api/music/abba.imy",
        EXTERNAL_DIR + "/media_api/music/ants.mid",
        EXTERNAL_DIR + "/media_api/music/greensleeves.rtttl",
        EXTERNAL_DIR + "/media_api/music/test.ota"};

//Performance measurement
    public static String[] WAVFILES = {
        EXTERNAL_DIR + "/media_api/music_perf/WAV/M1F1-AlawWE-AFsp.wav",
        EXTERNAL_DIR + "/media_api/music_perf/WAV/M1F1-float64-AFsp.wav",
        EXTERNAL_DIR + "/media_api/music_perf/WAV/song.wav",
        EXTERNAL_DIR + "/media_api/music_perf/WAV/WAVEtest.wav",
        EXTERNAL_DIR + "/media_api/music_perf/WAV/WAVEtest_out.wav",
        EXTERNAL_DIR + "/media_api/music_perf/WAV/test_out.wav"};

public static String[] AMRNBFILES = { 
        EXTERNAL_DIR + "/media_api/music_perf/AMR/AI_AMR-NB_5.9kbps_6.24kbps_8khz_mono_NMC.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMR/AI_AMR-NB_5.15kbps_5.46kbps_8khz_mono_NMC.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMR/AI_AMR-NB_7.4kbps_7.80kbps_8khz_mono_NMC.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMR/AI_AMR-NB_7.95kbps_9.6kbps_8khz_mono_NMC.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMR/AI_AMR-NB_10.2kbps_10.48kbps_8khz_mono_NMC.amr"};

public static String[] AMRWBFILES = { 
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/NIN_AMR-WB_15.85kbps_16kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/NIN_AMR-WB_18.25kbps_18kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/NIN_AMR-WB_19.85kbps_20kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/NIN_AMR-WB_23.05kbps_23kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/NIN_AMR-WB_23.85kbps_24kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/PD_AMR-WB_19.85kbps_20kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/PD_AMR-WB_23.05kbps_23kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/PD_AMR-WB_23.85kbps_24kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/WC_AMR-WB_23.05kbps_23kbps.amr",
        EXTERNAL_DIR + "/media_api/music_perf/AMRWB/WC_AMR-WB_23.85kbps_24kbps.amr", };

public static String[] MP3FILES = { 
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_56kbps_32khz_stereo_VBR_MCA.MP3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_80kbps_32khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_80kbps_44.1khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_80kbps_48khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_112kbps_32khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_112kbps_44.1khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_112kbps_48khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_192kbps_32khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_192kbps_44.1khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_192kbps_48khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_256kbps_44.1khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/NIN_256kbps_48khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/PD_112kbps_32khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/PD_112kbps_44.1khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/PD_112kbps_48khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/PD_192kbps_32khz_mono_CBR_DPA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/PD_256kbps_44.1khz_mono_CBR_DPA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/PD_256kbps_48khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/WC_256kbps_44.1khz_mono_CBR_DPA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/MP3/WC_256kbps_48khz_mono_CBR_DPA.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/Apologize.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/Because_Of_You.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/Complicated.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/Glamorous.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/Im_With_You.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/Smile.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/Suddenly_I_See.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/When You Say Nothing At All.mp3",
        EXTERNAL_DIR + "/media_api/music_perf/regular_album_photo/my_happy_ending.mp3"};

public static String[] AACFILES = { 
        EXTERNAL_DIR + "/media_api/music_perf/AAC/AI_AAC_24kbps_12khz_Mono_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/AI_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/AI_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/AI_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/AI_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/AI_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/NIN_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/NIN_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/NIN_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/NIN_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/NIN_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PD_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PD_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PD_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PD_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PD_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PV_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PV_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PV_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PV_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/PV_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/WC_AAC_56kbps_22.05khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/WC_AAC_56kbps_32khz_Stereo_CBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/WC_AAC_56kbps_44.1khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/WC_AAC_80kbps_32khz_Stereo_1pCBR_SSE.mp4",
        EXTERNAL_DIR + "/media_api/music_perf/AAC/WC_AAC_80kbps_32khz_Stereo_CBR_SSE.mp4",
};

    public static String[] VIDEOFILES = {
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "AI_CTO_Mpeg4_32kbps_10fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "AI_CTO_Mpeg4_32kbps_12fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "AI_CTO_Mpeg4_32kbps_15fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "AI_CTO_Mpeg4_32kbps_5fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "AI_CTO_Mpeg4_32kbps_5fps_SQCIF_128x96+AAC_8kbps_8khz_mono_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "AI_CTO_Mpeg4_32kbps_7.5fps_SQCIF_128x96+AAC_8kbps_8khz_mono_QTE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "AI_WMV_1024kbps_20fps_QCIF_176x144_noaudio_SSE.wmv",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "AI_WMV_1024kbps_25fps_QCIF_176x144_noaudio_SSE.wmv",
        EXTERNAL_DIR + "/media_api/video_perf/Chicken.wmv",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "MP_qcif_15fps_100kbps_48kHz_192kbps_30secs.wmv",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_H264_123kbps_5fps_QCIF_176x144+AMR_12.2kbps_8khz_mono_QTE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_H264_96kbps_10.2fps_QCIF_176x144+AMR_12.2kbps_8khz_mono_QTE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_H264_96kbps_12fps_QCIF_176x144+AMR_12.2kbps_8khz_mono_QTE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_H264_96kbps_15fps_QCIF_176x144+AMR_12.2kbps_8khz_mono_QTE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_Mpeg4_123kbps_15fps_QCIF_176x144+AAC_32kbps_22khz_mono_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_Mpeg4_123kbps_7.5fps_QCIF_176x144+AAC_32kbps_22khz_stereo_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_Mpeg4_128kbps_10fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_Mpeg4_128kbps_12fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_Mpeg4_128kbps_15fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_Mpeg4_128kbps_5fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_CTO_Mpeg4_128kbps_7.5fps_QCIF_176x144+AAC+_32kbps_48khz_stereo_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_H263_128kbps_10fps_QCIF_174x144_noaudio_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_H263_128kbps_15fps_QCIF_174x144_noaudio_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_H263_48kbps_10fps_QCIF_174x144_noaudio_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_H263_48kbps_12fps_QCIF_174x144_noaudio_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_H264_123kbps_15fps_QCIF_176x144+AAC_32kbps_22khz_stereo_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "NIN_H264_123kbps_7.5fps_QCIF_176x144+AAC_32kbps_22khz_stereo_SSE.3gp",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "PV_H264_2000kbps_20fps_CIF_352x288+AAC_96kbps_48khz_stereo_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "PV_H264_2000kbps_25fps_CIF_352x288+AAC_96kbps_48khz_stereo_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "PV_H264_2000kbps_30fps_CIF_352x288+AAC_128kbps_48khz_stereo_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/Stevie-1.wmv",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "WC_H264_1600kbps_20fps_QCIF_176x144+AAC_96kbps_48khz_mono_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "WC_H264_1600kbps_25fps_QCIF_176x144+AAC_96kbps_48khz_mono_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/" +
            "WC_H264_1600kbps_30fps_QCIF_176x144+AAC_96kbps_48khz_mono_SSE.mp4",
        EXTERNAL_DIR + "/media_api/video_perf/bugs.wmv",
        EXTERNAL_DIR + "/media_api/video_perf/niceday.wmv",
        EXTERNAL_DIR + "/media_api/video_perf/eaglesatopnflpe.wmv",
};

//wma - only support up to wma 9
public static String[] WMASUPPORTED = {
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/AI_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/AI_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/NIN_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/NIN_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/PD_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/PD_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/PV_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/PV_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/WC_WMA9.2_32kbps_44.1khz_mono_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMASUPPORTED/WC_WMA9.2_48kbps_44.1khz_mono_CBR_DPA.wma"

};

    public static String[] WMAUNSUPPORTED = {
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_127kbps_48khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_128kbps_44.1khz_stereo_2pVBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_128kbps_48khz_stereo_2pVBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_128kbps_88khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_128kbps_96khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_192kbps_44.1khz_stereo_2pVBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_192kbps_88khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_192kbps_96khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_256kbps_44khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_256kbps_48khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_256kbps_88khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_256kbps_96khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
        "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_384kbps_44khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_384kbps_48khz_stereo_CBR_DPA.wma",
        EXTERNAL_DIR +
            "/media_api/music_perf/WMAUNSUPPORTED/AI_WMA10_384kbps_88khz_stereo_CBR_DPA.wma"
};

//Media Recorder
    public static final String RECORDER_OUTPUT =
        EXTERNAL_DIR + "/media_api/recorderOutput.amr";

//video thumbnail
    public static final String THUMBNAIL_OUTPUT =
        EXTERNAL_DIR + "/media_api/videoThumbnail.png";
    public static final String GOLDEN_THUMBNAIL_OUTPUT =
        EXTERNAL_DIR + "/media_api/goldenThumbnail.png";
    public static final String GOLDEN_THUMBNAIL_OUTPUT_2 =
        EXTERNAL_DIR + "/media_api/goldenThumbnail2.png";

//Metadata Utility
public static final String[] THUMBNAIL_CAPTURE_TEST_FILES = {
        EXTERNAL_DIR + "/media_api/metadata/test.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test1.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test2.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test3.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test4.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test5.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test6.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test7.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test8.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test9.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test10.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test11.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test12.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test13.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test14.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test15.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test16.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test17.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test18.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test19.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test20.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test21.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test22.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test23.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test24.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test25.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test26.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test27.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test28.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test29.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test30.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test31.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test32.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test33.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test35.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test36.m4v",
        EXTERNAL_DIR + "/media_api/metadata/test34.wmv",
        EXTERNAL_DIR + "/media_api/metadata/test_metadata.mp4",
    };

    public static final String[] METADATA_RETRIEVAL_TEST_FILES = {
        // Raw AAC is not supported
        // EXTERNAL_DIR + "/media_api/test_raw.aac",
        // EXTERNAL_DIR + "/media_api/test_adts.aac",
        // EXTERNAL_DIR + "/media_api/test_adif.aac",
        EXTERNAL_DIR + "/media_api/metadata/test_metadata.mp4",
        EXTERNAL_DIR + "/media_api/metadata/WMA10.wma",
        EXTERNAL_DIR + "/media_api/metadata/Leadsol_out.wav",
        EXTERNAL_DIR + "/media_api/metadata/test_aac.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test_amr.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test_avc_amr.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test_metadata.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test_vbr.mp3",
        EXTERNAL_DIR + "/media_api/metadata/test_cbr.mp3",
        EXTERNAL_DIR + "/media_api/metadata/metadata_test1.mp3",
        EXTERNAL_DIR + "/media_api/metadata/test33.3gp",
        EXTERNAL_DIR + "/media_api/metadata/test35.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test36.m4v",
        EXTERNAL_DIR + "/media_api/metadata/test_m4v_amr.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test_h263_amr.mp4",
        EXTERNAL_DIR + "/media_api/metadata/test34.wmv",
    };

    public static final String[] ALBUMART_TEST_FILES = {
        EXTERNAL_DIR + "/media_api/album_photo/test_22_16_mp3.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/PD_256kbps_48khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/PD_256kbps_44.1khz_mono_CBR_DPA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/PD_192kbps_32khz_mono_CBR_DPA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/NIN_256kbps_48khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/NIN_256kbps_44.1khz_mono_CBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/NIN_112kbps(96kbps)_48khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/NIN_112kbps(96kbps)_44.1khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/lightGreen1.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/babyBlue2 1.mp3",
        EXTERNAL_DIR +
            "/media_api/album_photo/2-01 01 NIN_56kbps(64kbps)_32khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/02_NIN_112kbps(80kbps)_32khz_stereo_VBR_MCA.mp3",
        EXTERNAL_DIR + "/media_api/album_photo/No_Woman_No_Cry_128K.wma",
        EXTERNAL_DIR + "/media_api/album_photo/Beethoven_2.wma",
    };

  // TEST_PATH_1: is a video and contains metadata for key "num-tracks"
// TEST_PATH_2: any valid media file.
// TEST_PATH_3: invalid media file
  public static final String TEST_PATH_1 = EXTERNAL_DIR + "/media_api/metadata/test.mp4";
  public static final String TEST_PATH_3 = EXTERNAL_DIR + "/media_api/data.txt";
public static final String TEST_PATH_4 = "somenonexistingpathname";
public static final String TEST_PATH_5 = "mem://012345";

//Synthetic comment -- @@ -371,104 +460,125 @@
//cd_track_number, album, artist, author, composer, date, genre
//title, years, duration
public static final String META_DATA_MP3 [][] = {
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/ID3V1_ID3V2.mp3",
          "1/10", "ID3V2.3 Album", "ID3V2.3 Artist",
"ID3V2.3 Lyricist", "ID3V2.3 Composer", null, "Blues",
"ID3V2.3 Title", "1234", "295", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/ID3V2.mp3",
          "1/10", "ID3V2.3 Album", "ID3V2.3 Artist",
"ID3V2.3 Lyricist", "ID3V2.3 Composer", null, "Blues", 
"ID3V2.3 Title", "1234", "287", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/ID3V1.mp3",
          "1", "test ID3V1 Album", "test ID3V1 Artist",
null, null, null, "255", "test ID3V1 Title", "1234", "231332", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V1.mp3",
              null, null, null, null, null, null, null, null, null, "231330", "1", null},
//The corrupted TALB field in id3v2 would not switch to id3v1 tag automatically
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TALB.mp3",
          "01", null, "ID3V2.3 Artist",
"ID3V2.3 Lyricist", "ID3V2.3 Composer", null, 
"Blues", "ID3V2.3 Title", "1234", "295", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TCOM.mp3",
           "01", "ID3V2.3 Album",
"ID3V2.3 Artist", "ID3V2.3 Lyricist", null, null, 
"Blues", "ID3V2.3 Title", "1234", "295", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TCOM_2.mp3",
           "01", "ID3V2.3 Album",
           "ID3V2.3 Artist", null, null, null, "Blues",
           "ID3V2.3 Title", "1234", "295", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TRCK.mp3",
           "dd", "ID3V2.3 Album",
"ID3V2.3 Artist", "ID3V2.3 Lyricist", "ID3V2.3 Composer", null,
"Blues", "ID3V2.3 Title", "1234", "295", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TRCK_2.mp3",
           "01", "ID3V2.3 Album",
           "ID3V2.3 Artist", null, null, null, null,
           "ID3V2.3 Title", null, "295", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TYER.mp3",
           "01", "ID3V2.3 Album",
           "ID3V2.3 Artist", null, null, null, null, "ID3V2.3 Title",
           "9999", "295", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TYER_2.mp3",
           "01", "ID3V2.3 Album",
"ID3V2.3 Artist", "ID3V2.3 Lyricist", "ID3V2.3 Composer", null, 
"Blues", "ID3V2.3 Title", null, "295", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP3/Corrupted_ID3V2_TIT.mp3",
          null, null, null, null, null, null, null, null, null, "295", "1", null}
};

public static final String META_DATA_OTHERS [][] = {
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/3GP/cat.3gp",
          null, null, null,
null, null, "20080309T002415.000Z", null,
null, null, "63916", "2", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/AMR/AMR_NB.amr",
          null, null, null,
null, null, null, null,
null, null, "126540", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/AMRWB/AMR_WB.amr",
          null, null, null,
null, null, null, null,
null, null, "231180", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/M4A/Jaws Of Life_ver1.m4a",
          "1/8", "Suspended Animation",
"John Petrucci", null, null, "20070510T125223.000Z", 
"12", "Jaws Of Life", "2005", "449329", "1", "m4a composer"},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/M4V/sample_iPod.m4v", null, null,
          null, null, null, "20051220T202015.000Z",
null, null, null, "85500", "2", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MIDI/MIDI.mid",
          null, "Suspended Animation",
          "John Petrucci", null, null, "20070510T125223.000Z",
null, null, "2005", "231180", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/MP4/kung_fu_panda_h264.mp4",
          "2/0", "mp4 album Kung Fu Panda",
          "mp4 artist Kung Fu Panda", null, null, "20080517T091451.000Z",
"40", "Kung Fu Panda", "2008", "128521", "2", "mp4 composer"},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/OGG/Ring_Classic_02.ogg",
          null, "Suspended Animation",
          "John Petrucci", null, null, "20070510T125223.000Z",
null, null, "2005", "231180", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/OGG/When You Say Nothing At All.ogg",
          null, "Suspended Animation", "John Petrucci",
null, null, "20070510T125223.000Z", null, null, "2005", "231180", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/WAV/Im With You.wav",
          null, null, null, null, null, null,
null, null, null, "224000", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/WMA/WMA9.wma",
          "6", "Ten Songs in the Key of Betrayal",
          "Alien Crime Syndicate", "Alien Crime Syndicate",
          "wma 9 Composer", "20040521T175729.483Z",
"Rock", "Run for the Money", "2004", "134479", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/WMA/WMA10.wma", "09", "wma 10 Album",
          "wma 10 Album Artist", "wma 10 Artist", "wma 10 Composer", "20070705T063625.097Z",
"Acid Jazz", "wma 10 Title", "2010", "126574", "1", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/WMV/bugs.wmv", "8", "wmv 9 Album",
          null, "wmv 9 Artist ", null, "20051122T155247.540Z",
null, "Looney Tunes - Hare-Breadth Hurry", "2005", "193482", "2", null},
      {EXTERNAL_DIR + "/media_api/metaDataTestMedias/WMV/clips_ver7.wmv", "50", "wmv 7 Album",
          null, "Hallau Shoots & Company", null, "20020226T170045.891Z",
null, "CODEC Shootout", "1986", "43709", "2", null}
};

//output recorded video

  public static final String RECORDED_HVGA_H263 = EXTERNAL_DIR + "/HVGA_H263.3gp";
  public static final String RECORDED_QVGA_H263 = EXTERNAL_DIR + "/QVGA_H263.3gp";
  public static final String RECORDED_SQVGA_H263 = EXTERNAL_DIR + "/SQVGA_H263.3gp";
  public static final String RECORDED_CIF_H263 = EXTERNAL_DIR + "/CIF_H263.3gp";
  public static final String RECORDED_QCIF_H263 = EXTERNAL_DIR + "/QCIF_H263.3gp";
  public static final String RECORDED_PORTRAIT_H263 = EXTERNAL_DIR + "/QCIF_mp4.3gp";

  public static final String RECORDED_HVGA_MP4 = EXTERNAL_DIR + "/HVGA_mp4.mp4";
  public static final String RECORDED_QVGA_MP4 = EXTERNAL_DIR + "/QVGA_mp4.mp4";
  public static final String RECORDED_SQVGA_MP4 = EXTERNAL_DIR + "/SQVGA_mp4.mp4";
  public static final String RECORDED_CIF_MP4 = EXTERNAL_DIR + "/CIF_mp4.mp4";
  public static final String RECORDED_QCIF_MP4 = EXTERNAL_DIR + "/QCIF_mp4.mp4";

  public static final String RECORDED_VIDEO_3GP = EXTERNAL_DIR + "/temp.3gp";

  public static final String INVALD_VIDEO_PATH =
      EXTERNAL_DIR + "/media_api/filepathdoesnotexist/filepathdoesnotexist/temp.3gp";


public static final long RECORDED_TIME = 5000;
//Synthetic comment -- @@ -476,17 +586,17 @@

//Videos for the mediaplayer stress test
public static String[] H263_STRESS = { 
      EXTERNAL_DIR + "/media_api/video_stress/h263/H263_CIF.3gp",
      EXTERNAL_DIR + "/media_api/video_stress/h263/H263_QCIF.3gp",
      EXTERNAL_DIR + "/media_api/video_stress/h263/H263_QVGA.3gp",
      EXTERNAL_DIR + "/media_api/video_stress/h263/H263_SQVGA.3gp"
};

public static String[] MPEG4_STRESS = { 
    EXTERNAL_DIR + "/media_api/video_stress/h263/mpeg4_CIF.mp4",
    EXTERNAL_DIR + "/media_api/video_stress/h263/mpeg4_QCIF.3gp",
    EXTERNAL_DIR + "/media_api/video_stress/h263/mpeg4_QVGA.3gp",
    EXTERNAL_DIR + "/media_api/video_stress/h263/mpeg4_SQVGA.mp4"
};

//Streaming test files








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/CameraTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/CameraTest.java
//Synthetic comment -- index 2e599f2..06718d1 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.view.SurfaceHolder;

import android.os.ConditionVariable;
import android.os.Environment;
import android.os.Looper;

import android.test.suitebuilder.annotation.LargeTest;
//Synthetic comment -- @@ -153,7 +154,8 @@
try {         
if (rawData != null) {
int rawDataLength = rawData.length;
                    File rawoutput =
                        new File(Environment.getExternalStorageDirectory(), "test.bmp");
FileOutputStream outstream = new FileOutputStream(rawoutput);
outstream.write(rawData);                   
Log.v(TAG, "JpegPictureCallback rawDataLength = " + rawDataLength);








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/MediaMimeTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/MediaMimeTest.java
//Synthetic comment -- index 728e68d..2f8d505 100644

//Synthetic comment -- @@ -20,15 +20,13 @@

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.Suppress;
import com.android.mediaframeworktest.MediaFrameworkTest;

/*
//Synthetic comment -- @@ -48,7 +46,8 @@
public class MediaMimeTest extends ActivityInstrumentationTestCase2<MediaFrameworkTest> {    
private final String TAG = "MediaMimeTest";
private Context mContext;
    private final String MP3_FILE =
        Environment.getExternalStorageDirectory() + "/media_api/music/SHORTMP3.mp3";

public MediaMimeTest() {
super("com.android.mediaframeworktest", MediaFrameworkTest.class);








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/MediaRecorderTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/functional/MediaRecorderTest.java
//Synthetic comment -- index a52fd76..6b813a3 100644

//Synthetic comment -- @@ -25,13 +25,12 @@
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.EncoderCapabilities.VideoEncoderCap;
import android.media.EncoderCapabilities.AudioEncoderCap;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase;
import android.util.Log;
import android.view.SurfaceHolder;
import com.android.mediaframeworktest.MediaProfileReader;

import android.test.suitebuilder.annotation.LargeTest;
//Synthetic comment -- @@ -119,7 +118,8 @@
videoFps = MIN_VIDEO_FPS;
}
mSurfaceHolder = MediaFrameworkTest.mSurfaceView.getHolder();
        String filename = (Environment.getExternalStorageDirectory() + "/" + videoEncoder +
                "_" + audioEncoder + "_" + highQuality + ".3gp");
try {
Log.v(TAG, "video encoder :" + videoEncoder);
Log.v(TAG, "audio encoder :" + audioEncoder);








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/performance/MediaPlayerPerformance.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/performance/MediaPlayerPerformance.java
//Synthetic comment -- index a0ef905..66bd8ec 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase;
//Synthetic comment -- @@ -35,7 +36,6 @@

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
//Synthetic comment -- @@ -46,8 +46,6 @@
import android.media.MediaMetadataRetriever;
import com.android.mediaframeworktest.MediaProfileReader;

/**
* Junit / Instrumentation - performance measurement for media player and 
* recorder
//Synthetic comment -- @@ -61,8 +59,10 @@
private static final int NUM_STRESS_LOOP = 10;
private static final int NUM_PLAYBACk_IN_EACH_LOOP = 20;
private static final long MEDIA_STRESS_WAIT_TIME = 5000; //5 seconds
    private static final String EXTERNAL_DIR =
        Environment.getExternalStorageDirectory().toString();
private static final String MEDIA_MEMORY_OUTPUT =
        EXTERNAL_DIR + "/mediaMemOutput.txt";

private static int mStartMemory = 0;
private static int mEndMemory = 0;
//Synthetic comment -- @@ -90,7 +90,7 @@
}

public void createDB() {
        mDB = SQLiteDatabase.openOrCreateDatabase(EXTERNAL_DIR + "/perf.db", null);
mDB.execSQL("CREATE TABLE IF NOT EXISTS perfdata (_id INTEGER PRIMARY KEY," + 
"file TEXT," + "setdatatime LONG," + "preparetime LONG," +
"playtime LONG" + ");");








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/stress/MediaRecorderStressTest.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/stress/MediaRecorderStressTest.java
//Synthetic comment -- index e442c85..63588f8 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
//Synthetic comment -- @@ -36,15 +37,15 @@

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
//Synthetic comment -- @@ -53,10 +54,11 @@
private static final long WAIT_TIME_RECORDER_TEST = 6000;  // 6 second
private static final long WAIT_TIME_RECORD = 10000;  // 10 seconds
private static final long WAIT_TIME_PLAYBACK = 6000;  // 6 second
    private static final String OUTPUT_FILE =
        Environment.getExternalStorageDirectory() + "/temp";
private static final String OUTPUT_FILE_EXT = ".3gp";
private static final String MEDIA_STRESS_OUTPUT =
        Environment.getExternalStorageDirectory() + "/mediaStressOutput.txt";
private Looper mCameraLooper = null;
private Looper mRecorderLooper = null;
private final Object lock = new Object();
//Synthetic comment -- @@ -71,7 +73,7 @@

protected void setUp() throws Exception {
getActivity();
        super.setUp();
}

private final class CameraErrorCallback implements android.hardware.Camera.ErrorCallback {








//Synthetic comment -- diff --git a/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/unit/MediaRecorderStateUnitTestTemplate.java b/media/tests/MediaFrameworkTest/src/com/android/mediaframeworktest/unit/MediaRecorderStateUnitTestTemplate.java
//Synthetic comment -- index 9edc9aa..b9339b9 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.util.Log;
import android.media.MediaRecorder;
import android.os.Environment;
import android.test.AndroidTestCase;

/**
//Synthetic comment -- @@ -36,7 +37,8 @@
* 
*/
class MediaRecorderStateUnitTestTemplate extends AndroidTestCase {
    public static final String RECORD_OUTPUT_PATH =
        Environment.getExternalStorageDirectory() + "/recording.3gp";
public static final int OUTPUT_FORMAT= MediaRecorder.OutputFormat.THREE_GPP;
public static final int AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;
public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;







