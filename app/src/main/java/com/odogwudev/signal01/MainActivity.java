package com.odogwudev.signal01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;
import org.whispersystems.libsignal.util.Medium;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //At the install time, We need to generate IdentityKeyPair, registrationId, SignedPreKeyRecord & SignedPreKeyRecord.

    /*public static RegistrationKeyModel generateKeys() throws InvalidKeyException, IOException {
        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
        int registrationId = KeyHelper.generateRegistrationId(false);
        SignedPreKeyRecord signedPreKey = KeyHelper.generateSignedPreKey(identityKeyPair, new Random().nextInt(Medium.MAX_VALUE - 1));
        List<PreKeyRecord> preKeys = KeyHelper.generatePreKeys(new Random().nextInt(Medium.MAX_VALUE - 101), 100);
        return new RegistrationKeyModel(
                identityKeyPair,
                registrationId,
                preKeys,
                signedPreKey
        );
    }*/
    private static void init() {
        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
    }
}