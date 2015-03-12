package com.sdex.webteb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.database.model.DbUser;

import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Yuriy Mysochenko on 02.03.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "album.db";
    private static final int VERSION = 7;

    private static DatabaseHelper mInstance = null;

    static {
        cupboard().register(DbPhoto.class);
        cupboard().register(DbUser.class);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
        DbPhoto photo = new DbPhoto();
        photo.setDescription("tmp");
        photo.setTimestamp(0L);
        cupboard().withDatabase(db).put(photo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

    public void addPhoto(DbPhoto photo) {
        cupboard().withDatabase(getWritableDatabase()).put(photo);
    }

    public void deletePhoto(DbPhoto photo) {
        cupboard().withDatabase(getWritableDatabase()).delete(photo);
    }

    public List<DbPhoto> getPhotos(String owner) {
        return cupboard().withDatabase(getReadableDatabase()).query(DbPhoto.class)
                .withSelection("timestamp > ? AND owner = ?", "0", owner)
                .orderBy("timestamp"/* + " DESC"*/) // show newer on the top
                .list();
    }

    public List<DbPhoto> getPhotos(int limit, String owner) {
        return cupboard().withDatabase(getReadableDatabase()).query(DbPhoto.class)
                .withSelection("timestamp > ? AND owner = ?", "0", owner)
                .orderBy("timestamp DESC")
                .limit(limit)
                .list();
    }

    public DbPhoto getTmpPhoto() {
        return cupboard().withDatabase(getReadableDatabase()).query(DbPhoto.class)
                .withSelection("timestamp = ?", "0")
                .get();
    }

    public void setTmpPhoto(Uri uri) {
        DbPhoto photo = cupboard().withDatabase(getReadableDatabase()).query(DbPhoto.class)
                .withSelection("timestamp = ?", "0")
                .get();
        photo.setPath(uri.getPath());
        cupboard().withDatabase(getWritableDatabase()).put(photo);
    }

    public void addUser(DbUser user) {
        cupboard().withDatabase(getWritableDatabase()).put(user);
    }

    public DbUser getUser(String email) {
        return cupboard().withDatabase(getReadableDatabase()).query(DbUser.class)
                .withSelection("email = ?", email)
                .get();
    }

    public void updateUser(DbUser user) {
        ContentValues values = cupboard().withEntity(DbUser.class).toContentValues(user);
        cupboard().withDatabase(getWritableDatabase()).update(DbUser.class, values);
    }

}
