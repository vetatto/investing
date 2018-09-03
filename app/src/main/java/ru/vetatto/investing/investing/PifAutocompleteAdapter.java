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

    @Override
    public Filter getFilter() {
        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = ((PifAutocompleteData) resultValue).getName();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (tempItems == null) {
                    tempItems = new ArrayList<PifAutocompleteData>(items);
                }
                if (constraint == null || constraint.length() == 0) {
                    ArrayList<PifAutocompleteData> list = new ArrayList<PifAutocompleteData>(tempItems);
                    filterResults.values = list;
                    filterResults.count = list.size();
                } else {
                    suggestions.clear();
                    for (PifAutocompleteData people : tempItems) {
                        if (people.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(people);
                        }
                    }
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<PifAutocompleteData> filter = (ArrayList<PifAutocompleteData>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (PifAutocompleteData cust : filter) {
                        add(cust);
                        notifyDataSetChanged();
                    }
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return nameFilter;
    }
}
