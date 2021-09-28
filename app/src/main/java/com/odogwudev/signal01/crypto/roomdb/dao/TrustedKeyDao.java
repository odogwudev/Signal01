package com.odogwudev.signal01.crypto.roomdb.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.odogwudev.signal01.crypto.roomdb.TrustedKeyEntity;

@Dao
public interface TrustedKeyDao {

    @Query("SELECT * FROM trusted_identities WHERE address_name = :name AND device_id = :deviceId")
    TrustedKeyEntity queryByNameAndDeviceId(String name, int deviceId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TrustedKeyEntity entity);

    @Query("SELECT COUNT(*) FROM trusted_identities WHERE address_name = :name AND device_id = :deviceId")
    int countByNameAndDeviceId(String name, int deviceId);
}