package feup.cm.traintickets.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import feup.cm.traintickets.R;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TrainTripModel;

public class TrainListAdapter extends ArrayAdapter<TrainTripModel> {

    private List<TrainTripModel> dataSet;
    private Context mContext;

    // Layout view lookup
    public static class ViewHolder {
        TextView txtTitle;
        TextView txtId;
        TextView txtDeparture;
        TextView txtArrival;
        TextView txtDuration;
    }

    public TrainListAdapter(ArrayList<TrainTripModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return dataSet.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        TrainTripModel tripModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.content_train_list, parent, false);

            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.train_ticket_title_txt);
            viewHolder.txtId = (TextView) convertView.findViewById(R.id.train_ticket_id_txt);
            viewHolder.txtDeparture = (TextView) convertView.findViewById(R.id.train_list_departure_txt);
            viewHolder.txtArrival = (TextView) convertView.findViewById(R.id.train_list_arrival_txt);
            viewHolder.txtDuration = (TextView) convertView.findViewById(R.id.train_list_duration_txt);

            result=convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ?
                R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if (tripModel != null) {
            viewHolder.txtTitle.setText(tripModel.getDescription());
            viewHolder.txtId.setText(String.valueOf(tripModel.getId()));
            viewHolder.txtDeparture.setText(String.format("%s  Hrs", tripModel.getDepartureTime().toString()));
            viewHolder.txtArrival.setText(String.format("%s  Hrs", tripModel.getArrivalTime().toString()));
            java.sql.Time arr=tripModel.getArrivalTime();
            java.sql.Time dep=tripModel.getDepartureTime();
            long diffHrs= (int)Math.abs(arr.getTime() - dep.getTime())/3600000;
            long  diffMin=Math.abs(diffHrs*60-(Math.abs(arr.getTime()-dep.getTime()))/60000);
            viewHolder.txtDuration.setText(String.valueOf(diffHrs)+":"+String.valueOf(diffMin)+" Hrs");
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
