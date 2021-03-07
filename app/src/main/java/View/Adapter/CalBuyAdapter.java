/*package View.Adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdst.stockoverlay.R;

import java.util.ArrayList;

import Model.Calcul;

import View.CalculHolderMovie;

public class CalBuyAdapter extends RecyclerView.Adapter<CalBuyAdapter.MainHolder> {

    private ArrayList<Calcul> listCalcul = new ArrayList<>();

    private SparseBooleanArray clickfrag = new SparseBooleanArray();

    private int prePosition = -1;

    public class MainHolder extends RecyclerView.ViewHolder{

        public MainHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    //뷰 홀더를 생성하고 뷰를 붙여주는 역할
    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item4,parent,false);
        return new MainHolder(view);
    }
    //재활용 되는 뷰가 호출하여 실행되는 메소드, 뷰홀더에 있는 onBind 함수를 통해 리스트에있는 position번째 값의 데이터를 넘겨줌
    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, final int position) {

    }



    @Override
    public int getItemCount() {
        return 0;
    }


}
*/