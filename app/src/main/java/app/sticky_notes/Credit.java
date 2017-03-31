package app.sticky_notes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Display the credit text.
 * Created by Christopher D. Harte on 13/07/2016.
 */
public class Credit extends Activity
{
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit);
        TextView credit = (TextView) findViewById(R.id.credit);

        Bundle b = getIntent().getExtras();
        String title = b.getString("title");
        String text = b.getString("text");

        setTitle(title);
        credit.setText(text);
    }
}
