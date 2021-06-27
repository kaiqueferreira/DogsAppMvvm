package com.kaiqueferreira.dogsappmvvm.view;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.kaiqueferreira.dogsappmvvm.R;
import com.kaiqueferreira.dogsappmvvm.databinding.ItemDogBinding;
import com.kaiqueferreira.dogsappmvvm.model.DogBreed;
import com.kaiqueferreira.dogsappmvvm.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder>  implements DogClickListener{

    private ArrayList<DogBreed> dogsList;

    public DogsListAdapter(ArrayList<DogBreed> dogsList) {
        this.dogsList = dogsList;
    }

    public void updateDogsList(List<DogBreed> newDogsList) {
        dogsList.clear();
        dogsList.addAll(newDogsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Tradicionaly view holder
        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent, false);
        return new DogViewHolder(view);*/

        //Databinding
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogBinding view = DataBindingUtil.inflate(inflater, R.layout.item_dog, parent, false);
        return new DogViewHolder(view);

    }

    //Use databinding, fast compile time execute
    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        //Varabile dog setDog stay in xml item_dog
        // <data>
        //   <variable
        //     name="dog"
        //     type="com.kaiqueferreira.dogsappmvvm.model.DogBreed" />
        // </data>
        holder.itemView.setDog(dogsList.get(position));
    }

    @Override
    public void onDogClicked(View v) {
        String uuidString = ((TextView)v.findViewById(R.id.dogId)).getText().toString();
        int uuid = Integer.valueOf(uuidString);
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        action.setDogUuid(uuid);
        Navigation.findNavController(v).navigate(action);
    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        public ItemDogBinding itemView;

        public DogViewHolder(@NonNull ItemDogBinding itemView) {
            super(itemView.getRoot()); //super needs view
            this.itemView = itemView;
        }
    }


    /*@Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {

        ImageView image = holder.itemView.findViewById(R.id.imageView);
        TextView name = holder.itemView.findViewById(R.id.name);
        TextView lifespan = holder.itemView.findViewById(R.id.lifespan);
        LinearLayout layout = holder.itemView.findViewById(R.id.dogLayout);

        name.setText(dogsList.get(position).dogBreed);
        lifespan.setText(dogsList.get(position).lifeSpan);
        Util.loadImage(image, dogsList.get(position).imageUrl, Util.getProgressDrawable(image.getContext()));
        layout.setOnClickListener(view -> {
            ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
            action.setDogUuid(dogsList.get(position).uuid);
            Navigation.findNavController(layout).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder{

        public View itemView;

        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }*/
}
