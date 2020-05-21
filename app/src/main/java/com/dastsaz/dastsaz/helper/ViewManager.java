package com.dastsaz.dastsaz.helper;

import android.content.Context;
import android.view.View;


public class ViewManager {



    private View error;
    private View loading;
    private View noLoading;
    private View empty;

    public View getEmpty() {
        return empty;
    }

    public void setEmpty(View empty) {
        this.empty = empty;
    }

    private Context context;

    private CustomView mCustomView;

    public ViewManager(Context context){
        this.context = context;
    }

    public void setViewError(View view){
        this.error = view;
    }




    public void setViewLoading(View view){
        this.loading = view;
    }

    public void setViewNoLoading(View view){
        this.noLoading = view;
    }

    public void error(){

        if(checkAll()){
            this.empty.setVisibility(View.GONE);
            this.noLoading.setVisibility(View.GONE);
            this.loading.setVisibility(View.GONE);
            this.error.setVisibility(View.VISIBLE);
        }

    }

    public void showResult(){
        if(checkAll()){
            this.noLoading.setVisibility(View.VISIBLE);
            this.loading.setVisibility(View.GONE);
            this.error.setVisibility(View.GONE);
            this.empty.setVisibility(View.GONE);
        }
    }

    public void loading(){
        if(checkAll()){
            this.noLoading.setVisibility(View.GONE);
            this.loading.setVisibility(View.VISIBLE);
            this.error.setVisibility(View.GONE);
            this.empty.setVisibility(View.GONE);
        }
    }


    public void empty(){
        if(checkAll()){
            this.noLoading.setVisibility(View.GONE);
            this.loading.setVisibility(View.GONE);
            this.error.setVisibility(View.GONE);
            this.empty.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkAll(){

        if(error==null){

            new ManagerException("View error is null");
            return false;
        }else if(loading==null){


            new ManagerException("View loading is null");
            return false;
        }else if(noLoading==null){


            new ManagerException("View noloading is null");

            return false;
        }else if(empty==null){

            new ManagerException("View empty is null");
            return false;
        }


        return true;
    }


    public View getError() {
        return error;
    }

    public void setError(View error) {
        this.error = error;
    }

    public View getLoading() {
        return loading;
    }

    public void setLoading(View loading) {
        this.loading = loading;
    }

    public View getNoLoading() {
        return noLoading;
    }

    public void setNoLoading(View noLoading) {
        this.noLoading = noLoading;
    }



    public void setCustumizeView(CustomView cv){
        mCustomView = cv;
        if(checkAll()){
            if(mCustomView!=null){
                mCustomView.customErrorView(this.error);
                mCustomView.customLoadingView(this.loading);
                mCustomView.customEmptyView(this.empty);
            }
        }

    }




    class ManagerException extends Exception {
        public ManagerException(String msg){
            super(msg);
        }
    }



    public interface CustomView{
        public void customErrorView(View v);
        public void customLoadingView(View v);
        public void customEmptyView(View v);
    }

}
