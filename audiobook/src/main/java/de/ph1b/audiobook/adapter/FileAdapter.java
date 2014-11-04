package de.ph1b.audiobook.adapter;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.ph1b.audiobook.BuildConfig;
import de.ph1b.audiobook.R;
import de.ph1b.audiobook.fragment.FilesChooseFragment;

public class FileAdapter extends BaseAdapter {

    private final ArrayList<File> data;
    private final Fragment fragment;
    private final SparseBooleanArray checked;
    private static final String TAG = "FileAdapter";

    public FileAdapter(ArrayList<File> data, Fragment fragment) {
        this.data = data;
        this.fragment = fragment;
        this.checked = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.files_chooser_listview, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.file_check);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //setting checkbox tag to identify correct box
        viewHolder.checkBox.setTag(position);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = (Integer) buttonView.getTag();
                checked.put(pos, isChecked);
                ArrayList<File> returnList = new ArrayList<File>();
                for (int i = 0; i < getCount(); i++) {
                    if (checked.get(i))
                        returnList.add(data.get(i));
                }
                ((FilesChooseFragment) fragment).checkStateChanged(returnList);
            }
        });

        //setting correct value
        boolean fileIsChecked = checked.get(position);
        viewHolder.checkBox.setChecked(fileIsChecked);

        File f = data.get(position);
        String name = f.getName();

        if (f.isFile())
            viewHolder.icon.setImageResource(R.drawable.ic_album_grey600_48dp);
        else
            viewHolder.icon.setImageResource(R.drawable.ic_folder_grey600_48dp);

        viewHolder.nameView.setText(name);

        return convertView;
    }

    public void clearCheckBoxes() {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "clearCheckBoxes() was called!");
        checked.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView icon;
        TextView nameView;
        CheckBox checkBox;
    }
}