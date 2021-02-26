package View.Service;

public interface ItemTouchHelperListener {
    boolean onItemMove(int form_position,int to_position);
    void saveEditedList();
}
