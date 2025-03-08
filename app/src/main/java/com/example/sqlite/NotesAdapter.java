package com.example.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<NotesModel> notesModelList;

    public NotesAdapter(Context context, int layout, List<NotesModel> notesModelList) {
        this.context = context;
        this.layout = layout;
        this.notesModelList = notesModelList;
    }

    @Override
    public int getCount() {
        if (notesModelList != null)
        {
            return notesModelList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView textView;
        ImageView imageViewEdit;
        ImageView imageViewDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_NoteName);
            viewHolder.imageViewEdit = (ImageView) convertView.findViewById(R.id.img_edit);
            viewHolder.imageViewDelete = (ImageView) convertView.findViewById(R.id.img_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NotesModel notesModel = notesModelList.get(position);
        viewHolder.textView.setText(notesModel.getNameNote());

        final NotesModel notes = notesModelList.get(position);
        viewHolder.textView.setText(notes.getNameNote());
        viewHolder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cập nhật " + notes.getNameNote(),Toast.LENGTH_SHORT).show();
                if (context instanceof MainActivity) {
                    ((MainActivity) context).DiaLogCapNhatNotes(notes.getNameNote(), notes.getIdNote());
                }
            }

        });
        viewHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).DiaLogXoaNotes(notes.getNameNote(), notes.getIdNote());
                }
            }
        });

        return convertView;
    }
}
