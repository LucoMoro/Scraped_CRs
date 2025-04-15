/*When saving http uri locally download directory may not exist

Change-Id:I31cfbea0b1bc6ff73cf4a0f57fa6f92d2a6ba16f*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/CropImage.java b/src/com/cooliris/media/CropImage.java
//Synthetic comment -- index 4c49dc7..f37f584 100644

//Synthetic comment -- @@ -132,8 +132,11 @@
						MediaScannerConnection connection = mConnectionMap.get(context);    						
						if (connection != null) {
							try {
    								final String downloadDirectoryPath = LocalDataSource.DOWNLOAD_BUCKET_NAME;
    								File downloadDirectory = new File(downloadDirectoryPath);
    								downloadDirectory.mkdirs();
								final String path = UriTexture.writeHttpDataInDirectory(context, contentUri,
    										downloadDirectoryPath);
								if (path != null) {
									connection.scanFile(path, item.mMimeType);
								} else {







