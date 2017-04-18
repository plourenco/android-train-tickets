package feup.cm.traintickets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StepModel;
import feup.cm.traintickets.models.TripModel;

public class TimetableAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private ArrayList<TripModel> trips;
    private ArrayList<ArrayList<StepModel>> steps;

    private static class ViewHolder {
        TextView txtStation;
        TextView txtTime;
        TextView txtWaiting;
        TextView txtDescription;
        TextView txtTrain;
        TextView txtDirection;
    }

    public TimetableAdapter(Context _context, ArrayList<TripModel> trips, ArrayList<ArrayList<StepModel>> steps) {
        this._context = _context;
        this.trips = trips;
        this.steps = steps;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public StepModel getChild(int groupPosition, int childPosition) {
        return steps.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final StepModel childStepModel = getChild(groupPosition, childPosition);

        final ViewHolder holder;
        View view = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.timetable_item, parent, false);

            holder = new ViewHolder();
            holder.txtStation = (TextView) convertView.findViewById(R.id.timetable_station);
            holder.txtTime = (TextView) convertView.findViewById(R.id.timetable_time);
            holder.txtWaiting = (TextView) convertView.findViewById(R.id.timetable_waiting);

            holder.txtStation.setText("Test Station");
            holder.txtWaiting.setText("Test Wait");
            holder.txtTime.setText("test time");
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return steps.get(groupPosition).size();
    }

    @Override
    public TripModel getGroup(int groupPosition) {
        return trips.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return trips.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TripModel tripModel = (TripModel) getGroup(groupPosition);
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.timetable_group, parent, false);

            holder = new ViewHolder();
            holder.txtDescription = (TextView) convertView.findViewById(R.id.timetable_description);
            holder.txtTrain = (TextView) convertView.findViewById(R.id.timetable_train);
            holder.txtDirection = (TextView) convertView.findViewById(R.id.timetable_direction);

            holder.txtDescription.setText("Test Description");
            holder.txtTrain.setText("Test Train");
            holder.txtDirection.setText("test Direction");

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


}
