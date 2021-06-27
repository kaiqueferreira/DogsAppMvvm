package com.kaiqueferreira.dogsappmvvm.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kaiqueferreira.dogsappmvvm.model.DogBreed;
import com.kaiqueferreira.dogsappmvvm.model.DogDatabase;

public class DetailViewModel  extends AndroidViewModel {//ViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();
    private RetrieveDogTask task;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }


    /*public void fetch(){
        DogBreed dog4 = new DogBreed("4","German Shepherd","15 years","","hunter","angry","");
        dogLiveData.setValue(dog4);
    }*/

    public void fetch(int uuid){
        task = new RetrieveDogTask();
        task.execute(uuid);
        if (task != null){
            task.cancel(true);
            task = null;
        }
    }

    private class RetrieveDogTask extends AsyncTask<Integer,Void, DogBreed> {

        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid = integers[0];
            return DogDatabase.getInstance(getApplication()).dogDao().getDog(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogLiveData.setValue(dogBreed);
        }
    }

}
