package View.Adapter.Calculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

public class CalculAdapter extends RecyclerView.Adapter<CalculAdapter.ViewHolder>
{
    private ArrayList<String> listData = new ArrayList<>();



    private Context context;



    public CalculAdapter(ArrayList<String> list){
        this.listData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.custom_list_item4, parent, false);
        CalculAdapter.ViewHolder vh = new CalculAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(String str){
        listData.add(str);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        ViewHolder(View view){
            super(view);

            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                }
            });
            textView = view.findViewById(R.id.avg_Buy_Button);
        }


        void onBind(String str){
            textView.setText(str);
        }

    }

}
