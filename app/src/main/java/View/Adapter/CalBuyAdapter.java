package View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdst.stockoverlay.R;

public class CalBuyAdapter extends RecyclerView.Adapter<CalBuyAdapter.MainHolder> {


    public class MainHolder extends RecyclerView.ViewHolder{

        public MainHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_stockoutput)
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
