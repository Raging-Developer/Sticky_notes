package app.sticky_notes;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Save the preferences.
 * Created by Christopher D. Harte on 13/07/2016.
 * SharedPreferences are being replaced by dataStores, which are
 * currently only written in kotlin. So get learning.
 */
public class Prefs extends PreferenceActivity
{
    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        getFragmentManager().beginTransaction().replace(android.R.id.content, new Pref_frag()).commit();

        //All things being equal, this looks like it makes more sense.
        Pref_frag p = new Pref_frag();
        FragmentTransaction frag_man = getFragmentManager().beginTransaction();

        frag_man.replace(android.R.id.content, p);
        frag_man.commit();
    }

    public static class Pref_frag extends PreferenceFragment
    {
        @Override public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.prefs);
        }
    }
}
