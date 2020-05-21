package com.dastsaz.dastsaz.network;


import com.dastsaz.dastsaz.TApplication;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ClientConfigs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * this class make Retrofit API Service
 */
public class FakeDataProvider {

    private FakeDataService mTService;
    private Retrofit mRetrofitClient;
    private AppPreferenceTools mAppPreferenceTools;

    /**
     * config Retrofit in initialization
     */
    public FakeDataProvider() {
//        new AppPreferenceTools(TApplication.applicationContext);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //add http interceptor to add headers to each request
       // httpClient.addInterceptor(new Interceptor() {
         //   @Override
          //  public Response intercept(Chain chain) throws IOException {
              //  Request original = chain.request();
                //build request
             //   Request.Builder requestBuilder = original.newBuilder();
                //add header for all of the request
               // requestBuilder.addHeader("Accept", "application/json");
                //check is user logged in , if yes should add authorization header to every request
              //  if (mAppPreferenceTools.isAuthorized()) {
               //     requestBuilder.addHeader("Authorization",mAppPreferenceTools.getAccessToken());
                    // "bearer " +
             //   }
           //     requestBuilder.method(original.method(), original.body());
       //         Request request = requestBuilder.build();
         //       return chain.proceed(request);
     //       }
   //     });
        //create new gson object to define custom converter on Date type
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new UTCDateTypeAdapter())
                .create();

        mRetrofitClient = new Retrofit.Builder()
                .baseUrl(ClientConfigs.REST_API_BASE_URL) // set Base URL , should end with '/'
                .client(httpClient.build()) // add http client
                .addConverterFactory(GsonConverterFactory.create(gson))//add gson converter
                .build();
        mTService = mRetrofitClient.create(FakeDataService.class);
    }

    /**
     * can get Retrofit Service
     *
     * @return
     */
    public FakeDataService getTService() {
        return mTService;
    }

    /**
     * get Retrofit client
     * used in ErrorUtil class
     *
     * @return
     */
    public Retrofit getRetrofitClient() {
        return mRetrofitClient;
    }
}
