/*fixed: when camera number is zero, this two case will fail.

Change-Id:Icd17ffa17d228b3bf09bb8cb554e75706cf35073*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/Camera_SizeTest.java b/tests/tests/hardware/src/android/hardware/cts/Camera_SizeTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 028b819..46b2941

//Synthetic comment -- @@ -44,11 +44,14 @@
)
public void testConstructor() {
Camera camera = Camera.open();
        
        if(camera !=null){
            Parameters parameters = camera.getParameters();
    
            checkSize(parameters, WIDTH1, HEIGHT1);
            checkSize(parameters, WIDTH2, HEIGHT2);
            checkSize(parameters, WIDTH3, HEIGHT3);
        }
}

private void checkSize(Parameters parameters, int width, int height) {








//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/CamcorderProfileTest.java b/tests/tests/media/src/android/media/cts/CamcorderProfileTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index b94da11..0fa0578

//Synthetic comment -- @@ -70,10 +70,14 @@
)
})
public void testGet() {
        int nCamera = Camera.getNumberOfCameras();
        
        if(nCamera>0){
            CamcorderProfile lowProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
            CamcorderProfile highProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            checkProfile(lowProfile);
            checkProfile(highProfile);
        }
}

@TestTargets({







