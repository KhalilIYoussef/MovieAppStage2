package khaliliyoussef.copyvideo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
**
 *
 * Created by Khalil on 6/8/2017.
 */

public class SettingsActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();

    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefrence);
        }
    }

}
