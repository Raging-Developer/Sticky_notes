package app.sticky_notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Edit the note that was retrieved from the database.
 * Created by Christopher D. Harte on 13/07/2016.
 */
public class Edit_note extends Activity implements View.OnClickListener
{
    private Sticky_database_utils utils;
    private Long                  row_id;
    private EditText              edited_note;
    private int                   font_size;
    private String                font_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        edited_note          = findViewById(R.id.edit_text);
        Button update_button = findViewById(R.id.button_edit_note);
        Bundle b             = getIntent().getExtras();

        if (b != null) {
            font_size = b.getInt("fong");
            font_name = b.getString("fonz");
            row_id    = b.getLong("row_id");
            edited_note.setText(b.getString("note"));
        }

        Typeface fonts = Typeface.createFromAsset(getAssets(), "fonts/" + font_name);
        utils          = new Sticky_database_utils(this);
        edited_note.setTextSize(font_size);
        edited_note.setTypeface(fonts);

        update_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dia_build;
        AlertDialog dia;

        if (v.getId() == R.id.button_edit_note) {
            boolean verks = true;

            try {
                if (edited_note.getText().toString().trim().length() == 0) {
                    verks = false;
                    return;
                }

                utils.open();
                utils.edit_entry(edited_note.getText().toString(), row_id);
                utils.close();
            }
            catch (Exception e) {
                verks = false;
                String error = e.toString();

                dia_build = new AlertDialog.Builder(this);

                dia_build.setTitle("Borked");
                dia_build.setMessage("did not work because : " + error);
                dia_build.setPositiveButton("Damn!", (dialog, which) -> Edit_note.this.finish());

                dia = dia_build.create();
                dia.show();

            }
            finally {
                dia_build = new AlertDialog.Builder(this);

                if (verks) {
                    dia_build.setTitle("Edit applied");
                    dia_build.setMessage("and it cannot be undone or rolled back");
                    dia_build.setPositiveButton("Okay", (dialog, which) -> Edit_note.this.finish());
                }
                else {
                    dia_build.setMessage("Nothing to add").setCancelable(false);
                    dia_build.setPositiveButton("Needs more text", (dialog, which) -> Edit_note.this.finish());
                }

                dia = dia_build.create();
                dia.show();
            }
        }
    }
}

