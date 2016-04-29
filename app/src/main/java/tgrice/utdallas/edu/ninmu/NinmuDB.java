/**
 * Created by Thomas on 4/25/2016.
 */
package tgrice.utdallas.edu.ninmu;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// using this class to handle DB stuff, especially executing the SQL script for the first time
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
        readSQLScript(database, "createdb.sql");;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // I don't have anything planned for upgrades yet...
    }

    // reads in DB Creation script ran when app is started for the first time or reinstalled
    private void readSQLScript(SQLiteDatabase db, String dbfilename) {
    ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        byte byteBuffer[] = new byte[1024]; // buffer used for file I/O
        int length; // used to track whether buffer is empty
        AssetManager assetManager = context.getAssets(); // used to get sql script (an asset)
        InputStream iStream;
        try{
            iStream = assetManager.open(dbfilename);
            length = iStream.read(byteBuffer);
            // write to ostream while buff not empty
            do {
                oStream.write(byteBuffer, 0, length);
            }  while ((length = iStream.read(byteBuffer)) != -1);
            String[] sqlScript = oStream.toString().split(";");
            int lineLength; // used to check if line length > 0
            for (String substr : sqlScript) { // for each substring, execute as SQL statement
                String sqlLine = substr.trim();
                lineLength = sqlLine.length();
                if (lineLength > 0) { // if length is greater than 0, go ahead and execute.
                    sqlLine = sqlLine + ";";
                    db.execSQL(sqlLine);
                }
            }
            // close streams
            oStream.close();
            iStream.close();
        } catch (IOException err0r){
            // Log if there is an IO error... (hopefully not!)
            Log.v("NinmuDB", "Failed to load DB script");
        }

    }
}