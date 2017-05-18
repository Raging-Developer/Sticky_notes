package app.sticky_notes;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class Sticky_Activity extends AppCompatActivity implements OnClickListener
{
    private ArrayList<Sticky_notes>    notes;
    private Sticky_database_utils      utils;
    private static int                 font_size;

    private long record_no;
    private int  record_position;

    RecyclerView               recycler_view;
    RecyclerView.LayoutManager resyc_layout_manager;
    RecyclerView.Adapter       resyc_adapter;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);

        Button add_button = (Button) findViewById(R.id.button_add);
        add_button.setOnClickListener(this);

        //recycler stuff
        recycler_view = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_view.setHasFixedSize(false);

        resyc_layout_manager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(resyc_layout_manager);

        font_size = get_font_size();

        // To initialise the array list for the adapter
        utils = new Sticky_database_utils(this);
        utils.open();
        notes = utils.get_notes();
        utils.close();

        resyc_adapter = new Resyc_adapter(notes);
        recycler_view.setAdapter(resyc_adapter);
    }

    @Override protected void onResume()
    {
        super.onResume();

        font_size = get_font_size();

        load_adapter();
    }

    @Override public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_add :
                Intent i = new Intent("app.sticky_notes.ADD_NOTE");
                i.putExtra("fong", font_size);
                startActivity(i);
                break;
        }
    }


    /**
     * This definately was not working when I first upped to nuggart,
     * now it is. Bastard!
     * @param menu Menu item
     * @return true always
     */
    @Override public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater flate = getMenuInflater();
        flate.inflate(R.menu.sticky, menu);

        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        //Fucking toolbar...
        switch (item.getItemId())
        {
            case R.id.about_this :
                Intent a = new Intent("app.sticky_notes.ABOUT");
                a.putExtra("title", "A note taking app");
                a.putExtra("text", "A very simple note taking app.\n"
                        + "After adding, short click to edit,\n"
                        + "long click to delete, without notice unfortunately.\n");
                startActivity(a);
                break;

            case R.id.credit :
                Intent c = new Intent("app.sticky_notes.CREDIT");
                c.putExtra("title", "Sticky paper the note taker");
                c.putExtra("text", "Made by Chris Harte. Because he could!\n\n"
                        + "The note font is Note_this taken from fontsquirrel.\n");
                startActivity(c);
                break;

            // I do not have a home icon, as per the new diktat from Evil Google.
            case android.R.id.home :
                finish();
                break;

            case R.id.action_settings :
                Intent p = new Intent("app.sticky_notes.PREFS");
                startActivity(p);
                break;
        }
        return false;
    }

    /**
    * For structual changes ie added or removed, this from the manual
    * LayoutManagers will be forced to fully rebind and relayout all visible views.
    */
    private void load_adapter()
    {
        utils.open();
        notes = utils.get_notes();
        utils.close();

        //This is completely over the top but is how it is done.
        //The existing adapter is replaced with the new one.
        resyc_adapter = new Resyc_adapter(notes);
        recycler_view.setAdapter(resyc_adapter);
    }

    /**
     * Return the font size currently stored in preferences.
     *
     * @return the font size as an integer
     */
    private int get_font_size()
    {
        SharedPreferences get_prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String font_string = get_prefs.getString("fong", "24");
        font_size = Integer.parseInt(font_string);

        return font_size;
    }

    /**
     * New recycle view and adapter. No idea what was wrong with the listView adapter,
     * but this is the way we have to do it now.
     *
     * @author Christopher D. Harte
     */
    class Resyc_adapter extends RecyclerView.Adapter<Resyc_adapter.View_holder>
    {
        private ArrayList<Sticky_notes> local_data;

        class View_holder extends RecyclerView.ViewHolder
        {
            TextView txtNote;
            TextView txtTitle;

            View_holder (View note_view)
            {
                super(note_view);
                txtTitle = (TextView) note_view.findViewById(R.id.item_title);
                txtNote = (TextView) note_view.findViewById(R.id.item_note);
            }
        }

        /**
         * RecycleView adapter that holds the arrayList
         * @param data Sticky_notes
         */
        Resyc_adapter (ArrayList<Sticky_notes> data)
        {
            local_data = data;
        }

        @Override public Resyc_adapter.View_holder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater flater = LayoutInflater.from(parent.getContext());
            View_holder vh = new View_holder(flater.inflate(R.layout.note_item, parent, false));

            return vh;
        }

        @Override public void onBindViewHolder(final View_holder holder, final int position)
        {
            Sticky_notes temp_holder = local_data.get(position);

            Typeface fonts = Typeface.createFromAsset(getAssets(), "fonts/Note_this.ttf");

            String title = temp_holder.get_title();
            String note = temp_holder.get_note();

            /*
             * Use an intent to call an activity to edit the note
             */
            holder.txtNote.setOnClickListener(new OnClickListener()
            {
                @Override public void onClick(View view)
                {
                    String note_body = local_data.get(position).get_note();
                    record_no = Long.parseLong(local_data.get(position).get_row_id());

                    Intent i = new Intent("app.sticky_notes.EDIT_NOTE");
                    i.putExtra("fong", font_size);
                    i.putExtra("note", note_body);
                    i.putExtra("row_id", record_no);
                    startActivity(i);

                    notifyDataSetChanged();

                    holder.txtNote.setSelected(true);
                }
            });

            /*
             * Use my callback class to delete the note.
             * todo if the back arrow is clicked instead of the bin I need to return the colour to transparent
             */
            holder.txtNote.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override public boolean onLongClick(View view)
                {
                    holder.txtNote.setSelected(true);

                    record_no = Long.parseLong(local_data.get(position).get_row_id());

                    holder.txtNote.setBackgroundColor(Color.BLUE);

                    My_action_callback call_back_mode = new My_action_callback();
                    startActionMode(call_back_mode);

                    return true;
                }
            });

            holder.txtNote.setTypeface(fonts);
            holder.txtNote.setTextSize(font_size);
            holder.txtTitle.setText(title);
            holder.txtNote.setText(note);
        }

        @Override public int getItemCount()
        {
            return local_data.size();
        }
    }

    private class My_action_callback implements ActionMode.Callback
    {
        @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override public void onDestroyActionMode(ActionMode mode)
        {
            mode = null;
        }

        @Override public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            MenuInflater flate = mode.getMenuInflater();

            flate.inflate(R.menu.context_menu, menu);

            return true;
        }

        @Override public boolean onActionItemClicked(ActionMode mode, MenuItem menu)
        {
            boolean worked = true;

            switch (menu.getItemId())
            {
                case R.id.to_delete :
                    try
                    {
                        utils.open();
                        utils.delete_entry(record_no);
                        utils.close();

                        //For structual changes ie added or removed, this from the manual
                        //LayoutManagers will be forced to fully rebind and relayout all visible views.
                        load_adapter();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        worked = false;
                    }
                    mode.finish();
                break;
            }
            return worked;
        }
    }
}

