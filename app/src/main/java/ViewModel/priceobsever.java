package ViewModel;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class priceobsever {

    private stockViewModel model;

    protected void OnCreate(Bundle savedInstanceState){
        /*super.onCreate(savedInstanceState);
        //Other code to setup the activity
        model = new ViewModelProviders.of(this).get(stockViewModel.class);//get the ViewModel this대신에 엑티비티 넣기

        //Create the obsever which update the UI.
        final Observer<Integer> stockObserver = new Observer<Integer>(){
            public void onChanged(@Nullable final Integer newprice){
                //Update the UI, in this case, a TextView
                nameTextView.setText(newprice);*/
    }
    /*
        final Observer<Integer> stockObserver = (price) -> {
            view의 price.set(price);


        };
        //Obsever the LiveData, passing in this activity as the LifeCycleOwner and the obsever.
        model.getLiveDataPrice().observe(this,stockObserver);
    }

*/
}
