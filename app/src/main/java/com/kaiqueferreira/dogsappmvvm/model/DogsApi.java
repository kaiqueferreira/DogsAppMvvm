package com.kaiqueferreira.dogsappmvvm.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    Single<List<DogBreed>>getDogs();

    /*
    //Dynamic url
    @GET()
    Single<List<Cats>>getCats(@Url String url);*/
}
