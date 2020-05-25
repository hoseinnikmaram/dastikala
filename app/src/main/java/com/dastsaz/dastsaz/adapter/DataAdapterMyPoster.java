package com.dastsaz.dastsaz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.postertime;

import java.util.Collections;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 *  Adapter show tweet in Recycler view
 */
public class DataAdapterMyPoster extends RecyclerView.Adapter<DataAdapterMyPoster.DataViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<PosterModel> mData = Collections.emptyList();
    private DataEventHandler mDataEventHandler;
    private AppPreferenceTools mAppPreferenceTools;
    private RequestOptions options;
protected Boolean visibleimgedit;
    public DataAdapterMyPoster(Boolean imgedit,Context context, DataEventHandler dataEventHandler) {
        this.visibleimgedit=imgedit;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDataEventHandler = dataEventHandler;
        //  new AppPreferenceTools(this.mContext);
        options = new RequestOptions()
                .placeholder(R.drawable.ic_menu_camera)
                .centerCrop()
                .error(R.drawable.ic_menu_camera)
                //  .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

    }




    public void updateAdapterData(List<PosterModel> data) {
        this.mData = data;
    }


    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.data_row_myposter, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        PosterModel currentModel = mData.get(position);
        holder.mTxPosterBody.setText(currentModel.title);
        holder.mTxlocation.setText(currentModel.location_name);
        String u=currentModel.src_pic+"";

        if (u.contains(",")){

            String[] m = u.split(",");
            Glide.with(mContext).load(m[0])
                    .transition(withCrossFade())
                    .apply(options)
                    .thumbnail(0.5f)
                    .into(holder.mImageViewPoster);
        }
        else {
            Glide.with(mContext).load(currentModel.src_pic)
                    .transition(withCrossFade())
                    .apply(options)
                    .thumbnail(0.5f)
                    .into(holder.mImageViewPoster);
        }


        String t= currentModel.date;
        String ti= (String) t.subSequence(11,19);
        String[] x = ti.split(":");

        String d= (String) t.subSequence(0,10);
        String[] m = d.split("-");



        String sa= postertime.timer(m[0],m[1],m[2],x[0],x[1],x[2]);
        holder.mTxdate.setText(sa);
        // holder.mTxdate.setText(currentModel.date);
        if(visibleimgedit){
            holder.mImEdit.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }



    /**
     * view holder for tweet adapter we have one view as data_rowxml layout
     */
    public class DataViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLyAction;
        private AppCompatTextView mTxPosterBody;
        private AppCompatTextView mTxdate;
        private AppCompatTextView mTxlocation;
        private AppCompatImageView mImageViewPoster;
        private AppCompatImageButton mImEdit;
        private AppCompatImageButton mImDelete;


        public DataViewHolder(View itemView) {
            super(itemView);
            mImEdit = (AppCompatImageButton) itemView.findViewById(R.id.im_edit);
            mImDelete = (AppCompatImageButton) itemView.findViewById(R.id.im_delete);
            mTxPosterBody=(AppCompatTextView)itemView.findViewById(R.id.tx_body);
            mTxlocation=(AppCompatTextView)itemView.findViewById(R.id.tx_location);
            mTxdate=(AppCompatTextView)itemView.findViewById(R.id.tx_time);
            mImageViewPoster=(AppCompatImageView)itemView.findViewById(R.id.imageViewPoster);
            mLyAction =(LinearLayout) itemView.findViewById(R.id.ly_main);
            mLyAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onDetailData(mData.get(getAdapterPosition()).id_poster, getAdapterPosition());
                    }
                }
            });

            mImDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onDelete(mData.get(getAdapterPosition()).id_poster, getAdapterPosition());
                    }
                }
            });

            mImEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onEdit(mData.get(getAdapterPosition()).id_poster, getAdapterPosition(),mImEdit);
                    }
                }
            });


        }

    }



    /**
     * define interface to handle events
     */
    public interface DataEventHandler {

        void onDetailData(String PosterId, int position);

        void onEdit(String Id, int position, AppCompatImageButton mImEdit);

        void onDelete(String Id, int position);
    }
}
