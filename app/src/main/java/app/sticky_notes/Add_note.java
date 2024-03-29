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
    private int                   font_size;
    private String                font_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        utils = new Sticky_database_utils(this);
        Bundle b = getIntent().getExtras();

        if (b != null) {
            font_size = b.getInt("fong");
            font_name = b.getString("fonz");
        }

        Typeface fonts = Typeface.createFromAsset(getAssets(), "fonts/" + font_name);
        Button add_button = findViewById(R.id.button_add_add);
        add_button.setOnClickListener(this);
        add_note = findViewById(R.id.add_note);
        add_note.setTextSize(font_size);
        add_note.setTypeface(fonts);
        date_title = (String) DateFormat.format("\n dd/MM/yyyy HH:mm:ss", new Date());
    }

    @Override public void onClick(View v) {
        AlertDialog         dia;
        AlertDialog.Builder dia_build;

        if (v.getId() == R.id.button_add_add) {
            boolean verks = true;

            try {
                if (add_note.getText().toString().trim().length() == 0) {
                    verks = false;
                    return;
                }
                utils.open();
                utils.create_entry(date_title, add_note.getText().toString());
                utils.close();
            }
            catch (Exception e) {
                verks = false;
                String error = e.toString();
                dia_build = new AlertDialog.Builder(this);

                dia_build.setTitle("Borked");
                dia_build.setMessage("Cock up alert : " + error).setCancelable(false);
                dia_build.setPositiveButton("Clear", (dialog, which) -> Add_note.this.finish());
                dia = dia_build.create();
                dia.show();
            }
            finally {
                dia_build = new AlertDialog.Builder(this);

                if (verks) {
                    dia_build.setMessage("Note has been added").setCancelable(false);
                    dia_build.setPositiveButton("Okay Dokey", (dialog, which) -> Add_note.this.finish());
                }
                else {
                    dia_build.setMessage("Nothing to add").setCancelable(false);
                    dia_build.setPositiveButton("Try it with text next time", (dialog, which) -> Add_note.this.finish());
                }

                dia = dia_build.create();
                dia.show();
            }
        }
    }
}

