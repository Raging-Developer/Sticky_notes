package app.sticky_notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

/**
 * Add a note to the database.
 * Created by Christopher D. Harte on 13/07/2016.
 */
public class Add_note extends Activity implements OnClickListener
{
    private Sticky_database_utils utils;
    private EditText              add_note;
    private String                date_title;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        Typeface fonts = Typeface.createFromAsset(getAssets(), "fonts/Note_this.ttf" );

        utils = new Sticky_database_utils(this);

        Bundle b = getIntent().getExtras();
        int font_size = b.getInt("fong");

        Button add_button = (Button) findViewById(R.id.button_add_add);
        add_button.setOnClickListener(this);

        add_note = (EditText) findViewById(R.id.add_note);
        add_note.setTextSize(font_size);
        add_note.setTypeface(fonts);

        date_title = (String) DateFormat.format("\n dd/MM/yyyy hh:mm:ss", new Date());
    }

    @Override public void onClick(View v)
    {
        AlertDialog         dia;
        AlertDialog.Builder dia_build;

        switch (v.getId())
        {
            case R.id.button_add_add :
                boolean verks = true;

                try
                {
                    utils.open();
                    utils.create_entry(date_title, add_note.getText().toString());
                    utils.close();
                }
                catch (Exception e)
                {
                    verks = false;
                    String error = e.toString();
                    dia_build = new AlertDialog.Builder(this);

                    dia_build.setTitle("Borked");
                    dia_build.setMessage("Cock up alert : " + error).setCancelable(false);
                    dia_build.setPositiveButton("Clear", new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick(DialogInterface dialog, int which)
                        {
                            Add_note.this.finish();
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

                        dia_build.setMessage("Note has been added").setCancelable(false);
                        dia_build.setPositiveButton("Okay Dokey", new DialogInterface.OnClickListener()
                        {
                            @Override public void onClick(DialogInterface dialog, int which)
                            {
                                Add_note.this.finish();
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

