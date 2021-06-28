package com.kaiqueferreira.dogsappmvvm.viewmodel;

import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kaiqueferreira.dogsappmvvm.model.DogBreed;
import com.kaiqueferreira.dogsappmvvm.model.DogDao;
import com.kaiqueferreira.dogsappmvvm.model.DogDatabase;
import com.kaiqueferreira.dogsappmvvm.model.DogsApiService;
import com.kaiqueferreira.dogsappmvvm.util.NotificationsHelper;
import com.kaiqueferreira.dogsappmvvm.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    private DogsApiService dogsService = new DogsApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    //Prevent the app from destroying a task
    //Insert task
    private AsyncTask<List<DogBreed>, Void, List<DogBreed>> insertTask;
    //Recovery task
    private AsyncTask<Void,Void,List<DogBreed>> retrieveTask;

    //Cache data in application used shared preferences
    private SharedPreferencesHelper prefHelper = SharedPreferencesHelper.getInstance(getApplication());
    //System clock works with milliseconds
    //You choose the time to update, in this case it's 5 minutes
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L;

    public ListViewModel(@NonNull Application application) {
        super(application);
    }


    public void refresh() {
        checkCacheDuration();
        long updateTime = prefHelper.getUpdateTime();
        long currentTime = System.nanoTime();

        //If not first time to run, get data to local database, in this case, Room Database
        if (updateTime != 0 && currentTime - updateTime < refreshTime){
            fetchFromDatabase();
        }
        else {
            //If the first time to run, get data to Api database, in this case
            fetchFromRemote();
        }
    }

    public void refreshBypassCache() {
        fetchFromRemote();
    }

    private void checkCacheDuration() {
        String cachePreference = prefHelper.getCacheDuration();
        if (!cachePreference.equals("")) {
            try{
                int cachePreferenceInt = Integer.parseInt(cachePreference);
                refreshTime = cachePreferenceInt * 1000 * 1000 * 1000L;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchFromDatabase(){
        loading.setValue(true);
        retrieveTask = new RetriveDogsTask();
        retrieveTask.execute();
    }

    private void fetchFromRemote() {
        loading.setValue(true);
        disposable.add(
                dogsService.getDogs()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull List<DogBreed> dogBreeds) {
                                insertTask = new InsertDogsTask();
                                insertTask.execute(dogBreeds);
                                Toast.makeText(getApplication(), "Dogs retrivied from endpoint", Toast.LENGTH_SHORT).show();

                                //Create notification
                                NotificationsHelper.getInstance(getApplication()).createNotification();
                                //dogs.setValue(dogBreeds);
                                //dogLoadError.setValue(false);
                                //loading.setValue(false);

                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                dogLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    //Main thread
    private void dogsRetrieved(List<DogBreed> dogList) {
        dogs.setValue(dogList);
        dogLoadError.setValue(false);
        loading.setValue(false);

        if (insertTask != null ){
            insertTask.cancel(true);
            insertTask = null;
        }

        if (insertTask != null ){
            insertTask.cancel(true);
            insertTask = null;
        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        //Very important when working with task threads
        //Prevent the app from destroying a task
        //If the application is destroyed, then if any task is running.
        //We will cancel it and set it null, and that is how we get rid of memory crashes.
        if (insertTask != null) {
            insertTask.cancel(true);
            insertTask = null;
        }


        //Recovery task
        if (retrieveTask != null) {
            retrieveTask.cancel(true);
            retrieveTask = null;
        }


    }

    //Background thread
    private class InsertDogsTask extends AsyncTask<List<DogBreed>, Void, List<DogBreed>> {

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list = lists[0];
            DogDao dao = DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();

            ArrayList<DogBreed> newList = new ArrayList<>(list);
            List<Long> result = dao.insertAll(newList.toArray(new DogBreed[0]));
            int i = 0;
            while(i < list.size()) {
                list.get(i).uuid = result.get(i).intValue();
                ++i;
            }
            return list;
        }

        //Execute in foreground thread
        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
        }
    }

    private class RetriveDogsTask extends AsyncTask<Void, Void, List<DogBreed>> {

        //Background thread
        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        //Foreground thread

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(), "Dogs retrivied from database", Toast.LENGTH_SHORT).show();
            prefHelper.saveUpdateTime(System.nanoTime());
        }
    }


    /*public void refresh(){
        DogBreed dog1 = new DogBreed("1","Corgi","15 years","","","","");
        DogBreed dog2 = new DogBreed("2","Rotwailler","10 years","","","","");
        DogBreed dog3 = new DogBreed("3","Labrador","13 years","","","","");
        DogBreed dog4 = new DogBreed("4","German Shepherd","15 years","","","","");
        DogBreed dog5 = new DogBreed("5","Colly","10 years","","","","");
        DogBreed dog6 = new DogBreed("6","Golden Retrievers","13 years","","","","");
        DogBreed dog7 = new DogBreed("7","French Bulldogs","15 years","","","","");
        DogBreed dog8 = new DogBreed("8","Bulldogs","10 years","","","","");
        DogBreed dog9 = new DogBreed("9","German Shorthaired Pointers","13 years","","","","");

        ArrayList<DogBreed> dogsList = new ArrayList<>();
        dogsList.add(dog1);
        dogsList.add(dog2);
        dogsList.add(dog3);
        dogsList.add(dog4);
        dogsList.add(dog5);
        dogsList.add(dog6);
        dogsList.add(dog7);
        dogsList.add(dog8);
        dogsList.add(dog9);

        dogs.setValue(dogsList);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }*/
}
