package com.example.royma.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    // ArrayAdapter initialised outside of methods
    ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
        inflater.inflate(R.menu.viewlocation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Reloads forecast list
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ArrayAdapter takes data from a source and creates a view that represents
        // each data entry (populates gridView)
        mForecastAdapter = new ArrayAdapter<>(
                getActivity(),  // Context (fragment's parent activity)
                R.layout.list_item_forecast,    // ID of list item layout
                R.id.list_item_forecast_textview,   // ID of textView to populate
                new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Attaches adapter to view
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        // Attach onClick listener to items in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Gets item at current position in Adapter (weather for selected day)
                String forecast = mForecastAdapter.getItem(position);
                // Launch Detail activity with selected forecast passed as an extra
                Intent detailIntent = new Intent(getContext(), DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    private void updateWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
        // Retrieve user preferred location. Use default if none found
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String locationPref = sharedPref.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        weatherTask.execute(locationPref);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

}
