package com.dastsaz.dastsaz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.CityModel;
import com.dastsaz.dastsaz.models.ViewModel;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;

import java.util.Collections;
import java.util.List;

/**
 * Created by m.hosein on 1/12/2018.
 */

public class DataAdaterViews extends RecyclerView.Adapter<DataAdaterViews.DataViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ViewModel> mData = Collections.emptyList();
    private DataEventHandler mDataEventHandler;
    private AppPreferenceTools mAppPreferenceTools;


    public DataAdaterViews(Context context, DataEventHandler dataEventHandler) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDataEventHandler = dataEventHandler;
        //  new AppPreferenceTools(this.mContext);
    }




    public void updateAdapterData(List<ViewModel> data) {

        if(mData.isEmpty())
        {
            mData=data;
            //    mData.add(null);
            //   mData.remove(mData.size()-1);
        }
        else {

            mData.addAll(mData.size(), data);
            //    mData.add(null);
            //   mData.remove(mData.size() - 1);
        }
    }


    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.view_row, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        ViewModel currentModel = mData.get(position);
        holder.mTxName.setText(currentModel.name);
        holder.msg.setText(currentModel.message);



    }


    @Override
    public int getItemCount() {
        return mData.size();
    }



    /**
     * view holder for tweet adapter we have one view as data_rowxml layout
     */
    public class DataViewHolder extends RecyclerView.ViewHolder {

        private CardView mLyAction;
        private TextView mTxName;
        private TextView msg;


        public DataViewHolder(View itemView) {
            super(itemView);
            mTxName=(TextView)itemView.findViewById(R.id.name);
            msg=(TextView)itemView.findViewById(R.id.msg);

            mLyAction =(CardView) itemView.findViewById(R.id.placeItem);
            mLyAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onDetailData(String.valueOf(mData.get(getAdapterPosition()).message_id), getAdapterPosition());
                    }
                }
            });


        }
    }


    /**
     * define interface to handle events
     */
    public interface DataEventHandler {

        void onDetailData(String MessageId, int position);
    }

}
