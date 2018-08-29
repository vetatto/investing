package ru.vetatto.investing.investing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UkAutocompleteAdapter extends ArrayAdapter<UkAutocompleteData> {

    Context context;
    int resource, textViewResourceId;
    List<UkAutocompleteData> items, tempItems, suggestions;

    public UkAutocompleteAdapter(Context context, int resource, int textViewResourceId, List<UkAutocompleteData> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<UkAutocompleteData>(items); // this makes the difference.
        suggestions = new ArrayList<UkAutocompleteData>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.uk_autocomplete, parent, false);
        }
        UkAutocompleteData people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.UkNameLabel);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((UkAutocompleteData) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (UkAutocompleteData people : items) {
                    if (people.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<UkAutocompleteData> filterList = (ArrayList<UkAutocompleteData>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (UkAutocompleteData people : filterList) {
                    add(people);
                }
                    notifyDataSetChanged();
                }
            }
    };
}