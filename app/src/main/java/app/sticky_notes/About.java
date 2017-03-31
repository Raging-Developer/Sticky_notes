package app.sticky_notes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Display the aboot text.
 * Created by Christopher D. Harte on 13/07/2016.
 */
public class About extends Activity
{
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView about_text = (TextView)findViewById(R.id.about_text);

        Bundle b = getIntent().getExtras();
        String title = b.getString("title");
        String text = b.getString("text");

        setTitle(title);
        about_text.setText(text);
    }
}