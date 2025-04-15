/*Corrected android.media.cts.AudioEffectTest#test1_0ConstructorFromType

The test case queries for all effects and then tries to recreate them
by using the type uuid of the effect. This however failed for effects
that did not implement a standard type. These effects will have their
type set to AudioEffect.EFFECT_TYPE_NULL and thus reconstruction of
them would fail. Made the test skip effects that has this NULL type.

Change-Id:I352844a473a174c2fbf7d37fc0f40923c00b957b*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioEffectTest.java b/tests/tests/media/src/android/media/cts/AudioEffectTest.java
//Synthetic comment -- index 514f6a7..0aaf11f 100644

//Synthetic comment -- @@ -137,23 +137,25 @@
AudioEffect.Descriptor[] desc = AudioEffect.queryEffects();
assertTrue("no effects found", (desc.length != 0));
for (int i = 0; i < desc.length; i++) {
            try {
                AudioEffect effect = new AudioEffect(desc[i].type,
                        AudioEffect.EFFECT_TYPE_NULL,
                        0,
                        0);
                assertNotNull("could not create AudioEffect", effect);
try {
                    assertTrue("invalid effect ID", (effect.getId() != 0));
                } catch (IllegalStateException e) {
                    fail("AudioEffect not initialized");
                } finally {
                    effect.release();
}
            } catch (IllegalArgumentException e) {
                fail("Effect not found: "+desc[i].name);
            } catch (UnsupportedOperationException e) {
                fail("Effect library not loaded");
}
}
}
//Synthetic comment -- @@ -1376,4 +1378,4 @@
}
}

}
\ No newline at end of file







