package com.odogwudev.signal01.crypto.roomdb;


import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import java.util.Arrays;

import androidx.annotation.NonNull;

import com.odogwudev.signal01.crypto.roomdb.dao.LocalIdentityDao;
import com.odogwudev.signal01.crypto.roomdb.dao.TrustedKeyDao;

public class LocalIdentityKeyStore implements IdentityKeyStore {

    private LocalIdentityDao mLocalIdentityDao;
    private TrustedKeyDao mTrustedKeyDao;

    public LocalIdentityKeyStore(@NonNull TrustedKeyDao trustedKeyDao, @NonNull LocalIdentityDao localIdentityDao) {
        mLocalIdentityDao = localIdentityDao;
        mTrustedKeyDao = trustedKeyDao;
    }

    public void setLocalIdentity(IdentityKeyPair identityKeyPair, int registrationId) {
        mLocalIdentityDao.setLocalIdentity(new LocalIdentityEntity(
                0, registrationId, identityKeyPair.serialize()
        ));
    }

    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        LocalIdentityEntity entity = mLocalIdentityDao.query();
        if (entity != null && entity.getIdentityKeyPair() != null) {
            try {
                return new IdentityKeyPair(entity.getIdentityKeyPair());
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int getLocalRegistrationId() {
        return mLocalIdentityDao.queryRegistrationId();
    }

    @Override
    public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
        mTrustedKeyDao.insert(new TrustedKeyEntity(
                address.getName(),
                address.getDeviceId(),
                identityKey.serialize()
        ));
        return true;
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey, Direction direction) {
        TrustedKeyEntity entity = mTrustedKeyDao.queryByNameAndDeviceId(address.getName(), address.getDeviceId());
        return (entity == null) || (Arrays.equals(entity.getIdentityKey(), identityKey.serialize()));
    }

    @Override
    public IdentityKey getIdentity(SignalProtocolAddress address) {
        TrustedKeyEntity entity = mTrustedKeyDao.queryByNameAndDeviceId(address.getName(), address.getDeviceId());
        if (entity != null) {
            try {
                return new IdentityKey(entity.getIdentityKey(), 0);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}