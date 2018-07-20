package com.example.gaominyu.slease.Main;

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

import com.example.gaominyu.slease.Model.ItemPreview;
import com.example.gaominyu.slease.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ItemPreview> itemPreviewList;
    private List<String> itemIDs;
    private ItemAdapter mAdapter;
    private DatabaseReference FirebaseDatabaseItem;
    private FirebaseUser user;
    private String userId;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = view.findViewById(R.id.recycler_profile);
        itemPreviewList = new ArrayList<>();
        mAdapter = new ItemAdapter(getActivity(), itemPreviewList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        fetchLeasedItemsToLocal();

        return view;
    }

    private void fetchLeasedItemsToLocal() {

        if(user != null) {

            userId = user.getUid();
            itemIDs = new ArrayList<>();
            FirebaseDatabaseItem = FirebaseDatabase.getInstance().getReference("items").child(userId);
            FirebaseDatabaseItem.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                itemIDs.add(item.getKey());
                            }

                            fetchLeasedItemPreviews();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }
                    });
        }
    }

    private void fetchLeasedItemPreviews() {
        for (int i = 0; i < itemIDs.size(); i++) {
            if(i == itemIDs.size() - 1) {

                FirebaseDatabase.getInstance().getReference("items_preview").child(itemIDs.get(i)).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                itemPreviewList.add(dataSnapshot.getValue(ItemPreview.class));

                                // refreshing recycler view at last
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Fetch Items done", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });

            } else {

                FirebaseDatabase.getInstance().getReference("items_preview").child(itemIDs.get(i)).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                itemPreviewList.add(dataSnapshot.getValue(ItemPreview.class));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });

            }

        }
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
