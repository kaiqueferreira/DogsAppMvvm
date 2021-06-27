package com.kaiqueferreira.dogsappmvvm.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kaiqueferreira.dogsappmvvm.model.DogBreed;

public class DetailViewModel  extends ViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();

    public void fetch(){
        DogBreed dog4 = new DogBreed("4","German Shepherd","15 years","","hunter","angry","");
        dogLiveData.setValue(dog4);
    }

}
