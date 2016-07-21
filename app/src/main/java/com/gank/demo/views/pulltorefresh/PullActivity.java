package com.gank.demo.views.pulltorefresh;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gank.demo.R;

import java.util.ArrayList;
import java.util.List;


public class PullActivity extends Activity {

    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        initListView();
    }

    private void initListView() {
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add(String.valueOf(i));
        }
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new SampleAdapter(getBaseContext(), R.layout.list_item, mList));
    }

    class SampleAdapter extends ArrayAdapter<String> {

        private final LayoutInflater mInflater;
        private final List<String> mData;

        public SampleAdapter(Context context, int layoutResourceId, List<String> data) {
            super(context, layoutResourceId, data);
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, parent, false);
                viewHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvNum.setText(mData.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tvNum;
        }

    }
}
