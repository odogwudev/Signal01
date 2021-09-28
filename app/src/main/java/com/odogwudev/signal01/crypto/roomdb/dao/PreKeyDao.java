package com.odogwudev.signal01.crypto.roomdb.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.odogwudev.signal01.crypto.roomdb.PreKeyEntity;

@Dao
public interface PreKeyDao {

    @Query("SELECT * FROM prekeys WHERE prekey_id = :preKeyId")
    PreKeyEntity queryByPreKeyId(int preKeyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreKey(PreKeyEntity preKeyEntity);

    @Query("SELECT COUNT(prekey_id) FROM prekeys WHERE prekey_id = :preKeyId")
    int countByPreKeyId(int preKeyId);

    @Query("DELETE FROM prekeys WHERE prekey_id = :preKeyId")
    void deleteByPreKeyId(int preKeyId);

}