package com.yoke.database;

import android.arch.core.util.Function;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Update;
import android.content.Context;

import com.yoke.utils.Callback;
import com.yoke.utils.DataCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An abstract class that can be used to create any class to store data in the database
 * @param <T> The data that the class should contain
 */
public abstract class DataObject<T extends DataObject.DataObjectData>{
    // A reference to the actual data
    protected T data;

    /**
     * Creates a data object
     * @param data  The data to store in the object
     */
    public DataObject(T data) {
        this.data = data;
    }


    // Standard methods for any data

    /**
     * Gets all of the instances for some data object type from the database
     * @param dao  The dao to use to get the data
     * @param getObject  The function to translate the data obtained from the database into a data object
     * @param dataCallback  The callback to return all the data to
     * @param <T>  The data object type to retrieve
     * @param <D>  The data object data that the data object uses
     */
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

    /**
     * Maps data from one type to another
     * @param collection  THe data to map
     * @param mapper  The function to map the data with
     * @param <S>  The type of the input data
     * @param <T>  The type of the output data
     * @return  The mapped data
     */
    protected static <S, T> List<T> mapAll(Collection<S> collection, Function<S, T> mapper) {
        List<T> list = new ArrayList<T>();
        for (S item: collection){
            list.add(mapper.apply(item));
        }
        return list;
    }

    /**
     * Gets a specific instance of some data object type from the database
     * @param ID The id of the item to retrieve
     * @param dao  The dao to use to get the data
     * @param getObject  The function to translate the data obtained from the database into a data object
     * @param dataCallback  The callback to return all the data to
     * @param <T>  The data object type to retrieve
     * @param <D>  The data object data that the data object uses
     */
    protected static <T extends DataObject, D extends DataObject.DataObjectData> void getByID(
            final long ID,
            final DataDao<D> dao,
            final Function<D, T> getObject,
            final DataCallback<T> dataCallback) {
        new Thread(new Runnable() {
            public void run() {
                D data = dao.getByID(ID);
                dataCallback.retrieve(data != null ? getObject.apply(data) : null);
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
     * @param context  The context to keep an association with the specific app
     */
    public void save(Context context) {
        this.save(context, () -> {});
    }

    /**
     * Saves all of the data in the database
     * @param context  The context to keep an association with the specific app
     * @param callback  The callback to be called once saving has finished
     */
    public void save(Context context, Callback callback) {
        DataBase.getInstance(context, (db) -> {
            new Thread(new Runnable() {
                public void run() {
                    DataDao<T> dao = getDoa(db);

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
        });
    }

    /**
     * Deletes the data from the database
     * @param context  The context to keep an association with the specific app
     */
    public void delete(Context context) {
        this.delete(context, () -> {});
    }

    /**
     * Deletes the data from the database
     * @param context  The context to keep an association with the specific app
     * @param callback  The callback to be called once deletion has finished
     */
    public void delete(Context context, Callback callback) {
        DataBase.getInstance(context, (db) -> {
            T data = this.data;
            new Thread(new Runnable() {
                public void run() {
                    DataDao<T> dao = getDoa(db);
                    dao.delete(data);
                    if (callback != null) {
                        callback.call();
                    }
                }
            }).start();
        });
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

    /**
     * Retrieves the dao to be used for this class
     * @param db The database to get the dao from
     * @return  The class specific dao
     */
    abstract protected DataDao<T> getDoa(DataBase db);
}
