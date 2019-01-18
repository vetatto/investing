package ru.vetatto.investing.investing.PifInfo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.vetatto.investing.investing.PifList.PifAdapter;
import ru.vetatto.investing.investing.PifInfo.protfolioPifInfo;
import ru.vetatto.investing.investing.PifList.PifData;
import ru.vetatto.investing.investing.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PifInfoOperationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PifInfoOperationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PifInfoOperationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String word;
    View view;
    String phones2;
    PifOperationAdapter adapter;
    private OnFragmentInteractionListener mListener;

    ArrayList<PifOperationData> phones = new ArrayList();
    public PifInfoOperationFragment() {
        // Required empty public constructor
    }

    public static PifInfoOperationFragment newInstance(String param1) {
        PifInfoOperationFragment fragment = new PifInfoOperationFragment();
        Bundle args = new Bundle();
        args.putString("array", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TEST_FRAGMENT", "onCreate");
        if (getArguments() != null) {
            phones2 = getArguments().getString("array");
            mParam2 = getArguments().getString(ARG_PARAM2);
            Log.d("TEST_FRAGMENT",phones2);
        try {
            JSONArray dataJsonArray;
            JSONObject dataJsonObj;
            dataJsonObj = new JSONObject(phones2);
            dataJsonArray= dataJsonObj.getJSONArray("operations");
            Log.d("TEST_FRAGMENT",dataJsonArray.toString());
            for (int i = 0; i < dataJsonArray.length(); i++) {
                JSONObject dataJsonObj3 =  dataJsonArray.getJSONObject(i);
                int type_operation = dataJsonObj3.getInt("type_operation");
                phones.add(new PifOperationData( "test", "", 123, type_operation,"123", 123, 123,"Test", "123",123, 1, "text", 123));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TEST_FRAGMENT","ERROR:"+e.getMessage());
        }
        }
        else{
            Log.d("TEST_FRAGMENT","NULL");
        }


        // Inflate the layout for this fragment
      view = inflater.inflate(R.layout.fragment_pif_info_operation,
                container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RVOperation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new PifOperationAdapter(this.getContext(),phones);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        TextView text = view.findViewById(R.id.textView20);
        text.setText("test");
        Log.d("TEST_FRAGMENT", "onCreateView");
      //  Log.d("TEST_FRAGMENT", "PHONES:"+phones.toString());
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("TEST_FRAGMENT", "onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void sayMeow(ArrayList<PifOperationData> phones2) {

        Log.d("TEST_FRAGMENT", "function");
        //adapter.notifyDataSetChanged();
    }
}
