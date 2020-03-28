package com.pos.techicalservices;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AndroidDatabase extends SQLiteOpenHelper implements Database {

    private static final int DATABASE_VERSION = 1;

    public AndroidDatabase(Context context) {
        super(context, DatabaseContents.DATABASE.toString(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_PRODUCT_CATALOG + "("
                + "_id INTEGER PRIMARY KEY,"
                + "name TEXT(100),"
                + "barcode TEXT(100),"
                + "unit_price DOUBLE,"
                + "status TEXT(10)"+");"
        );

        Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_PRODUCT_CATALOG + " Successfully.");

        database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_STOCK + "("
				+ "_id INTEGER PRIMARY KEY,"
				+ "product_id INTEGER,"
				+ "quantity INTEGER,"
				+ "cost DOUBLE,"
				+ "date_added DATETIME"
				
				+ ");");
        Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_STOCK + " Successfully.");



        database.execSQL("CREATE TABLE "+ DatabaseContents.TABLE_SALE + "("

                + "_id INTEGER PRIMARY KEY,"
                + "status TEXT(30),"
                + "payment TEXT(30),"
                + "total DOUBLE,"
                + "start_time DATETIME,"
                + "end_time DATETIME,"
                + "orders INTEGER"

                + ");");
      Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_SALE + " Successfully.");


        database.execSQL("CREATE TABLE "+ DatabaseContents.TABLE_SALE_LINEITEM + "("

                + "_id INTEGER PRIMARY KEY,"
                + "sale_id INTEGER,"
                + "product_id INTEGER,"
                + "quantity INTEGER,"
                + "unit_price DOUBLE"

                + ");");
        Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_SALE_LINEITEM + " Successfully.");

// this _id is product_id but for update method, it is easier to use name _id
        database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_STOCK_SUM + "("

                + "_id INTEGER PRIMARY KEY,"
                + "quantity INTEGER"

                + ");");
        Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_STOCK_SUM + " Successfully.");

         database.execSQL("CREATE TABLE " + DatabaseContents.LANGUAGE + "("

                + "_id INTEGER PRIMARY KEY,"
                + "language TEXT(5)"

                + ");");

        Log.d("CREATE DATABASE", "Create " + DatabaseContents.LANGUAGE + " Successfully.");


        Log.d("CREATE DATABASE", "Create Database Successfully.");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public List<Object> select(String queryString) {
        try{
            SQLiteDatabase database = this.getWritableDatabase();
            List<Object> list = new ArrayList<Object>();
            // rawQuery for select command,ex:"select * from person”
            Cursor cursor = database.rawQuery(queryString, null);
            if (cursor != null) {
              if(cursor.moveToFirst()){
                  do{
                      ContentValues content = new ContentValues();
                      String [] columnnames =  cursor.getColumnNames();
                      for(String columnName :columnnames)
                      {
                         content.put(columnName,cursor.getString(cursor.getColumnIndex(columnName)));
                      }
                      list.add(content);
                  }while (cursor.moveToNext());

              }
            }
            cursor.close();
            database.close();
            return list;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int insert(String tableName, Object content) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            int id = (int) database.insert(tableName, null,
                    (ContentValues) content);
//System.out.println("android database insert="+id);
            database.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    @Override
    public boolean update(String tableName, Object content) {

        try {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues cont = (ContentValues) content;
            // this array will always contains only one element.
            String[] array = new String[]{cont.get("_id")+""};
            //update(String table, ContentValues values, String whereClause, String[] whereArgs)
            //ex “update person set phone=?,name=? where personid=?”
            database.update(tableName, cont, " _id = ?", array);
            database.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean delete(String tableName, int id) {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            //delete(String table, String whereClause, String[] whereArgs)
            database.delete(tableName, " _id = ?", new String[]{id+""});
            database.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean execute(String queryString) {
        try{
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL(queryString);
            database.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean SqltabbleIsExist(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        if(tableName == null){
            return false;
        }
        Cursor cursor = null;
        try {
           String sql = "SELECT count(*) as c FROM sqlite_master  WHERE type ='table' AND name ='"+tableName.trim()+"' ";
System.out.println("sql query string="+sql);
            //cursor = db.rawQuery(sql,  new String[] {"table", tableName});
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        db.close();
        return result;
    }
}
