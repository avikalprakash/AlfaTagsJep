package com.organization.jeptagsalpha.ui.activity.main;

import android.content.Context;
import android.database.Cursor;

public class ReadLanguage {
    public static String read(Context ctx)
    {
        String language="";
        LanguageDataBase languageDatabase=new LanguageDataBase(ctx);
        languageDatabase.open();
        Cursor cu=languageDatabase.returnall();
        if(cu.moveToFirst())
        {
            do {
                language=cu.getString(0);
            }
            while (cu.moveToNext());
        }
        languageDatabase.close();
        return language;
    }
}
