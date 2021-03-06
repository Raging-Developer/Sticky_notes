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
        String title = "";
        String text = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit);
        TextView credit = findViewById(R.id.credit);

        Bundle b = getIntent().getExtras();
        if (b != null)
        {
            title = b.getString("title");
            text = b.getString("text");
        }

        setTitle(title);
        credit.setText(text);
    }
}
