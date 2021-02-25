package View.Service;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperListener listener;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener) { this.listener = listener; }

    // drag 위치를 ItemTouchHelper에서 받아 세팅 후, 현재 위치값을 int로 반환
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder){
        // 상하 드래그
        int drag_flags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 좌우 드래그
        // int swipe_flags = ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(drag_flags,0);
    }

    // 해당 아이템의 움직임을 감지
    @Override public boolean onMove(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }

    // 롱클릭감지 메소드
    @Override public boolean isLongPressDragEnabled() { return true; }
}
