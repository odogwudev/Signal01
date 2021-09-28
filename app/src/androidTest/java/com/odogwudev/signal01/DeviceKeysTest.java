package com.odogwudev.signal01;

import android.util.Log;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.whispersystems.libsignal.InvalidKeyException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.odogwudev.signal01.crypto.DeviceKeyBundle;
import com.odogwudev.signal01.crypto.DeviceKeyBundleUtils;
import com.odogwudev.signal01.crypto.RemoteDeviceKeys;
import com.odogwudev.signal01.crypto.RemoteDeviceKeysUtils;

@RunWith(AndroidJUnit4.class)
public class DeviceKeysTest {

    @Test
    public void generateDeviceKeyBundleTest() throws InvalidKeyException {
        DeviceKeyBundle dkb = DeviceKeyBundleUtils
                .generateDeviceKeyBundle("+6281111111111", 11228, 1, 100);

        assertNotNull(dkb);
        assertEquals("+6281111111111", dkb.getAddress().getName());
        assertEquals(11228, dkb.getAddress().getDeviceId());
        assertEquals(1, dkb.getSignedPreKey().getId());
        assertEquals(dkb.getPreKeys().size(), 100);
    }

    @Test
    public void preKeyBundleSerializationDeserializationTest() throws InvalidKeyException, JSONException {
        DeviceKeyBundle dkb = DeviceKeyBundleUtils
                .generateDeviceKeyBundle("+6281111111111", 123, 1, 1);

        RemoteDeviceKeys rdk = new RemoteDeviceKeys(
                dkb.getAddress(),
                dkb.getRegistrationId(),
                dkb.getPreKeys().get(0).getId(),
                dkb.getPreKeys().get(0).getKeyPair().getPublicKey(),
                dkb.getSignedPreKey().getId(),
                dkb.getSignedPreKey().getKeyPair().getPublicKey(),
                dkb.getSignedPreKey().getSignature(),
                dkb.getIdentityKeyPair().getPublicKey()
        );

        String keyJson1 = RemoteDeviceKeysUtils.toJson(rdk);

        assertNotNull(keyJson1);

        RemoteDeviceKeys rdk2 = RemoteDeviceKeysUtils.fromJson(keyJson1);

        assertNotNull(rdk2);
        assertEquals(rdk.getRegistrationId(), rdk2.getRegistrationId());
        assertEquals(rdk.getAddress(), rdk2.getAddress());
        assertEquals(rdk.getPreKeyId(), rdk2.getPreKeyId());
        assertEquals(rdk.getPreKeyPublic(), rdk2.getPreKeyPublic());
        assertEquals(rdk.getSignPreKeyId(), rdk2.getSignPreKeyId());
        assertEquals(rdk.getSignedPreKeyPublic(), rdk2.getSignedPreKeyPublic());
        assertArrayEquals(rdk.getSignedPreKeySignature(), rdk2.getSignedPreKeySignature());
        assertEquals(rdk.getIdentityKey(), rdk2.getIdentityKey());

        String keyJson2 = RemoteDeviceKeysUtils.toJson(rdk2);

        assertNotNull(keyJson2);
        assertEquals(keyJson1, keyJson2);

        Log.d("DeviceKeysTest", keyJson1);
        Log.d("DeviceKeysTest", keyJson2);
    }
}
