package com.kaiqueferreira.dogsappmvvm.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaiqueferreira.dogsappmvvm.DetailFragmentArgs;
import com.kaiqueferreira.dogsappmvvm.R;
import com.kaiqueferreira.dogsappmvvm.databinding.FragmentDetailBinding;
import com.kaiqueferreira.dogsappmvvm.model.DogBreed;
import com.kaiqueferreira.dogsappmvvm.model.DogPalette;
import com.kaiqueferreira.dogsappmvvm.util.Util;
import com.kaiqueferreira.dogsappmvvm.viewmodel.DetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    private int dogUuid;
    private DetailViewModel viewModel;
    private FragmentDetailBinding binding;

    private Boolean sendSmsStarted = false;


    /* Traditionally method
    @BindView(R.id.dogImage)
    ImageView dogImage;

    @BindView(R.id.dogName)
    TextView dogName;

    @BindView(R.id.dogPurpose)
    TextView dogPurpose;

    @BindView(R.id.dogTemperament)
    TextView dogTemperament;

    @BindView(R.id.dogLifespan)
    TextView dogLifespan;*/

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_detail, container, false);
        //ButterKnife.bind(this,view);
        //return view;
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        this.binding = binding;
        //Set menu
        setHasOptionsMenu(true);
        return binding.getRoot();

        //Use databinding  like a viewholder findviewById
        //binding.dogName.setText("Some text");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
            //tv2.setText(String.valueOf(dogUuid));
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch(dogUuid);

        observerViewModel();

        //fab.setOnClickListener(view1 -> onGoToList());
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void observerViewModel() {
        viewModel.dogLiveData.observe(this, dogBreed -> {
            //Verify getContext() != null, sometimes application destroyed and don't have a context
            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                binding.setDog(dogBreed);

                //Verify imageUrl not null when use in Palette
                if (dogBreed.imageUrl != null) {
                    setupBackgroundColor(dogBreed.imageUrl);
                }

            }
        });
    }

    private void setupBackgroundColor(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() { //Not convert to lambda when have two functions
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    //Get Dominant color
                                    //int intColor = palette.getDominantSwatch().getRgb();
                                    int intColor = palette.getLightMutedSwatch().getRgb();
                                    DogPalette myPalette = new DogPalette(intColor);
                                    binding.setPalette(myPalette);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_sms: {
                Toast.makeText(getContext(), "Action send sms", Toast.LENGTH_SHORT).show();
                if (!sendSmsStarted) {
                    sendSmsStarted = true;
                    ((MainActivity) getActivity()).checkSmsPermission();
                }
                break;
            }
            case R.id.action_share: {
                Toast.makeText(getContext(), "Action share", Toast.LENGTH_SHORT).show();

            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPermissionResult(Boolean permissionGranted) {
        sendSmsStarted = false;
    }

    /*@SuppressLint("FragmentLiveDataObserve")
    private void observerViewModel() {
        viewModel.dogLiveData.observe(this, dogBreed -> {
            //Verify getContext() != null, sometimes application destroyed and don't have a context
            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                dogName.setText(dogBreed.dogBreed);
                dogPurpose.setText(dogBreed.bredFor);
                dogTemperament.setText(dogBreed.temperament);
                dogLifespan.setText(dogBreed.lifeSpan);

                if (dogBreed.imageUrl != null) {
                    Util.loadImage(dogImage, dogBreed.imageUrl, new CircularProgressDrawable(getContext()));
                }

            }
        });
    }*/

    /*private void onGoToList() {
        NavDirections action = DetailFragmentDirections.actionList();
        Navigation.findNavController(fab).navigate(action);
    }*/
}