package com.example.agroseva.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.agroseva.databinding.FragmentPashuSampadaBinding;
import com.example.agroseva.ui.activities.LiveStockDetailsActivity;
import com.example.agroseva.ui.adapters.HomePosterAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PashuSampadaFragment extends Fragment {
    FragmentPashuSampadaBinding binding;
    ArrayList<String> homePosterList = new ArrayList<>();
    ArrayList<String> arrangedPosterList = new ArrayList<>();
    HomePosterAdapter posterAdapter;
    int currentPage;
    final static int DELAY_TIME = 1500;
    final static int PERIOD_TIME = 1500;
    Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPashuSampadaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), LiveStockDetailsActivity.class);
                startActivity(i);
            }
        });

        //hardcoded url's for sliders
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F7.png?alt=media&token=586ac5e3-0ec1-4324-a845-21450ab6ffe6");
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F8.png?alt=media&token=37a0f6b5-3959-4e80-86b2-f02845013e6a");
        homePosterList.add("https://firebasestorage.googleapis.com/v0/b/agroseva-4fdc8.appspot.com/o/banner%2F9.png?alt=media&token=c2c6aee0-2e3a-40c4-a42d-e8cbf54eb9ea");
        //end
        setPosters(homePosterList); //calling poster slider function
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
        binding.vpPashuBanner.setAdapter(posterAdapter);
        binding.vpPashuBanner.setPageMargin(56);

        binding.vpPashuBanner.setCurrentItem(currentPage);
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

        binding.vpPashuBanner.addOnPageChangeListener(listener);
        binding.vpPashuBanner.setOnTouchListener(new View.OnTouchListener() {
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
            binding.vpPashuBanner.setCurrentItem(currentPage, false);

        } else if (currentPage == 0) {
            currentPage = l.size() - 2;
            binding.vpPashuBanner.setCurrentItem(currentPage, false);
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

                binding.vpPashuBanner.setCurrentItem(currentPage++, true);

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
