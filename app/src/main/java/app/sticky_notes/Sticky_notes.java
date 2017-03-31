package app.sticky_notes;

/**
 * This is the data taken from the database as a Note.
 * Created by Christopher D. Harte on 13/07/2016.
 */
class Sticky_notes
{
    private String row_id;
    private String title;
    private String note;


    Sticky_notes (String row_id, String title, String note)
    {
        super();
        this.row_id = row_id;
        this.title = title;
        this.note = note;
    }

    /**
     * Get the row id of the saved note.
     * @return the row id
     */
    String get_row_id()
    {
        return row_id;
    }

    /**
     * Get the title of the saved note.
     * @return the title
     */
    String get_title()
    {
        return title;
    }

    /**
     * Get the body of the saved note.
     * @return the note
     */
    String get_note()
    {
        return note;
    }

    public void put_row(String row)
    {
        row_id = row;
    }

    public void put_title (String title)
    {
        this.title = title;
    }

    public void put_note (String note)
    {
        this.note = note;
    }
    /*
    @Override public String toString()
    {
        return row_id + title + note;
    }*/

}

