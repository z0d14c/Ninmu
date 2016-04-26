package tgrice.utdallas.edu.ninmu;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Thomas on 4/25/2016.
 */
public class NinmuDB extends SQLiteOpenHelper {

    final static int DB_VERSION = 1;
    final static String DB_NAME = "NinmuDB";
    Context context;

    public NinmuDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // Store the context for later use
        this.context = context;
    }

    // on creation/first run of app
    // run sql script
    @Override
    public void onCreate(SQLiteDatabase database) {
        executeSQLScript(database, "createdb.sql");;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // reads in DB Creation script ran when app is started for the first time
    // written based on modified code from http://www.drdobbs.com/database/using-sqlite-on-android/232900584
    private void executeSQLScript(SQLiteDatabase database, String dbfilename) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;

        try{
            inputStream = assetManager.open(dbfilename);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            String[] createScript = outputStream.toString().split(";");
            for (String substring : createScript) {
                String sqlStatement = substring.trim();
                if (sqlStatement.length() > 0) {
                    database.execSQL(sqlStatement + ";");
                }
            }
        } catch (IOException e){
            Log.v("NinmuDB", "Failed to load DB script");
        }
    }
}