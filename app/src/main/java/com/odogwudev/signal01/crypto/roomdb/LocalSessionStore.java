package com.odogwudev.signal01.crypto.roomdb;

import android.util.Log;

import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;

import com.odogwudev.signal01.crypto.roomdb.dao.SessionDao;

public class LocalSessionStore implements SessionStore {

    private SessionDao mSessionDao;

    public LocalSessionStore(@NonNull SessionDao sessionDao) {
        mSessionDao = sessionDao;
    }

    @Override
    public SessionRecord loadSession(SignalProtocolAddress address) {
        SessionEntity entity = mSessionDao.queryByAddress(address.getName(), address.getDeviceId());
        if (entity != null && entity.getSession() != null) {
            try {
                return new SessionRecord(entity.getSession());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new SessionRecord();
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        return mSessionDao.queryDeviceIds(name, 1);
    }

    @Override
    public void storeSession(SignalProtocolAddress address, SessionRecord record) {
        Log.d("TestEncrypt", "store session...");
//        if (containsSession(address)) return;
        Log.d("TestEncrypt", "\t session not exist store new one");
        mSessionDao.insert(new SessionEntity(
                address.getName(),
                address.getDeviceId(),
                record.serialize()
        ));
    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {
        int count = mSessionDao.queryCountByAddress(address.getName(), address.getDeviceId());
        return count > 0;
    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        mSessionDao.deleteByAddress(address.getName(), address.getDeviceId());
    }

    @Override
    public void deleteAllSessions(String name) {
        mSessionDao.deleteByAddressName(name);
    }
}