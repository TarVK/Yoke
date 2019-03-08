package com.yoke.database;

import android.arch.core.util.Function;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class DataObject<T extends DataObject.DataObjectData>{
    protected T data;
    protected DataBase db;

    public DataObject(T data) {
        db = DataBase.getInstance();
        this.data = data;
    }


    // Standard methods for any data
    protected static <T extends DataObject<D>, D extends DataObject.DataObjectData> void getAll(
            final DataDao<D> dao,
            final Function<D, T> getObject,
            final DataCallback<List<T>> dataCallback) {
        new Thread(new Runnable() {
            public void run() {
                List<D> data = dao.getAll();
                dataCallback.retrieve(mapAll(data,getObject));
            }
        }).start();
    }
    protected static <S, T> List<T> mapAll(Collection<S> collection, Function<S, T> mapper) {
        List<T> list = new ArrayList<T>();
        for (S item: collection){
            list.add(mapper.apply(item));
        }
        return list;
    }
    protected static <T extends DataObject, D extends DataObject.DataObjectData> void getByID(
            final long ID,
            final DataDao<D> dao,
            final Function<D, T> getObject,
            final DataCallback<T> dataCallback) {
        new Thread(new Runnable() {
            public void run() {
                D data = dao.getByID(ID);
                dataCallback.retrieve(getObject.apply(data));
            }
        }).start();
    }


    /**
     * Retrieves the unique ID of this data object
     * @return The ID
     */
    public long getID() {
        return this.data.uid;
    }

    /**
     * Saves all of the data in the database
     */
    public void save() {
        this.save(null);
    }

    /**
     * Saves all of the data in the database
     * @param callback  The callback to be called once saving has finished
     */
    public void save(Callback callback) {
        new Thread(new Runnable() {
            public void run() {
                DataDao<T> dao = getDoa();

                // Check whether data should be inserted or updated
                if (data.uid == 0) {
                    data.uid = dao.insert(data);
                } else {
                    dao.update(data);
                }

                // Perform teh callback
                if (callback != null) {
                    callback.call();
                }
            }
        }).start();
    }

    /**
     * Deletes the data from the database
     */
    public void delete() {
        this.delete(null);
    }

    /**
     * Deletes the data from the database
     * @param callback  The callback to be called once deletion has finished
     */
    public void delete(Callback callback) {
        T data = this.data;
        new Thread(new Runnable() {
            public void run() {
                DataDao<T> dao = getDoa();
                dao.delete(data);
                if (callback != null) {
                    callback.call();
                }
            }
        }).start();
    }

    // Async callback classes
    public interface DataCallback<T> {
        void retrieve(T data);
    }
    public interface Callback {
        void call();
    }

    // The standard data that any object has
    static public abstract class DataObjectData {
        @PrimaryKey(autoGenerate = true)
        public long uid;
    }

    // The standard dao that any object has
    public interface DataDao<T extends DataObjectData> {
        List<T> getAll();
        T getByID(long ID);

        @Update
        void update(T data);

        @Insert
        long insert(T data);

        @Delete
        void delete(T data);
    }
    abstract protected DataDao<T> getDoa();
}
