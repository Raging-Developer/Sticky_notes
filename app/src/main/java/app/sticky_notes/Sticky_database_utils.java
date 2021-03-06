package app.sticky_notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Utils for access the database.
 * Created by Christopher D. Harte on 13/07/2016.
 */
class Sticky_database_utils
{
    private final Context my_context;
    private Db_helper my_helper;
    private SQLiteDatabase my_database;
//    private ArrayList<Sticky_notes> results;

    private static final String database_name = "Sticky_notes";
    private static final String database_table = "notes";
    private static final Integer database_version = 1;

    private static final String key_row_id = "_id";
    private static final String key_title = "title";
    private static final String key_note = "note";

    private String[] columns = new String[] {key_row_id, key_title, key_note};


    Sticky_database_utils (Context c)
    {
        my_context = c;
    }

    /**
     * Creates a new helper and opens the database for reading and writing.
     * @throws  SQLException
     */
    public void open() throws SQLException
    {
        my_helper = new Db_helper (my_context);
        my_database = my_helper.getWritableDatabase();
    }

    /**
     * Close the database.
     */
    void close()
    {
        my_helper.close();
    }

    /**
     * Create a new entry in the database
     * @param title the current time and date. A string
     * @param note what ever is typed into the screen. A string
     * @return the result of the attempt to insert a row into the database. A long
     */
    public long create_entry(String title, String note)
    {
        ContentValues cv = new ContentValues();
        cv.put(key_title, title);
        cv.put(key_note, note);

        return my_database.insert(database_table, null, cv);

    }

    /**
     * Populate the arrayList with the saved sticky notes
     * @return ArrayList of Sticky_notes
     * @throws SQLException
     */
    ArrayList<Sticky_notes> get_notes() throws SQLException
    {
        ArrayList<Sticky_notes> results = new ArrayList<>();

        Cursor c = my_database.query(database_table, columns, null, null, null, null, null);

        int i_row = c.getColumnIndex(key_row_id);
        int i_title = c.getColumnIndex(key_title);
        int i_note = c.getColumnIndex(key_note);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            String row = c.getString(i_row);
            String title = c.getString(i_title);
            String note = c.getString(i_note);

            Sticky_notes notes = new Sticky_notes (row, title, note);

            results.add(notes);
        }

        c.close();
        return results;
    }


    /**
     * Returns a single note from the database.
     * Currently unused, I am getting the data from the adapter instead
     * @param l the row id. A long
     * @return the saved note at the given row id. A string
     * @throws SQLException
     */
    public String get_a_note (long l) throws SQLException
    {
        Cursor c = my_database.query(database_table, columns, key_row_id + "=" + l, null, null, null, null);

        if (c != null)
        {
            String q_res;
            c.moveToFirst();
            q_res = c.getString(2);
            c.close();

            return q_res;
        }
        return null;
    }

    /**
     * Delete the entry at the given row id
     * @param row the row id. A long
     * @throws SQLException
     */
    int delete_entry (long row) throws SQLException
    {
        return my_database.delete(database_table, key_row_id + "=" + row , null);
    }

    /**
     * Edit the body of the saved note, the title will be unchanged.
     * @param note the body of the note. A string
     * @param row the row id. A long
     * @throws SQLException
     */
    int edit_entry (String note, Long row) throws SQLException
    {
        ContentValues cv = new ContentValues();
        cv.put(key_note, note);

        return my_database.update(database_table, cv, key_row_id + "=" + row, null);
    }

    /**
     * The helper for the sticky notes database
     * @author Christopher D. Harte
     *
     */
    private static class Db_helper extends SQLiteOpenHelper
    {
        Db_helper (Context context)
        {
            super (context, database_name, null, database_version);
        }

        @Override public void onCreate(SQLiteDatabase db)
        {
            db.execSQL( "CREATE TABLE IF NOT EXISTS " + database_table + " ("
                    + key_row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + key_title + " TEXT NOT NULL,"
                    + key_note + " TEXT NOT NULL);");
        }

        /**
         * Or not. Do not drop if you want to carry on using the existing database.
         */
        @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + database_table);
            onCreate(db);
        }
    }
}
