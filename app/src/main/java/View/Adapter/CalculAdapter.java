package View.Adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
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

    private SparseBooleanArray selected = new SparseBooleanArray();//아이템 클릭 상태를 저장할 array 객체
    private int prePosition = -1;
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
        /*
        if (position == 0){
            holder.textView.setText("총 매수");
        }
        else if (position == 1){
            holder.textView.setText("총 매도");
        }
        else{
            holder.textView.setText("총 손익");
        }
         */
        if(position == 0) holder.textView.setBackgroundColor(Color.parseColor("#343434"));
        else if (position == 1) holder.textView.setBackgroundColor(Color.parseColor("#5F5E5D"));
        else holder.textView.setBackgroundColor(Color.parseColor("#808080"));

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

        private void changeVisibility(final boolean isExpanded){
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue + d);

            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();


                }
            });
        }

    }

}
