package feup.cm.traintickets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import feup.cm.traintickets.R;
import feup.cm.traintickets.models.StepModel;
import feup.cm.traintickets.models.TripModel;

public class TimetableAdapter extends BaseExpandableListAdapter {

    public String arrival="";
    public String arrivalTime="";

    /*private static class ViewHolder {
        TextView txtStation;
        TextView txtTime;
        TextView txtWaiting;
        TextView txtDescription;
        TextView txtTrain;
        TextView txtDirection;
    }*/

    private Context _context;
    private List<TripModel> trips; // header titles
    // child data in format of header title, child title
    private HashMap<TripModel, List<StepModel>> steps;

    public TimetableAdapter(Context context, List<TripModel> listDataHeader,
                                 HashMap<TripModel, List<StepModel>> listChildData) {
        this._context = context;
        this.trips = listDataHeader;
        this.steps = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.steps.get(this.trips.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final StepModel childStep = (StepModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.timetable_item, null);
        }

        if(childStep != null) {
            if(childPosition==0) {
                TextView station = (TextView) convertView.findViewById(R.id.timetable_station);
                station.setText(childStep.getDepartureStation().getStationName());
                TextView time = (TextView) convertView.findViewById(R.id.timetable_time);
                time.setText(childStep.getDepartureTime().toString());
                TextView waiting = (TextView) convertView.findViewById(R.id.timetable_waiting);
                waiting.setText("     -------");
            }
            else if( childPosition>0 &&childPosition<4){
                TextView station = (TextView) convertView.findViewById(R.id.timetable_station);
                station.setText(childStep.getDepartureStation().getStationName());
                TextView time = (TextView) convertView.findViewById(R.id.timetable_time);
                time.setText(childStep.getDepartureTime().toString());
                TextView waiting = (TextView) convertView.findViewById(R.id.timetable_waiting);
                waiting.setText(String.format("%s mn Stop", String.valueOf(childStep.getWaitingTime())));
                arrival=childStep.getArrivalStation().getStationName();
                arrivalTime=childStep.getArrivalTime().toString();
            }
            else{
                TextView station = (TextView) convertView.findViewById(R.id.timetable_station);
                station.setText(arrival);
                TextView time = (TextView) convertView.findViewById(R.id.timetable_time);
                time.setText(arrivalTime);
                TextView waiting = (TextView) convertView.findViewById(R.id.timetable_waiting);
                waiting.setText("     -------");
            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.steps.get(this.trips.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.trips.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.trips.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        TripModel trip = (TripModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.timetable_group, null);
        }

        TextView txtTrip = (TextView) convertView.findViewById(R.id.timetable_description);
        txtTrip.setText(trip.getDescription());
        TextView txtDirection=(TextView)convertView.findViewById(R.id.timetable_direction_txt);
        txtDirection.setText(trip.getDirection());
        TextView txtTrain=(TextView)convertView.findViewById(R.id.timetable_train);
        txtTrain.setText("Train Nr :"+trip.getId());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

