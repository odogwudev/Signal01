package com.odogwudev.signal01.crypto.roomdb.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.odogwudev.signal01.crypto.roomdb.LocalIdentityEntity;

@Dao
public interface LocalIdentityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLocalIdentity(LocalIdentityEntity entity);

    @Query("SELECT * FROM local_identity")
    LocalIdentityEntity query();

    @Query("SELECT reg_id FROM local_identity")
    int queryRegistrationId();

}