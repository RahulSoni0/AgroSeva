package com.example.agroseva.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.agroseva.databinding.FragmentFasalDarpanBinding;
import com.example.agroseva.ui.adapters.CropAdapter;
import com.example.agroseva.ui.adapters.HomePosterAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FasalDarpanFragment extends Fragment {
    FragmentFasalDarpanBinding binding;
    private FirebaseFirestore firestore;
    ArrayList<String> homePosterList = new ArrayList<>();
    ArrayList<String> arrangedPosterList = new ArrayList<>();
    HomePosterAdapter posterAdapter;
    int currentPage;
    final static int DELAY_TIME = 1500;
    final static int PERIOD_TIME = 1500;
    Timer timer;

    CropAdapter adapter;
    List<String> data = new ArrayList<>();
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFasalDarpanBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //hardcoded url's for sliders
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F4.png?alt=media&token=3b4d3f04-cd82-420a-a4d8-9971268b58fa");
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F5.png?alt=media&token=1896a29f-f41a-4237-a826-19fb15d4506d");
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F6.png?alt=media&token=2d052f68-1def-4b96-a268-9c68802b304e");
        //end
        setPosters(homePosterList); //calling poster slider function
        firestore = FirebaseFirestore.getInstance();


        adapter = new CropAdapter(data, requireContext());

        data.addAll(Andhra_Pradesh);
        binding.rvCrops.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.rvCrops.setLayoutManager(layoutManager);
        binding.rvCrops.setAdapter(adapter);


        binding.crops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), selectedValue, Toast.LENGTH_SHORT).show();
                data = new ArrayList<>();
                switch (selectedValue) {
                    case "Andhra_Pradesh":
                        data.addAll(Andhra_Pradesh);

                        break;
                    case "Arunachal_Pradesh":
                        data.addAll(Arunachal_Pradesh);

                        break;
                    case "Assam":
                        data.addAll(Assam);

                        break;
                    case "Bihar":
                        data.addAll(Bihar);

                        break;
                    case "Chhattisgarh":
                        data.addAll(Chhattisgarh);

                        break;
                    case "Goa":
                        data.addAll(Goa);

                        break;
                    case "Gujarat":
                        data.addAll(Gujarat);

                        break;
                    case "Haryana":
                        data.addAll(Haryana);

                        break;
                    case "Himachal_Pradesh":
                        data.addAll(Himachal_Pradesh);

                        break;
                    case "Jharkhand":
                        data.addAll(Jharkhand);

                        break;
                    case "Karnataka":
                        data.addAll(Karnataka);

                        break;
                    case "Kerala":
                        data.addAll(Kerala);

                        break;
                    case "Madhya_Pradesh":
                        data.addAll(Madhya_Pradesh);

                        break;
                    case "Maharashtra":
                        data.addAll(Maharashtra);

                        break;
                    case "Manipur":
                        data.addAll(Manipur);

                        break;
                    case "Meghalaya":
                        data.addAll(Meghalaya);

                        break;
                    case "Mizoram":
                        data.addAll(Mizoram);

                        break;
                    case "Nagaland":
                        data.addAll(Nagaland);

                        break;
                    case "Odisha":
                        data.addAll(Odisha);

                        break;
                    case "Punjab":
                        data.addAll(Punjab);

                        break;
                    case "Rajasthan":
                        data.addAll(Rajasthan);

                        break;
                    case "Sikkim":
                        data.addAll(Sikkim);

                        break;
                    case "Tamil_Nadu":
                        data.addAll(Tamil_Nadu);

                        break;
                    case "Telangana":
                        data.addAll(Telangana);

                        break;
                    case "Tripura":
                        data.addAll(Tripura);

                        break;
                    case "Uttar_Pradesh":
                        data.addAll(Uttar_Pradesh);

                        break;
                    case "Uttarakhand":
                        data.addAll(Uttarakhand);
                        break;
                    case "West_Bengal":
                        data.addAll(West_Bengal);
                        break;
                    default:
                        // handle default case if none of the above match
                        break;
                }
                adapter.notifyDataSetChanged();
            }


        });

        postData();

    }

    private void setPosters(ArrayList<String> l) {
        if (timer != null) {
            timer.cancel();

        }

        currentPage = 1;  //-->first url of main list
        //applying logic to make sliders infinite so modifying list
        for (int i = 0; i < l.size(); i++) {
            arrangedPosterList.add(l.get(i));
        }

        arrangedPosterList.add(0, l.get(l.size() - 1));
        arrangedPosterList.add(l.get(0));

        //setting Adapter for poster Slider
        posterAdapter = new HomePosterAdapter(arrangedPosterList);
        binding.vpfasalBanner.setAdapter(posterAdapter);
        binding.vpfasalBanner.setPageMargin(56);

        binding.vpfasalBanner.setCurrentItem(currentPage);
        //end

        autoSliding(arrangedPosterList);

        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {

                    loopingInfinitely(arrangedPosterList);

                }

            }
        };

        binding.vpfasalBanner.addOnPageChangeListener(listener);
        binding.vpfasalBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loopingInfinitely(arrangedPosterList);
                stopSlidingOnTouch();

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    autoSliding(arrangedPosterList);

                }
                return false;
            }
        });

    }

    private void loopingInfinitely(ArrayList<String> l) {

        if (currentPage == l.size() - 1) {

            currentPage = 1;
            binding.vpfasalBanner.setCurrentItem(currentPage, false);

        } else if (currentPage == 0) {
            currentPage = l.size() - 2;
            binding.vpfasalBanner.setCurrentItem(currentPage, false);
        }

    }

    private void autoSliding(ArrayList<String> l) {

        final Handler handler = new Handler();
        final Runnable run = new Runnable() {
            @Override
            public void run() {

                if (currentPage >= l.size()) {

                    currentPage = 0;

                }

                binding.vpfasalBanner.setCurrentItem(currentPage++, true);

            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(run);

            }
        }, DELAY_TIME, PERIOD_TIME);

    }

    private void stopSlidingOnTouch() {

        timer.cancel();


    }

    public void postData() {

        // to be fetced from server...


//        ArrayList<List<String>> crops = new ArrayList<>();
//        crops.add(Andhra_Pradesh);
//        crops.add(Arunachal_Pradesh);
//        crops.add(Assam);
//        crops.add(Bihar);
//        crops.add(Chhattisgarh);
//        crops.add(Goa);
//        crops.add(Gujarat);
//        crops.add(Haryana);
//        crops.add(Himachal_Pradesh);
//        crops.add(Jharkhand);
//        crops.add(Karnataka);
//        crops.add(Kerala);
//        crops.add(Madhya_Pradesh);
//        crops.add(Maharashtra);
//        crops.add(Manipur);
//        crops.add(Meghalaya);
//        crops.add(Mizoram);
//        crops.add(Nagaland);
//        crops.add(Odisha);
//        crops.add(Punjab);
//        crops.add(Rajasthan);
//        crops.add(Sikkim);
//        crops.add(Tamil_Nadu);
//        crops.add(Telangana);
//        crops.add(Tripura);
//        crops.add(Uttar_Pradesh);
//        crops.add(Uttarakhand);
//        crops.add(West_Bengal);
//
//
//        firestore.collection("crops").document("p100TqDdYunHE6QL65pB").set(crops);


    }

    static List<String> Andhra_Pradesh = Arrays.asList("Rice", "Sugarcane", "Tobacco", "Cotton", "Chillies", "Groundnut", "Turmeric", "Oilseeds");
    static List<String> Arunachal_Pradesh = Arrays.asList("Rice", "Maize", "Millets", "Fruits");
    static List<String> Assam = Arrays.asList("Tea", "Jute", "Oilseeds", "Sugarcane", "Cotton");
    static List<String> Bihar = Arrays.asList("Rice", "Sugarcane", "Tobacco", "Jute", "Oilseeds", "Maize");
    static List<String> Chhattisgarh = Arrays.asList("Rice", "Sugarcane", "Oilseeds", "Pulses", "Maize");
    static List<String> Goa = Arrays.asList("Rice", "Cashew", "Coconut");
    static List<String> Gujarat = Arrays.asList("Cotton", "Groundnut", "Sesame", "Castor", "Cumin", "Tobacco");
    static List<String> Haryana = Arrays.asList("Wheat", "Rice", "Sugarcane", "Jute", "Oilseeds");
    static List<String> Himachal_Pradesh = Arrays.asList("Apples", "Rice", "Maize", "Wheat");
    static List<String> Jharkhand = Arrays.asList("Rice", "Maize", "Pulses", "Oilseeds", "Fruits");
    static List<String> Karnataka = Arrays.asList("Rice", "Sugarcane", "Cotton", "Tobacco", "Groundnut", "Oilseeds");
    static List<String> Kerala = Arrays.asList("Rubber", "Coconut", "Tea", "Coffee", "Spices");
    static List<String> Madhya_Pradesh = Arrays.asList("Wheat", "Soybean", "Sugarcane", "Cotton", "Oilseeds");
    static List<String> Maharashtra = Arrays.asList("Sugarcane", "Cotton", "Oilseeds", "Groundnut", "Soybean", "Rice");
    static List<String> Manipur = Arrays.asList("Rice", "Maize", "Pulses", "Oilseeds", "Fruits");
    static List<String> Meghalaya = Arrays.asList("Rice", "Maize", "Oilseeds", "Fruits", "Spices");
    static List<String> Mizoram = Arrays.asList("Rice", "Maize", "Pulses", "Oilseeds", "Fruits");
    static List<String> Nagaland = Arrays.asList("Rice", "Maize", "Pulses", "Oilseeds", "Fruits");
    static List<String> Odisha = Arrays.asList("Rice", "Sugarcane", "Tobacco", "Cotton", "Jute", "Oilseeds");
    static List<String> Punjab = Arrays.asList("Wheat", "Rice", "Cotton", "Sugarcane", "Maize");
    static List<String> Rajasthan = Arrays.asList("Wheat", "Cotton", "Groundnut", "Sesame", "Mustard");
    static List<String> Sikkim = Arrays.asList("Rice", "Maize", "Wheat", "Vegetables");
    static List<String> Tamil_Nadu = Arrays.asList("Rice", "Sugarcane", "Cotton", "Tea", "Coffee");
    static List<String> Telangana = Arrays.asList("Rice", "Sugarcane", "Cotton", "Groundnut", "Maize");
    static List<String> Tripura = new ArrayList<>(Arrays.asList("Rice", "Maize", "Pulses", "Oilseeds", "Fruits"));
    static List<String> Uttar_Pradesh = new ArrayList<>(Arrays.asList("Wheat", "Rice", "Sugarcane", "Tobacco", "Cotton", "Jute", "Oilseeds"));
    static List<String> Uttarakhand = new ArrayList<>(Arrays.asList("Rice", "Wheat", "Maize", "Fruits"));
    static List<String> West_Bengal = new ArrayList<>(Arrays.asList("Rice", "Tea", "Jute", "Sugarcane", "Tobacco"));
}


