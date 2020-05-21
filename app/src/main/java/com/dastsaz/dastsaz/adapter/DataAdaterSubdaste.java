package com.dastsaz.dastsaz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.Subdaste;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;

import java.util.Collections;
import java.util.List;

/**
 * Created by m.hosein on 1/12/2018.
 */

public class DataAdaterSubdaste  extends RecyclerView.Adapter<DataAdaterSubdaste.DataViewHolder>  {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Subdaste> mData = Collections.emptyList();
    private DataEventHandler mDataEventHandler;
    private AppPreferenceTools mAppPreferenceTools;


    public DataAdaterSubdaste(Context context, DataEventHandler dataEventHandler) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDataEventHandler = dataEventHandler;
        //  new AppPreferenceTools(this.mContext);
    }




    public void updateAdapterData(List<Subdaste> data) {
        this.mData = data;
    }


    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.rows, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        Subdaste currentModel = mData.get(position);
        holder.mTxBody.setText(currentModel.subname);



    }


    @Override
    public int getItemCount() {
        return mData.size();
    }



    /**
     * view holder for tweet adapter we have one view as data_rowxml layout
     */
    public class DataViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView mTxBody;

        private LinearLayout mLyAction;



        public DataViewHolder(View itemView) {
            super(itemView);
            mTxBody=(AppCompatTextView)itemView.findViewById(R.id.tx_body_dialog);


            mLyAction =(LinearLayout) itemView.findViewById(R.id.ly_main_dialog);
            mLyAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onDetailData(String.valueOf(mData.get(getAdapterPosition()).id_sub), getAdapterPosition());
                    }
                }
            });


        }
    }


    /**
     * define interface to handle events
     */
    public interface DataEventHandler {

        void onDetailData(String CDasteId, int position);
    }
}


