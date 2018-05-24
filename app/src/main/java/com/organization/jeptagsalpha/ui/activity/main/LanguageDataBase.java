package com.organization.jeptagsalpha.ui.activity.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.organization.jeptagsalpha.R;



public class LanguageDataBase {
    public static String table="create table language(name varchar(50) not null);";


    DataBaseHelper dataBaseHelper;
    Context ctx;
    SQLiteDatabase db;

    public LanguageDataBase(Context ctx)
    {
        this.ctx=ctx;
        dataBaseHelper=new DataBaseHelper(ctx);
    }


    private static class DataBaseHelper extends SQLiteOpenHelper
    {
        Context ct;
        public DataBaseHelper(Context ctx)
        {
            super(ctx,"MeoTalkLe",null,1);
            ct=ctx;
        }

        private String getResString(int resId){
            return ct.getResources().getString(resId);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try
            {
                db.execSQL(table);
                String lan= getResString(R.string.en);

                String s="insert into language values('"+lan+"')";
                db.execSQL(s);
            }
            catch (Exception e)
            {

            }

        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table if already exists language");
            onCreate(db);
        }
    }

    public LanguageDataBase open()
    {
        db=dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    public void update(String s1)
    {
        String s="update language set name='"+s1+"'";
        db.execSQL(s);
    }

    public Cursor returnall()
    {
        return  db.rawQuery("select * from language",null);
    }
}

