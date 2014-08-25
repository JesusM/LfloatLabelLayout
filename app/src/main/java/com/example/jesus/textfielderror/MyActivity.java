package com.example.jesus.textfielderror;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jesus.textfielderror.customui.FloatLabelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MyActivity extends Activity {

    private PlaceholderFragment placeholderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            placeholderFragment = new PlaceholderFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, placeholderFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_show:
                placeholderFragment.showEditText();
                break;
            case R.id.action_show_custom_icon:
                placeholderFragment.showEditTextWithCustomIcon();
                break;
            case R.id.action_clear:
                placeholderFragment.clearEditText();
                break;
            case R.id.action_enable:
                placeholderFragment.setEditTextEnabled(true);
                break;
            case R.id.action_disable:
                placeholderFragment.setEditTextEnabled(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @InjectView(R.id.editText)
        FloatLabelLayout editText;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            ButterKnife.inject(this, rootView);
            return rootView;
        }

        public void clearEditText() {
            editText.clearError();
        }

        public void showEditText() {
            editText.setError(getString(R.string.error_message_sample));
        }

        public void setEditTextEnabled(boolean enabled) {
            editText.setEnabled(enabled);
        }

        public void showEditTextWithCustomIcon() {
            editText.setError(getString(R.string.error_message),
                    getResources().getDrawable(R.drawable.ic_action_alerts_and_states_error));
        }
    }
}
