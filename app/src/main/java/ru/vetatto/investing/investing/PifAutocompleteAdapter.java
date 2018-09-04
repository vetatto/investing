package ru.vetatto.investing.investing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class PifAutocompleteAdapter extends ArrayAdapter<PifAutocompleteData> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<PifAutocompleteData> items;
    ArrayList<PifAutocompleteData> tempItems;
    ArrayList<PifAutocompleteData> suggestions;

    public PifAutocompleteAdapter(Context context, int resource, int textViewResourceId, ArrayList<PifAutocompleteData> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        suggestions = new ArrayList<PifAutocompleteData>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pif_spinner, parent, false);
        }
        PifAutocompleteData people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.UkNameLabel);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        return view;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.uk_autocomplete, parent, false);
        }
        PifAutocompleteData people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.UkNameLabel);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        return view;
    }
    }
