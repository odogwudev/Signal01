package com.odogwudev.signal01.db;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.util.KeyHelper;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.odogwudev.signal01.crypto.roomdb.CryptoKeysDatabase;
import com.odogwudev.signal01.crypto.roomdb.LocalIdentityEntity;
import com.odogwudev.signal01.crypto.roomdb.LocalIdentityKeyStore;
import com.odogwudev.signal01.crypto.roomdb.TrustedKeyEntity;
import com.odogwudev.signal01.crypto.roomdb.dao.LocalIdentityDao;
import com.odogwudev.signal01.crypto.roomdb.dao.TrustedKeyDao;


@RunWith(AndroidJUnit4.class)
public class TrustedIdentityDatabaseAndStoreTest {

    private CryptoKeysDatabase db;
    private LocalIdentityDao mLocalIdentityDao;
    private TrustedKeyDao mTrustedKeyDao;
    private LocalIdentityKeyStore mLocalIdentityKeyStore;

    @Before
    public void start() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, CryptoKeysDatabase.class).build();
        mLocalIdentityDao = db.getLocalIdentityDao();
        mTrustedKeyDao = db.getTrustedKeyDao();
        mLocalIdentityKeyStore = new LocalIdentityKeyStore(mTrustedKeyDao, mLocalIdentityDao);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void localIdentityDaoTest() throws InvalidKeyException {
        int regId = KeyHelper.generateRegistrationId(false);
        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();

        LocalIdentityEntity idEnt = new LocalIdentityEntity(
                0, regId, identityKeyPair.serialize()
        );

        mLocalIdentityDao.setLocalIdentity(idEnt);

        int queryRegId = mLocalIdentityDao.queryRegistrationId();

        assertEquals(regId, queryRegId);

        LocalIdentityEntity queryIdEnt = mLocalIdentityDao.query();

        assertEquals(idEnt.getRegistrationId(), queryIdEnt.getRegistrationId());
        assertArrayEquals(idEnt.getIdentityKeyPair(), queryIdEnt.getIdentityKeyPair());

        IdentityKeyPair queryIdentityKeyPair = new IdentityKeyPair(queryIdEnt.getIdentityKeyPair());

        assertArrayEquals(identityKeyPair.serialize(), queryIdentityKeyPair.serialize());
    }

    @Test
    public void trustedKeyDaoTest() throws InvalidKeyException {
        IdentityKey ik1 = new IdentityKey(Curve.generateKeyPair().getPublicKey());

        TrustedKeyEntity trust1 = new TrustedKeyEntity("+6281", 1, ik1.serialize());

        mTrustedKeyDao.insert(trust1);

        int count = mTrustedKeyDao.countByNameAndDeviceId("+6281", 1);

        assertEquals(1, count);

        count = mTrustedKeyDao.countByNameAndDeviceId("+6288", 1);

        assertEquals(0, count);

        TrustedKeyEntity queryTrust = mTrustedKeyDao.queryByNameAndDeviceId("+6281", 1);
        IdentityKey queryIk = new IdentityKey(queryTrust.getIdentityKey(), 0);

        assertNotNull(queryTrust);
        assertEquals(trust1.getAddressName(), queryTrust.getAddressName());
        assertEquals(trust1.getDeviceId(), queryTrust.getDeviceId());
        assertArrayEquals(trust1.getIdentityKey(), queryTrust.getIdentityKey());
        assertEquals(ik1, queryIk);

        queryTrust = mTrustedKeyDao.queryByNameAndDeviceId("no", 1);

        assertNull(queryTrust);
    }

    @Test
    public void localIdentityStoreTest() {
        IdentityKey ik1 = new IdentityKey(Curve.generateKeyPair().getPublicKey());
        SignalProtocolAddress addr1 = new SignalProtocolAddress("alice", 1);

        boolean saved = mLocalIdentityKeyStore.saveIdentity(addr1, ik1);

        assertTrue(saved);

        IdentityKey queryIk = mLocalIdentityKeyStore.getIdentity(addr1);

        assertArrayEquals(ik1.serialize(), queryIk.serialize());

        boolean trusted = mLocalIdentityKeyStore.isTrustedIdentity(addr1, ik1, IdentityKeyStore.Direction.RECEIVING);

        assertTrue(trusted);

        trusted = mLocalIdentityKeyStore.isTrustedIdentity(new SignalProtocolAddress("bob", 1), ik1, IdentityKeyStore.Direction.RECEIVING);

        assertTrue(trusted);
    }
}
