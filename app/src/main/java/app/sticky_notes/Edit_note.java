package app.sticky_notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static app.sticky_notes.R.array.font_size;

/**
 * Edit the note that was retrieved from the database.
 * Created by Christopher D. Harte on 13/07/2016.
 */
public class Edit_note extends Activity implements View.OnClickListener
{
    private Sticky_database_utils utils;
    private Long row_id;
    private EditText edited_note;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        Button update_button;
        int font_size;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        Typeface fonts = Typeface.createFromAsset(getAssets(), "fonts/Note_this.ttf" );

        Bundle b = getIntent().getExtras();
        font_size = b.getInt("fong");
        row_id = b.getLong("row_id");

        utils = new Sticky_database_utils(this);

        edited_note = (EditText)findViewById(R.id.edit_text);
        update_button = (Button)findViewById(R.id.button_edit_note);

        edited_note.setText(b.getString("note"));
        edited_note.setTextSize(font_size);
        edited_note.setTypeface(fonts);

        update_button.setOnClickListener(this);
    }


    @Override public void onClick(View v)
    {
        AlertDialog.Builder dia_build;
        AlertDialog dia;

        switch (v.getId())
        {
            case R.id.button_edit_note :
                boolean verks = true;

                try
                {
                    utils.open();
                    utils.edit_entry(edited_note.getText().toString(), row_id);
                    utils.close();
                }
                catch (Exception e)
                {
                    verks = false;
                    String error = e.toString();

                    dia_build = new AlertDialog.Builder(this);

                    dia_build.setTitle("Borked");
                    dia_build.setMessage("did not work because : " + error);
                    dia_build.setPositiveButton("Damn!", new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick(DialogInterface dialog, int which)
                        {
                            Edit_note.this.finish();
                        }
                    });

                    dia = dia_build.create();
                    dia.show();

                }
                finally
                {
                    if (verks)
                    {

                        dia_build = new AlertDialog.Builder(this);

                        dia_build.setTitle("Edit applied");
                        dia_build.setMessage("and it cannot be undone or rolled back");
                        dia_build.setPositiveButton("Okay", new DialogInterface.OnClickListener()
                        {
                            @Override public void onClick(DialogInterface dialog, int which)
                            {
                                Edit_note.this.finish();
                            }
                        });

                        dia = dia_build.create();
                        dia.show();
                    }
                }
                break;
        }
    }
}

