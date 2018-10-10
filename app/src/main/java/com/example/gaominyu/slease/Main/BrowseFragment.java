package com.example.gaominyu.slease.Main;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gaominyu.slease.Detail.ItemActivity;
import com.example.gaominyu.slease.Model.ItemPreview;
import com.example.gaominyu.slease.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseFragment extends Fragment {

    //private static final String TAG = BrowseFragment.class.getSimpleName();
    //private static final String URL = "https://api.androidhive.info/json/movies_2017.json";

    private RecyclerView recyclerView;
    private List<ItemPreview> itemPreviewList;
    private ItemAdapter mAdapter;
    private DatabaseReference FirebaseDatabaseItemPreview;

    public BrowseFragment() {
        // Required empty public constructor
    }

    public static BrowseFragment newInstance(String param1, String param2) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        itemPreviewList = new ArrayList<>();
        mAdapter = new ItemAdapter(getActivity(), itemPreviewList, new ItemAdapter.OnItemClickListener() {
            @Override public void onItemClick(ItemPreview itemPreview) {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra("userID", itemPreview.userId);
                intent.putExtra("itemID", itemPreview.itemId);
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

//        //Tried these to reduce Jank cases
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemViewCacheSize(10);
//        recyclerView.setDrawingCacheEnabled(true);
//        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        fetchLeasedItemsToLocal();

        return view;
    }

    private void fetchLeasedItemsToLocal() {
//        JsonArrayRequest request = new JsonArrayRequest(URL,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        if (response == null) {
//                            Toast.makeText(getActivity(), "Couldn't fetch the leased items! Pleas try again.", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        List<ItemPreview> itemPreviews = new Gson().fromJson(response.toString(), new TypeToken<List<ItemPreview>>() {
//                        }.getType());
//
//                        itemPreviewList.clear();
//                        itemPreviewList.addAll(itemPreviews);
//
//                        // refreshing recycler view
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error in getting json
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        VolleySingleton.getInstance().addToRequestQueue(request);

        FirebaseDatabaseItemPreview = FirebaseDatabase.getInstance().getReference("items_preview");
        FirebaseDatabaseItemPreview.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot previewData : dataSnapshot.getChildren()) {
                            itemPreviewList.add(previewData.getValue(ItemPreview.class));
                        }
                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Fetch Items done", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
