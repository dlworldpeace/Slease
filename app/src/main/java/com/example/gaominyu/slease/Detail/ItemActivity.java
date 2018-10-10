package com.example.gaominyu.slease.Detail;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.gaominyu.slease.Model.Item;
import com.example.gaominyu.slease.Model.User;
import com.example.gaominyu.slease.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ItemActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout sliderLayout;
    private String userId;
    private String itemId;
    private Item item;
    private DatabaseReference FirebaseDatabaseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Initialize view elements
        sliderLayout = findViewById(R.id.image_slider);

        // Retrieve current item
        Intent intent = getIntent();
        userId = intent.getStringExtra("userID");
        itemId = intent.getStringExtra("userID");
        FirebaseDatabaseItem = FirebaseDatabase.getInstance().getReference("users").child(userId).child(itemId);
        if(FirebaseDatabaseItem == null){
            Toast.makeText(getApplication(), "item does not exist anymore", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabaseItem.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Finish retrieving
                        item = dataSnapshot.getValue(Item.class);

                        // Initialize slider layout for images of current item
                        HashMap<String,String> url_maps = new HashMap<>();
                        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
                        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
                        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
                        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

                        HashMap<String,Integer> file_maps = new HashMap<>();
                        file_maps.put("Hannibal",R.drawable.activity);
                        file_maps.put("Big Bang Theory",R.drawable.browse);
                        file_maps.put("House of Cards",R.drawable.camera);
                        file_maps.put("Game of Thrones", R.drawable.cross16);

                        for(String name : file_maps.keySet()){
                            TextSliderView textSliderView = new TextSliderView(ItemActivity.this);
                            // initialize a SliderLayout
                            textSliderView.description(name)
                                    .image(file_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(ItemActivity.this);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);

                            sliderLayout.addSlider(textSliderView);
                        }

                        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        sliderLayout.setCustomAnimation(new DescriptionAnimation());
                        sliderLayout.setDuration(4000);
                        sliderLayout.addOnPageChangeListener(ItemActivity.this);
                        ListView l = findViewById(R.id.transformers);
                        l.setAdapter(new TransformerAdapter(ItemActivity.this));
                        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                sliderLayout.setPresetTransformer(((TextView) view).getText().toString());
                                Toast.makeText(ItemActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_custom_indicator:
//                sliderLayout.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
//                break;
//            case R.id.action_custom_child_animation:
//                sliderLayout.setCustomAnimation(new ChildAnimationExample());
//                break;
//            case R.id.action_restore_default:
//                sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                sliderLayout.setCustomAnimation(new DescriptionAnimation());
//                break;
//            case R.id.action_github:
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
//                startActivity(browserIntent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
