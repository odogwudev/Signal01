package com.odogwudev.signal01.crypto.roomdb;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.odogwudev.signal01.crypto.roomdb.dao.LocalIdentityDao;
import com.odogwudev.signal01.crypto.roomdb.dao.PreKeyDao;
import com.odogwudev.signal01.crypto.roomdb.dao.SessionDao;
import com.odogwudev.signal01.crypto.roomdb.dao.SignedPreKeyDao;
import com.odogwudev.signal01.crypto.roomdb.dao.TrustedKeyDao;

@Database(entities = {
        PreKeyEntity.class,
        LocalIdentityEntity.class,
        TrustedKeyEntity.class,
        SignedPreKeyEntity.class,
        SessionEntity.class}, version = 1)
public abstract class CryptoKeysDatabase extends RoomDatabase {

    public abstract PreKeyDao getPreKeyDao();

    public abstract LocalIdentityDao getLocalIdentityDao();

    public abstract TrustedKeyDao getTrustedKeyDao();

    public abstract SignedPreKeyDao getSignedPreKeyDao();

    public abstract SessionDao getSessionDao();
}