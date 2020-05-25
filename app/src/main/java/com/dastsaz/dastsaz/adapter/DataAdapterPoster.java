package com.dastsaz.dastsaz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.ui.OnLoadMoreListener;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.myClass;
import com.dastsaz.dastsaz.utility.postertime;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *  Adapter show tweet in Recycler view
 */
public class DataAdapterPoster extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int last,page=0;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<PosterModel> mData = Collections.emptyList();
    private DataEventHandler mDataEventHandler;
    private AppPreferenceTools mAppPreferenceTools;
    private int lastPosition = -1;
    private RequestOptions options;
    private ArrayList<String> SendDate = new ArrayList<String>();
    public DataAdapterPoster(RecyclerView recyclerView,Context context, DataEventHandler dataEventHandler) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDataEventHandler = dataEventHandler;
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();


                    if(!isLoading &&totalItemCount >=5){
                        final int lastItem=pastVisiblesItems+visibleItemCount;
                        if(lastItem == totalItemCount){
                            if(last!=lastItem){
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                isLoading = true;
                                last=lastItem;
                            //    page = page +1;

                            }
                        }
                    }

                }
            }
        });





        //  new AppPreferenceTools(this.mContext);
        options = new RequestOptions()
                .placeholder(R.drawable.ic_menu_camera)
                .centerCrop()
                .error(R.drawable.ic_menu_camera)
                //  .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

    }


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void updateAdapterData(List<PosterModel> data) {
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
    public void clearDataPoster(){
       mData.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = mLayoutInflater.inflate(R.layout.data_row, parent, false);
            return new DataViewHolder(view);
        }

        else if (viewType == VIEW_TYPE_LOADING) {
            View view =  mLayoutInflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder) {
            DataViewHolder DataViewHolder = (DataViewHolder) holder;

            PosterModel currentModel = mData.get(position);
            DataViewHolder.mTxPosterBody.setText(currentModel.title);
            DataViewHolder.mTxlocation.setText(currentModel.location_name);
            //  Picasso.with(this.mContext).load(currentModel.src_pic).transform(new CropCircleTransformation()).into(holder.mImageViewPoster);

            Glide.with(mContext).load(currentModel.src_pic )
                    .transition(withCrossFade())
                    .apply(options)
                    .thumbnail(0.5f)
                    .into(DataViewHolder.mImageViewPoster);

            String t = currentModel.date;
            String ti = (String) t.subSequence(11, 19);
            String[] x = ti.split(":");

            String d = (String) t.subSequence(0, 10);
            String[] m = d.split("-");


            String sa = postertime.timer(m[0], m[1], m[2], x[0], x[1], x[2]);
            sa = convert_number(sa);
            DataViewHolder.mTxdate.setText(sa);
            SendDate.add(position, sa);
            // holder.mTxdate.setText(currentModel.date);

            myClass.textview_face(mContext, "IRANSans", DataViewHolder.mTxlocation, DataViewHolder.mTxdate, DataViewHolder.mTxPosterBody);
            setAnimation(holder.itemView, position);
        }
        else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }



    protected void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    public String convert_number(String number)
    {

        return number.replace("1","١").replace("2","۲").replace("3","۳")
                .replace("6","۶").replace("7","۷").replace("8","۸")
                .replace("9","۹").replace("4","۴").replace("5","۵");

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setLoaded() {
        isLoading = false;
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
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


        public DataViewHolder(View itemView) {
            super(itemView);
            mTxPosterBody=(AppCompatTextView)itemView.findViewById(R.id.tx_body);
            mTxlocation=(AppCompatTextView)itemView.findViewById(R.id.tx_location);
            mTxdate=(AppCompatTextView)itemView.findViewById(R.id.tx_time);
            mImageViewPoster=(AppCompatImageView)itemView.findViewById(R.id.imageViewPoster);
            mLyAction =(LinearLayout) itemView.findViewById(R.id.ly_main);
            mLyAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataEventHandler != null) {
                        mDataEventHandler.onDetailData(mData.get(getAdapterPosition()).id_poster, SendDate.get(getAdapterPosition()).toString(),getAdapterPosition());
                    }
                }
            });


        }
    }


    /**
     * define interface to handle events
     */
    public interface DataEventHandler {

        void onDetailData(String PosterId, String date , int position);
    }
}
