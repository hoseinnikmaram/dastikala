package com.dastsaz.dastsaz.network;

import com.dastsaz.dastsaz.adapter.DataAdapterMyPoster;
import com.dastsaz.dastsaz.adapter.DataAdapterPoster;
import com.dastsaz.dastsaz.adapter.OperationResultModel;
import com.dastsaz.dastsaz.models.AuthenticationResponseModel;
import com.dastsaz.dastsaz.models.CityModel;
import com.dastsaz.dastsaz.models.DasteModel;
import com.dastsaz.dastsaz.models.DatePosterModel;
import com.dastsaz.dastsaz.models.NowDateModel;
import com.dastsaz.dastsaz.models.PictureModel;
import com.dastsaz.dastsaz.models.SignInRequestModel;
import com.dastsaz.dastsaz.models.SignUpRequestModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.models.SmsModel;
import com.dastsaz.dastsaz.models.Subdaste;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.UserModel;
import com.dastsaz.dastsaz.models.ViewModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * the interface implements REST API routes
 */
public interface FakeDataService {

  //  @POST("posterbyinsert")
   // Call<ErrorModel> createNewPoster(@Body PosterModel posterModel);


    @Multipart
    @POST("posterbyinsert")
    Call<PosterModel> createNewPosterbyupload(@Header("Authorization") String authHeader,@PartMap Map<String,RequestBody> mappic, @PartMap Map<String,RequestBody> mapbody);



    @GET("poster/index/{idcity}")
    Call<DatePosterModel> getPosters(@Path("idcity") String idcity);

    @POST("poster/index")
    Call<DatePosterModel> getPoster(@Body DatePosterModel datePosterModel);

    @GET("posterbyid/{id}")
    Call<PosterModel> getPosterbyid(@Path("id") String posterid);

    @GET("posterbytitle/{title}")
    Call<List<PosterModel>> getPosterBytitle(@Path("title") String postertitle);


    @GET("city/index")
    Call<List<CityModel>> getCitys();

    @GET("daste/index")
    Call<List<DasteModel>> getDastes();

    @GET("subdaste/index")
    Call<List<Subdaste>> getSubdaste();



    @GET("picturebyid/{id}")
    Call<List<PictureModel>> getPicturebyid(@Path("id") String posterid);


    @GET("posterbygroupid/{idgroup}/{idcity}")
    Call<List<PosterModel>> getPosterbyGroupid(@Path("idgroup") String idgroup,@Path("idcity") String idcity);


    @GET("posterbygroup-subgroupid/{idgroup}/{idsubgroup}/{idcity}")
    Call<List<PosterModel>> getPosterbyGroupSubgroupid(@Path("idgroup") String idgroup,@Path("idsubgroup") String idsubgroup,@Path("idcity") String idcity);


    @GET("current")
    Call<NowDateModel> getCurrentDate();

    @GET("posterbyuser/{id}")
    Call<List<PosterModel>> getPosterbyUserid(@Path("id") String userid);

    @POST("signup")
    Call<AuthenticationResponseModel> signUp(@Body SignUpRequestModel signUpRequestModel);

    @GET("deleteposterbyuser/{id}")
    Call<ErrorModel> deleteposterbyuser(@Path("id") String posterid);

    @POST("editposterbyuser/{id}")
    Call<ErrorModel> editposterbyuser(@Path("id") String posterid,@Body PosterModel posterModel);

    @POST("signin")
    Call<AuthenticationResponseModel> signIn(@Body SignInRequestModel signInRequestModel);

    @POST("call")
    Call<SmsModel> smsverfication(@Header("Authorization") String authHeader,@Body SmsModel smsModel);

    @POST("user/app")
    Call<OperationResultModel> terminateApp(@Header("Authorization") String authHeader);

    @POST("showuser")
    Call<UserModel> showuser(@Header("Authorization") String authHeader);
///////////view
    @POST("getview")
    Call<List<ViewModel>> getviews(@Body ViewModel viewModel);
    @POST("insertview")
    Call<ErrorModel> insertview(@Body ViewModel viewModel);

}
