/**
 * Copyright (C) futuretek AG 2016
 * All Rights Reserved
 *
 * @author Artan Veliju
 */
package survey.android.futuretek.ch.ft_survey;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SkillsActivity extends BaseActivity {
    private Button btn_add;
    private ListView listview;
    public List<String> _productlist = new ArrayList<String>();
    private ListAdapter adapter;
    private Button addSkillBtn;
    private String newSkill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        listview = (ListView) findViewById(R.id.listView);
        View mainTextView = findViewById(R.id.textLayout);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        addSkillBtn = (Button) findViewById(R.id.addSkillBtn);
        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openInputDialog(new View.OnClickListener() {
                    public void onClick(View v) {
                        EditText userInput = ((EditText) v.findViewById(R.id.userInput));
                        newSkill = null;
                        try {
                            newSkill = userInput.getText().toString();
                        } catch(Exception exc) {
                            exc.printStackTrace();
                        }
                        insertSkill(newSkill);
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ViewGroup)findViewById(R.id.textLayout)).removeAllViews();
        List<String> textArray = new ArrayList<>(1);
        textArray.add("Please add a developer skill");
        animateText(textArray);
        _productlist.clear();
        _productlist = getDatabase().getAllSkills();
        adapter = new ListAdapter(this);
        listview.setAdapter(adapter);
    }

    private class ListAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ViewHolder viewHolder;

        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return _productlist.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_row, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView1);
                viewHolder.delBtn = (Button) convertView.findViewById(R.id.deleteBtn);
                viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewGroup row = ((ViewGroup)v.getParent());
                        String id = ((TextView)row.findViewById(R.id.textView1)).getText().toString();
                        getDatabase().deleteSkill(id);
                        _productlist.remove(id);
                        adapter.notifyDataSetChanged();
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(_productlist.get(position));
            return convertView;
        }
    }

    private class ViewHolder {
        TextView textView;
        Button delBtn;

    }

    private void insertSkill(String skill){
        try {
            getDatabase().putSkill(skill);
            _productlist = getDatabase().getAllSkills();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}