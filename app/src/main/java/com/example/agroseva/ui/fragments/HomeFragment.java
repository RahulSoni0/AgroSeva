package com.example.agroseva.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.agroseva.data.home.ModelHomeItem;
import com.example.agroseva.databinding.FragmentHomeBinding;
import com.example.agroseva.ui.adapters.HomePosterAdapter;
import com.example.agroseva.ui.adapters.HomeRvAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<String> homePosterList = new ArrayList<>();
    ArrayList<String> arrangedPosterList = new ArrayList<>();
    FirebaseFirestore firestore;
    HomePosterAdapter posterAdapter;
    int currentPage;
    final static int DELAY_TIME = 1500;
    final static int PERIOD_TIME = 1500;
    Timer timer;

    List<ModelHomeItem> commonlist;
    List<ModelHomeItem> l1;
    List<ModelHomeItem> l2;
    List<ModelHomeItem> l3;
    List<ModelHomeItem> l4;
    List<ModelHomeItem> l5;

    HomeRvAdapter a1;
    HomeRvAdapter a2;
    HomeRvAdapter a3;
    HomeRvAdapter a4;
    HomeRvAdapter a5;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = ProgressDialog.show(requireContext(), "Loading", "Please wait...", true);


        firestore = FirebaseFirestore.getInstance();

        //hardcoded url's for sliders
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F1.png?alt=media&token=37b2697c-02ce-4cb5-bf05-c55b09789afb");
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F2.png?alt=media&token=b49f3930-e908-4d43-8803-cafe4751a9b3");
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F3.png?alt=media&token=cd708712-bda2-4cd2-a923-118d14f39224");
        //end
        setPosters(homePosterList); //calling poster slider function

        commonlist = new ArrayList<>();
        l1 = new ArrayList<>();
        l2 = new ArrayList<>();
        l3 = new ArrayList<>();
        l4 = new ArrayList<>();
        l5 = new ArrayList<>();


        a1 = new HomeRvAdapter(l1, requireContext());
        a2 = new HomeRvAdapter(l2, requireContext());
        a3 = new HomeRvAdapter(l3, requireContext());
        a4 = new HomeRvAdapter(l4, requireContext());
        a5 = new HomeRvAdapter(l5, requireContext());
        initData();

        binding.rvLinks.setHasFixedSize(true);
        binding.rvLinks.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvLinks.setAdapter(a1);


        binding.rvInitiatives.setHasFixedSize(true);
        binding.rvInitiatives.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvInitiatives.setAdapter(a2);

        binding.rvScheme.setHasFixedSize(true);
        binding.rvScheme.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvScheme.setAdapter(a3);

        binding.rvNews.setHasFixedSize(true);
        binding.rvNews.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvNews.setAdapter(a4);

        binding.rvArticles.setHasFixedSize(true);
        binding.rvArticles.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvArticles.setAdapter(a5);


    }

    private void initData() {

        // firebase...

        firestore.collection("homepage").document("5H9srNYTfEA50OjbdSCd").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<ModelHomeItem> s1 = (List<ModelHomeItem>) documentSnapshot.get("links");
                    List<ModelHomeItem> s2 = (List<ModelHomeItem>) documentSnapshot.get("initiatives");
                    List<ModelHomeItem> s3 = (List<ModelHomeItem>) documentSnapshot.get("schemes");
                    List<ModelHomeItem> s4 = (List<ModelHomeItem>) documentSnapshot.get("news");
                    List<ModelHomeItem> s5 = (List<ModelHomeItem>) documentSnapshot.get("articles");


                    for (int index = 0; index < s1.size(); index++) {
                        HashMap<Integer, Object> tempData = new HashMap<>();
                        tempData.putAll((Map<? extends Integer, ? extends Object>) s1.get(index));
                        if (tempData != null) {

                            ModelHomeItem temp = new ModelHomeItem();
                            temp.setTitle((String) tempData.get("title"));
                            temp.setDesc((String) tempData.get("desc"));
                            temp.setImage_url((String) tempData.get("image_url"));
                            temp.setUrl((String) tempData.get("url"));
                            l1.add(temp);
                        }
                    }
                    for (int index = 0; index < s2.size(); index++) {
                        HashMap<Integer, Object> tempData = new HashMap<>();
                        tempData.putAll((Map<? extends Integer, ? extends Object>) s2.get(index));
                        if (tempData != null) {

                            ModelHomeItem temp = new ModelHomeItem();
                            temp.setTitle((String) tempData.get("title"));
                            temp.setDesc((String) tempData.get("desc"));
                            temp.setImage_url((String) tempData.get("image_url"));
                            temp.setUrl((String) tempData.get("url"));
                            l2.add(temp);
                        }
                    }
                    for (int index = 0; index < s3.size(); index++) {
                        HashMap<Integer, Object> tempData = new HashMap<>();
                        tempData.putAll((Map<? extends Integer, ? extends Object>) s3.get(index));
                        if (tempData != null) {

                            ModelHomeItem temp = new ModelHomeItem();
                            temp.setTitle((String) tempData.get("title"));
                            temp.setDesc((String) tempData.get("desc"));
                            temp.setImage_url((String) tempData.get("image_url"));
                            temp.setUrl((String) tempData.get("url"));
                            l3.add(temp);
                        }
                    }
                    for (int index = 0; index < s4.size(); index++) {
                        HashMap<Integer, Object> tempData = new HashMap<>();
                        tempData.putAll((Map<? extends Integer, ? extends Object>) s4.get(index));
                        if (tempData != null) {

                            ModelHomeItem temp = new ModelHomeItem();
                            temp.setTitle((String) tempData.get("title"));
                            temp.setDesc((String) tempData.get("desc"));
                            temp.setImage_url((String) tempData.get("image_url"));
                            temp.setUrl((String) tempData.get("url"));
                            l4.add(temp);
                        }
                    }
                    for (int index = 0; index < s5.size(); index++) {
                        HashMap<Integer, Object> tempData = new HashMap<>();
                        tempData.putAll((Map<? extends Integer, ? extends Object>) s5.get(index));
                        if (tempData != null) {

                            ModelHomeItem temp = new ModelHomeItem();
                            temp.setTitle((String) tempData.get("title"));
                            temp.setDesc((String) tempData.get("desc"));
                            temp.setImage_url((String) tempData.get("image_url"));
                            temp.setUrl((String) tempData.get("url"));
                            l5.add(temp);
                        }
                    }




                    a1.notifyDataSetChanged();


                    a2.notifyDataSetChanged();


                    a3.notifyDataSetChanged();


                    a4.notifyDataSetChanged();


                    a5.notifyDataSetChanged();



                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });


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
        binding.vpHomeBanner.setAdapter(posterAdapter);
        binding.vpHomeBanner.setPageMargin(56);

        binding.vpHomeBanner.setCurrentItem(currentPage);
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

        binding.vpHomeBanner.addOnPageChangeListener(listener);
        binding.vpHomeBanner.setOnTouchListener(new View.OnTouchListener() {
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
            binding.vpHomeBanner.setCurrentItem(currentPage, false);

        } else if (currentPage == 0) {
            currentPage = l.size() - 2;
            binding.vpHomeBanner.setCurrentItem(currentPage, false);
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

                binding.vpHomeBanner.setCurrentItem(currentPage++, true);

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
}
