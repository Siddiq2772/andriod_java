package com.example.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo.Adapter.NoteAdapter;
import com.example.todo.Adapter.ToDoAdapter;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import com.example.todo.R;

public class TouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerView.Adapter adapter;

    public TouchHelper(RecyclerView.Adapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are You Sure?")
                    .setTitle("Delete ")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (adapter instanceof ToDoAdapter) {
                                ((ToDoAdapter) adapter).deleteTask(position);
                            } else if (adapter instanceof NoteAdapter) {
                                ((NoteAdapter) adapter).deleteTask(position);
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(position);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            if (adapter instanceof ToDoAdapter) {
                ((ToDoAdapter) adapter).editTask(position);
            } else if (adapter instanceof NoteAdapter) {
                ((NoteAdapter) adapter).editTask(position);
            }
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit_24)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_blue))
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private Context getContext() {
        if (adapter instanceof ToDoAdapter) {
            return ((ToDoAdapter) adapter).getContext();
        } else if (adapter instanceof NoteAdapter) {
            return ((NoteAdapter) adapter).getContext();
        }
        return null;
    }
}
