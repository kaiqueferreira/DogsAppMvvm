package com.kaiqueferreira.dogsappmvvm.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaiqueferreira.dogsappmvvm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    /*@BindView(R.id.floatingActionButton)
    FloatingActionButton fab;*/

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*fab.setOnClickListener(view1 -> {
            onGoToDetails();
        });
    }*/

    /*void onGoToDetails() {
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        (action).setDogUuid(5);
        Navigation.findNavController(fab).navigate(action);

    }*/
}