package com.kaiqueferreira.dogsappmvvm.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaiqueferreira.dogsappmvvm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {


    /*@BindView(R.id.floatingActionButton2)
    FloatingActionButton fab;

    @BindView(R.id.textView2)
    TextView tv2;*/

    private  int dogUuid;

    public DetailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
            //tv2.setText(String.valueOf(dogUuid));
        }
        //fab.setOnClickListener(view1 -> onGoToList());
    }

    /*private void onGoToList() {
        NavDirections action = DetailFragmentDirections.actionList();
        Navigation.findNavController(fab).navigate(action);
    }*/
}