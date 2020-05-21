package com.dastsaz.dastsaz.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.AuthenticationResponseModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * for better management of preference in application
 * like authentication information
 */
public class AppPreferenceTools {
    SharedPreferences sh;
    private AppPreferenceTools mAppPreferenceTools;
    private SharedPreferences mPreference;
    private Context mContext;
    public static final String STRING_PREF_UNAVAILABLE = "string preference unavailable";

    public AppPreferenceTools(Context context) {
        this.mContext = context;
        this.mPreference = this.mContext.getSharedPreferences("app_preference", Context.MODE_PRIVATE);
        sh = this.mContext.getSharedPreferences("ap_preference", Context.MODE_PRIVATE);

    }

    /**
     * save the user authentication model to pref at sing up || sign in
     *
     * @param authModel
     */
    public void saveUserAuthenticationInfo(AuthenticationResponseModel authModel) {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.pref_access_token), authModel.token.access_token)
              //  .putLong(this.mContext.getString(R.string.pref_expire_in_sec), authModel.token.expire_in_sec)
               // .putLong(this.mContext.getString(R.string.pref_expire_at), authModel.token.expire_at.getTime())
            //    .putString(this.mContext.getString(R.string.pref_refresh_token), authModel.token.refresh_token)
               // .putString(this.mContext.getString(R.string.pref_app_id), authModel.token.app_id)
                .putString(this.mContext.getString(R.string.pref_user_id), authModel.user_profile.id)
                .putString(this.mContext.getString(R.string.pref_user_email), authModel.user_profile.email)
                .putString(this.mContext.getString(R.string.pref_user_name), authModel.user_profile.name)
                .putString(this.mContext.getString(R.string.pref_user_phone), authModel.user_profile.phone)
                .putString(this.mContext.getString(R.string.pref_user_active), String.valueOf(authModel.user_profile.active))

                .apply();
    }

    public void savemarket(){

    }

    public void saveidcity(String idcity,String txtcity) {

        sh.edit()
                .putString(this.mContext.getString(R.string.pref_id_city),idcity)
                .putString(this.mContext.getString(R.string.pref_txt_city),txtcity)

                .apply();
    }

    public void savesmsverification(String verification) {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.pref_verification),verification)

                .apply();
    }

    public void savedefultidcity(String id) {

        sh.edit()
                .putString(this.mContext.getString(R.string.pref_getdefult_id),id)
                .apply();
    }

    public String getsmsverification() {
        return mPreference.getString(this.mContext.getString(R.string.pref_verification),"");
    }

    /**
     * get access token
     *
     * @return
     */
    public String getAccessToken() {
        return mPreference.getString(this.mContext.getString(R.string.pref_access_token), STRING_PREF_UNAVAILABLE);
    }

    public String getphonenumber() {
        return mPreference.getString(this.mContext.getString(R.string.pref_user_phone), "");
    }

    public String getidcity() {
        return sh.getString(this.mContext.getString(R.string.pref_id_city),"0");
    }

    public String gettxtcity() {
        return sh.getString(this.mContext.getString(R.string.pref_txt_city),"همه ی شهرها");
    }

    /**
     * detect is user sign in
     *
     * @return
     */
    public boolean isAuthorized() {
        return !getAccessToken().equals(STRING_PREF_UNAVAILABLE);
    }


    /**
     * get user name
     *
     * @return
     */
    public String getUserName() {
        return mPreference.getString(this.mContext.getString(R.string.pref_user_name), "");
    }



    public String getUserId() {
        return mPreference.getString(this.mContext.getString(R.string.pref_user_id), "null");
    }

    public String getCityDefultId() {
        return sh.getString(this.mContext.getString(R.string.pref_getdefult_id), "0");
    }

    /**
     * remove all prefs in logout
     */
    public void removeAllPrefs() {
        mPreference.edit().clear().apply();
    }



    // This four methods are used for maintaining favorites.
    public void saveFavorites( List<PosterModel> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = mContext.getSharedPreferences("PREFS_NAME",
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString("FAVORITES", jsonFavorites);

        editor.commit();
    }

    public void addFavorite( PosterModel poster) {
        List<PosterModel> favorites = getFavorites();
        if (favorites == null)
            favorites = new ArrayList<PosterModel>();
        favorites.add(poster);
        saveFavorites(favorites);
    }

    public void removeFavorite( String IdInEditMode) {
        ArrayList<PosterModel> favorites = getFavorites();
        if (favorites != null) {
            for (int i = 0; i < favorites.size(); i++) {

                if (favorites.get(i).id_poster.toString().equals(IdInEditMode)) {
                    favorites.remove(i);
                    saveFavorites(favorites);
                    Toast.makeText(mContext, "نشان آگهی حذف شد" , Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public Boolean EXISTFavorite( String IdInEditMode) {
        ArrayList<PosterModel> favorites = getFavorites();
        if (!(favorites==null)) {
            for (int i = 0; i < favorites.size(); i++) {

                if (favorites.get(i).id_poster.toString().equals(IdInEditMode)) {

                    return true ;

                }


            }
        }
        return false;
    }

    public ArrayList<PosterModel> getFavorites() {
        SharedPreferences settings;
        List<PosterModel> favorites = null;

        settings = mContext.getSharedPreferences("PREFS_NAME",
                Context.MODE_PRIVATE);

        if (settings.contains("FAVORITES")) {
            String jsonFavorites = settings.getString("FAVORITES", null);
            Gson gson = new Gson();
            PosterModel[] favoriteItems = gson.fromJson(jsonFavorites,
                    PosterModel[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList <PosterModel>(favorites);
        } else
            return (ArrayList<PosterModel>) favorites;

        return (ArrayList<PosterModel>) favorites;
    }
}
