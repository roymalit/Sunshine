package com.example.royma.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mForecastAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Add forecast elements
        String [] data = {
                "Today - Sunny - 26/15",
                "Tomorrow - Rainy - 20/11",
                "Wednesday - Sunny - 28/18",
                "Thursday - Cloudy - 23/13",
                "Friday - Foggy - 15/8",
        };

        // Initialise array list
        List<String> forecast_arraylist = new ArrayList<String>(Arrays.asList(data));


        // ArrayAdapter takes data from a source and creates a view that represents
        // each data entry (populates listView)

        mForecastAdapter = new ArrayAdapter<String>(
                // Context (fragment's parent activity)
                getActivity(),
                // ID of list item layout
                R.layout.list_item_forecast,
                // ID of textView to populate
                R.id.list_item_forecast_textview,
                //Forecast data
                forecast_arraylist);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }
}
