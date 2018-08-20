package com.minivision.openplatform.lmdb.server.core.lmdb;

import lombok.extern.slf4j.Slf4j;
import org.lmdbjava.Cursor;
import org.lmdbjava.Dbi;
import org.lmdbjava.DbiFlags;
import org.lmdbjava.Env;
import org.lmdbjava.GetOp;
import org.lmdbjava.Txn;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description> <br>
 *
 * @author caixing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年08月17日 <br>
 */
@Slf4j
@Component
public class LmdbOperation {

    /**
     * lmdb path
     */
    @Value("${lmdb.path}")
    private String lmdbPath;

    /**
     * env
     */
    private Env<ByteBuffer> env;

    /**
     * lmdb max size
     */
    @Value("${lmdb.max.size}")
    private int lmdbMaxSize;

    /**
     * lmdb库的数量
     */
    @Value("${lmdb.database.count}:10")
    private int lmdbDatabaseCount;

    /**
     * 存储当前的dbi
     */
    private ConcurrentHashMap<String, Dbi<ByteBuffer>> concurrentHashMap;

    /**
     * 存储当前的txn reader
     */
    private ConcurrentHashMap<String, Txn<ByteBuffer>> txnConcurrentHashMap;


    /**
     * Description: 初始化操作 <br>
     * @author caixing<br>
     * @taskId <br>
     *
     */
    @PostConstruct
    private void init() {
        File lmdbFile = new File(lmdbPath);
        env = Env.<ByteBuffer>create().setMapSize(lmdbMaxSize * 1024 * 1024 * 1024).setMaxDbs(lmdbDatabaseCount).setMaxReaders(lmdbDatabaseCount*2).open(lmdbFile);
        concurrentHashMap = new ConcurrentHashMap<>();
        txnConcurrentHashMap = new ConcurrentHashMap<>();
    }

    /**
     * Description: 向库中插入数据 <br>
     * @param dbName dbname
     * @param dbVal 数据
     * @author caixing<br>
     * @taskId <br>
     *
     */
    public void putValueToDb(String dbName, Map<byte[], byte[]> dbVal) {
        Dbi<ByteBuffer> db = getDbi(dbName);
        Txn<ByteBuffer> txn = env.txnWrite();
        Cursor<ByteBuffer> cursor = db.openCursor(txn);
        for (Map.Entry<byte[], byte[]> entry : dbVal.entrySet()) {
            ByteBuffer key = ByteBuffer.allocateDirect(entry.getKey().length);
            ByteBuffer val = ByteBuffer.allocateDirect(entry.getValue().length);
            key.put(entry.getKey()).flip();
            val.put(entry.getValue()).flip();
            cursor.put(key, val);
        }
        cursor.close();
        txn.commit();
        txn.close();
    }

    /**
     * Description: 根据指定的key获取数据 <br>
     * @param dbName dbname
     * @param key key
     * @author caixing<br>
     * @taskId <br>
     * @return 获取到的值
     *
     */
    public ByteBuffer getValueByKey(String dbName, byte[] key) {
        Dbi<ByteBuffer> db = getDbi(dbName);
        Txn<ByteBuffer> txn = getTxn(dbName);
        Cursor<ByteBuffer> cursor = db.openCursor(txn);
        ByteBuffer k = ByteBuffer.allocateDirect(key.length);
        k.put(key).flip();
        cursor.get(k, GetOp.MDB_SET_KEY);
        ByteBuffer value = cursor.val();
        cursor.close();
        return value;
    }

    /**
     * Description: 获取库下所有的数据 <br>
     * @param dbName dbname
     * @author caixing<br>
     * @taskId <br>
     * @return 所有的数据
     *
     */
    public Map<byte[], byte[]> getAllValueByDbName(String dbName) {
        Dbi<ByteBuffer> db = getDbi(dbName);
        Txn<ByteBuffer> txn = getTxn(dbName);
        Cursor<ByteBuffer> cursor = db.openCursor(txn);
        Map<byte[], byte[]> map = new HashMap<>();
        while (cursor.next()) {
            ByteBuffer key = cursor.key();
            ByteBuffer val = cursor.val();
            byte[] k = new byte[key.capacity()];
            byte[] v = new byte[val.capacity()];
            key.get(k);
            val.get(v);
            map.put(k, v);
        }
        cursor.close();
        return map;
    }

    /**
     * Description: 根据dbname获取该库下的数量 <br>
     * @param dbName dbname
     * @author caixing<br>
     * @taskId <br>
     * @return 库中的数量
     *
     */
    public int getDbCount(String dbName) {
        return getAllValueByDbName(dbName).size();
    }

    /**
     * Description: 根据dbName获取dbi <br>
     * @param dbName db名称
     * @author caixing<br>
     * @taskId <br>
     * @return dbi
     *
     */
    private Dbi<ByteBuffer> getDbi(String dbName) {
        Dbi<ByteBuffer> dbi = null;
        if (concurrentHashMap.containsKey(dbName)) {
            dbi = concurrentHashMap.get(dbName);
        } else {
            dbi = env.openDbi(dbName, DbiFlags.MDB_CREATE);
            concurrentHashMap.put(dbName, dbi);
        }
        return dbi;
    }

    /**
     * Description: 根据dbname获取txnReader <br>
     * @param dbName db名称
     * @author caixing<br>
     * @taskId <br>
     * @return txn_reader
     *
     */
    private Txn<ByteBuffer> getTxn(String dbName) {
        Txn<ByteBuffer> txn = null;
        if (txnConcurrentHashMap.containsKey(dbName)) {
            txn = txnConcurrentHashMap.get(dbName);
        } else {
            txn = env.txnRead();
            txnConcurrentHashMap.put(dbName, txn);
        }
        return txn;
    }


}
