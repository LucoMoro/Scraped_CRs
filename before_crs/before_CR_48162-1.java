/*Some changes added to compile and run with Java 6 and Java 7.

  - overload problems

Change-Id:I15a367dbce3a52557c8236fe0888f0174dfff024*/
//Synthetic comment -- diff --git a/src/com/google/android/testing/mocking/AndroidMock.java b/src/com/google/android/testing/mocking/AndroidMock.java
//Synthetic comment -- index 88f79b1..8378c4e 100644

//Synthetic comment -- @@ -2361,96 +2361,6 @@
}

/**
   * Expect any {@code int/Integer} as a parameter to the mocked method, but capture it for later
   * use.
   * 
   * {@link org.easymock.Capture} allows for capturing of the incoming value. Use
   * {@link org.easymock.Capture#getValue()} to retrieve the captured value.
   * 
   * @param captured a container to hold the captured value, retrieved by
   * {@link org.easymock.Capture#getValue()}
   * @return {@code 0}. The return value is always ignored.
   */
  public static int capture(Capture<Integer> captured) {
    return EasyMock.capture(captured);
  }

  /**
   * Expect any {@code long/Long} as a parameter to the mocked method, but capture it for later
   * use.
   * 
   * {@link org.easymock.Capture} allows for capturing of the incoming value. Use
   * {@link org.easymock.Capture#getValue()} to retrieve the captured value.
   * 
   * @param captured a container to hold the captured value, retrieved by
   * {@link org.easymock.Capture#getValue()}
   * @return {@code 0}. The return value is always ignored.
   */
  public static long capture(Capture<Long> captured) {
    return EasyMock.capture(captured);
  }

  /**
   * Expect any {@code float/Float} as a parameter to the mocked method, but capture it for later
   * use.
   * 
   * {@link org.easymock.Capture} allows for capturing of the incoming value. Use
   * {@link org.easymock.Capture#getValue()} to retrieve the captured value.
   * 
   * @param captured a container to hold the captured value, retrieved by
   * {@link org.easymock.Capture#getValue()}
   * @return {@code 0}. The return value is always ignored.
   */
  public static float capture(Capture<Float> captured) {
    return EasyMock.capture(captured);
  }

  /**
   * Expect any {@code double/Double} as a parameter to the mocked method, but capture it for later
   * use.
   * 
   * {@link org.easymock.Capture} allows for capturing of the incoming value. Use
   * {@link org.easymock.Capture#getValue()} to retrieve the captured value.
   * 
   * @param captured a container to hold the captured value, retrieved by
   * {@link org.easymock.Capture#getValue()}
   * @return {@code 0}. The return value is always ignored.
   */
  public static double capture(Capture<Double> captured) {
    return EasyMock.capture(captured);
  }

  /**
   * Expect any {@code byte/Byte} as a parameter to the mocked method, but capture it for later
   * use.
   * 
   * {@link org.easymock.Capture} allows for capturing of the incoming value. Use
   * {@link org.easymock.Capture#getValue()} to retrieve the captured value.
   * 
   * @param captured a container to hold the captured value, retrieved by
   * {@link org.easymock.Capture#getValue()}
   * @return {@code 0}
   */
  public static byte capture(Capture<Byte> captured) {
    return EasyMock.capture(captured);
  }

  /**
   * Expect any {@code char/Character} as a parameter to the mocked method, but capture it for later
   * use.
   * 
   * {@link org.easymock.Capture} allows for capturing of the incoming value. Use
   * {@link org.easymock.Capture#getValue()} to retrieve the captured value.
   * 
   * @param captured a container to hold the captured value, retrieved by
   * {@link org.easymock.Capture#getValue()}
   * @return {@code 0}
   */
  public static char capture(Capture<Character> captured) {
    return EasyMock.capture(captured);
  }

  /**
* Switches the given mock objects (more exactly: the controls of the mock
* objects) to replay mode.
* 







