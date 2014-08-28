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


public class SampleActivity extends Activity {

    private SampleFragment sampleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textfield_sample);
        if (savedInstanceState == null) {
            sampleFragment = new SampleFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, sampleFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_show:
                sampleFragment.showEditText();
                break;
            case R.id.action_show_custom_icon:
                sampleFragment.showEditTextWithCustomIcon();
                break;
            case R.id.action_clear:
                sampleFragment.clearEditText();
                break;
            case R.id.action_enable:
                sampleFragment.setEditTextEnabled(true);
                break;
            case R.id.action_disable:
                sampleFragment.setEditTextEnabled(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SampleFragment extends Fragment {

        @InjectView(R.id.floatingLabelLayout)
        FloatLabelLayout floatLabelLayout;

        public SampleFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_textfield_sample, container, false);
            ButterKnife.inject(this, rootView);
            initEditText();
            return rootView;
        }

        private void initEditText() {
            floatLabelLayout.setHint(getString(R.string.enter_your_text));
        }

        public void clearEditText() {
            floatLabelLayout.clearError();
        }

        public void showEditText() {
            floatLabelLayout.setError(getString(R.string.error_message_sample));
        }

        public void setEditTextEnabled(boolean enabled) {
            floatLabelLayout.setEnabled(enabled);
        }

        public void showEditTextWithCustomIcon() {
            floatLabelLayout.setError(getString(R.string.error_message_sample),
                    getResources().getDrawable(R.drawable.ic_action_alerts_and_states_error));
        }
    }
}
