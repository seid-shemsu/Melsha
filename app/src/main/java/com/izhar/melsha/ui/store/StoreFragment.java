package com.izhar.melsha.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.izhar.melsha.R;
import com.izhar.melsha.adapters.TabAdapter;
import com.izhar.melsha.ui.loan.loans.LoanTabAdapter;

public class StoreFragment extends Fragment {
    /*private FloatingActionButton fab;
    private RecyclerView recycler;
    private TextView no_store;
    private List<StoreModel> stores = new ArrayList<>();
    private StoresAdapter adapter;
    private ProgressBar progress;*/
    View root;
    TabAdapter tabAdapter;
    TabLayout tab;
    ViewPager view;
    View mIndicator;
    private int indicatorWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_store, container, false);
        /*fab = root.findViewById(R.id.fab);
        recycler = root.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        no_store = root.findViewById(R.id.no_store);
        progress = root.findViewById(R.id.progress);

        fab.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            dialog.setContentView(R.layout.form_add_store);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            EditText name = dialog.findViewById(R.id.name);
            Button add = dialog.findViewById(R.id.add);
            ProgressBar progress = dialog.findViewById(R.id.progress);
            add.setOnClickListener(v1 -> {
                if (name.getText().toString().length() > 0){
                    name.setEnabled(false);
                    add.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    String id = FirebaseDatabase.getInstance().getReference("Stores").push().getKey();
                    StoreModel store = new StoreModel();
                    store.setId(id);
                    store.setName(name.getText().toString());
                    FirebaseDatabase.getInstance().getReference("Stores").child(id).setValue(store)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Store Added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                }
                else
                    Toast.makeText(getContext(), "enter store name", Toast.LENGTH_SHORT).show();
            });
        });
        FirebaseDatabase.getInstance().getReference("Stores")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            no_store.setVisibility(View.GONE);
                            stores.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                stores.add(snapshot1.getValue(StoreModel.class));
                            }
                            adapter = new StoresAdapter(getContext(), stores);
                            recycler.setAdapter(adapter);
                        }
                        else {
                            no_store.setVisibility(View.VISIBLE);
                        }
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
        tab = root.findViewById(R.id.tab);
        view = root.findViewById(R.id.viewpager);

        mIndicator = root.findViewById(R.id.indicator);
        tabAdapter = new TabAdapter(getFragmentManager(), tab.getTabCount());
        view.setAdapter(tabAdapter);
        tab.setupWithViewPager(view);
        //view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+position) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab.post(() -> {
            indicatorWidth = tab.getWidth() / tab.getTabCount();
            //Assign new width
            FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
            indicatorParams.width = indicatorWidth;
            mIndicator.setLayoutParams(indicatorParams);
        });
        return root;
    }
}