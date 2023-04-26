package com.example.fitfood.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.PlanEntity;
import com.example.fitfood.data.data_sources.room.entites.RecipeEntity;
import com.example.fitfood.ui.RecipeCardFragment;
import com.example.fitfood.ui.view_models.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanCardAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<List<RecipeEntity>> recipes;
    private String[] daysOfWeek = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};

    public PlanCardAdapter(Context context, List<List<RecipeEntity>> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getGroupCount() {
        return daysOfWeek.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return recipes.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.plan_choosing_expandable_parent_item, parent, false);


        TextView dayName = convertView.findViewById(R.id.day_name);
        dayName.setText(daysOfWeek[groupPosition]);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.plan_choosing_expandable_child_item, parent, false);

        RecipeEntity recipeEntity = recipes.get(groupPosition).get(childPosition);

        TextView time = convertView.findViewById(R.id.time);
        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);
        ImageView image = convertView.findViewById(R.id.image);

        switch (childPosition){
            case 0:
                time.setText("Завтрак");
                break;
            case 1:
                time.setText("Обед");
                break;
            case 2:
                time.setText("Ужин");
                break;
            case 3:
                time.setText("Перекус");
                break;

        }
        System.out.println(recipeEntity.Title);
        title.setText(recipeEntity.Title);
        description.setText(recipeEntity.Description);
        image.setImageResource(context.getResources().getIdentifier(recipeEntity.ImageName, "drawable", context.getPackageName()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
