/*Avoid using String.format in MediaRecorder

String.format was used instead of a simple string concatenation.
This is a problem when language is set to Arabic since simple
integers will be converted into Arabic numbers.

Change-Id:I2cbd4c5cd2d09117202e6ae191845fd5fc9154ec*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaRecorder.java b/media/java/android/media/MediaRecorder.java
//Synthetic comment -- index ecabae8..9e97e92 100644

//Synthetic comment -- @@ -306,7 +306,7 @@
degrees != 270) {
throw new IllegalArgumentException("Unsupported angle: " + degrees);
}
        setParameter("video-param-rotation-angle-degrees=" + degrees);
}

/**
//Synthetic comment -- @@ -425,7 +425,7 @@
if (samplingRate <= 0) {
throw new IllegalArgumentException("Audio sampling rate is not positive");
}
        setParameter("audio-param-sampling-rate=" + samplingRate);
}

/**
//Synthetic comment -- @@ -440,7 +440,7 @@
if (numChannels <= 0) {
throw new IllegalArgumentException("Number of channels is not positive");
}
        setParameter("audio-param-number-of-channels=" + numChannels);
}

/**
//Synthetic comment -- @@ -456,7 +456,7 @@
if (bitRate <= 0) {
throw new IllegalArgumentException("Audio encoding bit rate is not positive");
}
        setParameter("audio-param-encoding-bitrate=" + bitRate);
}

/**
//Synthetic comment -- @@ -472,7 +472,7 @@
if (bitRate <= 0) {
throw new IllegalArgumentException("Video encoding bit rate is not positive");
}
        setParameter("video-param-encoding-bitrate=" + bitRate);
}

/**







