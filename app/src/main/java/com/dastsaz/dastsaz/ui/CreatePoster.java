package com.dastsaz.dastsaz.ui;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.TApplication;
import com.dastsaz.dastsaz.models.CityModel;
import com.dastsaz.dastsaz.models.DasteModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.models.Subdaste;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.AndroidUtilities;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.Constants;
import com.dastsaz.dastsaz.utility.ErrorUtils;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePoster extends AppCompatActivity {
    private  int id_location=-2;
    private AppCompatEditText mETxtitle;
    private MaterialDialog builder;

    private static Map<String, RequestBody> mappic=new HashMap<>();
    private Map<String, RequestBody> mapbody=new HashMap<>();

    private AppCompatEditText mETxdescription;
   // private ArrayList<Image> menuImageList;
   private AppPreferenceTools mAppPreferenceTools;
    private Intent CropIntent;
    private ImageView img1,img2,img3,img4;
    private ArrayList<Object> picvalue = new ArrayList<Object>();

    private AppCompatEditText mETxphone;
    private AppCompatEditText mETxsms;
    private AppCompatEditText mETxprice;
    private PermissionEventListener mPermissionEventListener;
    int req_code = 0;

    private TextView txt_group;
    private TextView txt_subdaste;
    private TextView txt_selcity;
    private int mg1,mg2,mg3,mg4=0;
    private String IdInEditMode;
    private FakeDataService mTService;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    private static final int STORAGE_PERMISSION_CODE = 123;
    private ProgressDialog mProgressDialog;
    private ProgressDialog mProgressDialoginsert;

   private static int i = 0;

    private Bundle args;
    private TextView txt_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_create_poster);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //get argument and check is in edit mode
        mAppPreferenceTools = new AppPreferenceTools(this);

        if (mAppPreferenceTools.isAuthorized() && mAppPreferenceTools.getsmsverification().equals("ok")) {


            Toolbar toolbar = (Toolbar) findViewById(R.id.Poster_create_toolbar);
            setSupportActionBar(toolbar);

            mETxdescription = (AppCompatEditText) findViewById(R.id.etx_description);
            mETxtitle = (AppCompatEditText) findViewById(R.id.etx_title);
            mETxsms = (AppCompatEditText) findViewById(R.id.etx_sms);
            mETxprice = (AppCompatEditText) findViewById(R.id.etx_price);
            img1=(ImageView) findViewById(R.id.img1);
            img2=(ImageView) findViewById(R.id.img2);
            img3=(ImageView) findViewById(R.id.img3);
            img4=(ImageView) findViewById(R.id.img4);

            mETxphone = (AppCompatEditText) findViewById(R.id.etx_phone);

            txt_group = (TextView) findViewById(R.id.txt_group);
            txt_selcity = (TextView) findViewById(R.id.txt_selcity);
            txt_subdaste = (TextView) findViewById(R.id.txt_subdaste);

             txt_location = (TextView) findViewById(R.id.txt_location);

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);

            mProgressDialoginsert = new ProgressDialog(this);
            mProgressDialoginsert.setCancelable(false);
            mProgressDialoginsert.setCanceledOnTouchOutside(false);

            //first create new instant of FakeDataProvider
            FakeDataProvider provider = new FakeDataProvider();
            //get the FakeDataService interface to call API routes
            mTService = provider.getTService();
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    req_code = 1;
                    bottom_sheet();
                }
            });

            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    req_code = 2;
                    bottom_sheet();
                }
            });

            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    req_code = 3;
                    bottom_sheet();
                }
            });

            img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    req_code = 4;
                    bottom_sheet();
                }
            });


        }

        else {
            //the user is not logged in so should navigate to sing in activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }




    }

    void bottom_sheet(){
        BottomSheetMenuDialog dialog = new BottomSheetBuilder(CreatePoster.this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setMenu(R.menu.sheet_img_insert)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.sheet_pick:
                                if(req_code==1){
                                    Toast.makeText(getBaseContext(), "اولین عکس به عنوان عکس اصلی در نظر گرفته می شود", Toast.LENGTH_LONG).show();
                                }
                                PictureSelection();
                                break;

                            case R.id.sheet_show:
                                image_show();
                                break;

                            case  R.id.sheet_del:
                                image_del();
                                break;
                        }
                    }
                })
                .createDialog();

        dialog.show();
    }

    void image_del(){

        switch (req_code){
            case 1:

                if(mg1 == 1) {
                    mappic.remove(picvalue.get(0));
                    mg1=0;
                    img1.setImageResource(R.drawable.ic_defult);

                }
                    else
                    Toast.makeText(this,"تصویری جهت حذف وجود ندارد",Toast.LENGTH_LONG).show();

                break;

            case 2:
                if(mg2 == 1) {
                    mappic.remove(picvalue.get(1));
                    mg2=0;
                    img2.setImageResource(R.drawable.ic_defult);
                }
                else
                    Toast.makeText(this,"تصویری جهت حذف وجود ندارد",Toast.LENGTH_LONG).show();

                break;

            case 3:
                if(mg3 == 1) {
                    mappic.remove(picvalue.get(2));
                    mg3=0;
                    img3.setImageResource(R.drawable.ic_defult);

                }
                else
                    Toast.makeText(this,"تصویری جهت حذف وجود ندارد",Toast.LENGTH_LONG).show();

                break;

            case 4:
                if(mg4 == 1) {
                    mappic.remove(picvalue.get(3));
                    mg4=0;
                    img4.setImageResource(R.drawable.ic_defult);

                }
                else
                    Toast.makeText(this,"تصویری جهت حذف وجود ندارد",Toast.LENGTH_LONG).show();

                break;

        }
    }
    void image_show(){
        Drawable drawable =null;
        boolean show=false;
        switch (req_code){
            case 1:
                drawable=img1.getDrawable();
                if(mg1 == 1)
                    show=true;
                break;

            case 2:
                drawable =img2.getDrawable();
                if(mg2 == 1)
                    show =true;
                break;

            case 3:
                drawable =img3.getDrawable();
                if(mg3 == 1)
                    show=true;
                break;

            case 4:
                drawable =img4.getDrawable();
                if(mg4 == 1)
                    show=true;
                break;
        }

        if(show == false){
            Toast.makeText(this,"تصویری جهت نمایش وجود ندارد",Toast.LENGTH_LONG).show();
            return;
        }

        Dialog dialog=new Dialog(CreatePoster.this);
        dialog.setCancelable(true);
        dialog.setTitle("نمایش تصویر");
        dialog.onBackPressed();
        dialog.setContentView(R.layout.dialog_image_show);
        ImageView imageView =(ImageView)dialog.findViewById(R.id.dialog_img);
        imageView.setImageDrawable(drawable);

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {


            try {
                Uri selectedImageUri = data.getData();
                if(req_code == 1){
                    mg1=1;
                    img1.setImageURI(selectedImageUri);
                    img2.setVisibility(View.VISIBLE);
                    Toast.makeText(getBaseContext(), "اولین عکس به عنوان عکس اصلی در نظر گرفته می شود", Toast.LENGTH_LONG).show();

                }
                else if(req_code == 2){
                    mg2=1;
                    img2.setImageURI(selectedImageUri);

                    img3.setVisibility(View.VISIBLE);
                    img4.setVisibility(View.VISIBLE);

                }
                else if (req_code == 3){
                    mg3=1;
                    img3.setImageURI(selectedImageUri);
                }
                else if (req_code == 4){
                    mg4=1;
                    img4.setImageURI(selectedImageUri);
                }
                String extractUriFrom = selectedImageUri.toString();
                //check is from google photos or google drive
                if (extractUriFrom.contains("com.google.android.apps.photos.contentprovider") || extractUriFrom.contains("com.google.android.apps.docs.storage")) {
                    final int chunkSize = 1024;  // We'll read in one kB at a time
                    byte[] imageData = new byte[chunkSize];
                    File imageFile = AndroidUtilities.generateImagePath();
                    InputStream in = null;
                    OutputStream out = null;
                    mProgressDialog.setMessage("منتظر بمانید ...");
                    mProgressDialog.show();
                    try {
                        in = getContentResolver().openInputStream(selectedImageUri);
                        out = new FileOutputStream(imageFile);
                        int bytesRead;
                        while ((bytesRead = in.read(imageData)) > 0) {
                            out.write(Arrays.copyOfRange(imageData, 0, Math.max(0, bytesRead)));
                        }
                        uploadImageProfile(imageFile.getAbsolutePath());
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    } catch (Exception ex) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        Toast.makeText(getBaseContext(), "can not get this image :|", Toast.LENGTH_SHORT).show();
                        Log.e("Something went wrong.", ex.toString());
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                } else {
                    uploadImageProfile(AndroidUtilities.getPath(selectedImageUri));
                  //  File imageFile = AndroidUtilities.generateImagePath();
                  //  uploadImageProfile(imageFile.getAbsolutePath());

                }
            } catch (Exception ex) {
                Toast.makeText(getBaseContext(), "something wrong :|", Toast.LENGTH_SHORT).show();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadImageProfile(String imagePath) {


        File imageFile = new File(imagePath);



            if (imageFile.exists()) {
                //create request body


                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                picvalue.add(i,"photo" + i + "\"; filename=\"" + imageFile.getName() + "\"");
                mappic.put("photo" + i + "\"; filename=\"" + imageFile.getName() + "\"", requestBody);

            } else {
                Toast.makeText(getBaseContext(), "can not upload file since the file is not exist :|", Toast.LENGTH_SHORT).show();
            }
            i = i + 1;


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_send, menu);
        getSupportActionBar().setTitle("ایجاد آگهی");
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            if (id_location!=-1 && mETxtitle.getText().toString().trim().length() > 0 && mETxdescription.getText().toString().trim().length() > 0 ) {
                //create  Model
                mProgressDialoginsert.setMessage("منتظر بمانید...");
                mProgressDialoginsert.show();
                String ui=mAppPreferenceTools.getUserId();
                //assign  model values
                String ic = String.valueOf(CityModel.idcity);
                String ip = String.valueOf(DasteModel.idgroup);
                String is = String.valueOf(Subdaste.idsub);
                String lo =String.valueOf(id_location);

                String p = String.valueOf(mETxprice.getText() + "");
                String ph = String.valueOf(mETxphone.getText() + "");
                String sm= String.valueOf(mETxsms.getText() + "");
                String de =mETxdescription.getText().toString();
                String ti =mETxtitle.getText().toString();

                RequestBody iduser= RequestBody.create(MediaType.parse("text/plain"),ui );
                mapbody.put("id_user",iduser);

                RequestBody idcit= RequestBody.create(MediaType.parse("text/plain"),ic );
                mapbody.put("id_city",idcit);

                RequestBody idgroup = RequestBody.create(MediaType.parse("text/plain"),ip );
                mapbody.put("id_group",idgroup);


                RequestBody idsub = RequestBody.create(MediaType.parse("text/plain"), is);
                mapbody.put("id_sub",idsub);

                RequestBody Price = RequestBody.create(MediaType.parse("text/plain"),p );
                mapbody.put("price",Price);

                RequestBody Phone = RequestBody.create(MediaType.parse("text/plain"), ph);
                mapbody.put("phone",Phone);

                RequestBody Sms = RequestBody.create(MediaType.parse("text/plain"),sm );
                mapbody.put("sms",Sms);

                RequestBody Description = RequestBody.create(MediaType.parse("text/plain"),de );
                mapbody.put("description",Description);

                RequestBody Title = RequestBody.create(MediaType.parse("text/plain"),ti);
                mapbody.put("title",Title);

                RequestBody Location = RequestBody.create(MediaType.parse("text/plain"), lo);
                mapbody.put("id_location",Location);








                //create  generic class to send request to server
                Call<PosterModel> call = mTService.createNewPosterbyupload("Bearer " + mAppPreferenceTools.getAccessToken(),mappic,mapbody);
                //Async request
                //NOTE: you should always send Async request since the sync request cause crash in your application
                call.enqueue(new Callback<PosterModel>() {
                    @Override
                    public void onResponse(Call<PosterModel> call, Response<PosterModel> response) {
                        if (response.isSuccessful()) {
                            if (mProgressDialoginsert.isShowing()) {
                                mProgressDialoginsert.dismiss();
                            }

                            Toast.makeText(getBaseContext(), "موفق در ایجاد آگهی", Toast.LENGTH_LONG).show();
                            //finish this activity with result OK to refresh the data from server

                            setResult(-1);
                            finish();
                        } else {
                            if (mProgressDialoginsert.isShowing()) {
                                mProgressDialoginsert.dismiss();
                            }
                            ErrorModel errorModel = ErrorUtils.parseError(response);
                            Toast.makeText(getBaseContext(), "Error type is " +"exbody"+response.errorBody().toString()+ "exmassege"+response.message()+errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PosterModel> call, Throwable t) {
                        //occur when fail to deserialize || no network connection || server unavailable
                        if (mProgressDialoginsert.isShowing()) {
                            mProgressDialoginsert.dismiss();
                        }
                        Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                });

            }
            else {
                Toast.makeText(getBaseContext(), "بعضی از فیلد ها خالی هستند", Toast.LENGTH_LONG).show();

            }

        } else if (id == android.R.id.home) {
            //back to main activity
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnClickDasteSelection(View v){
      //  startActivity(new Intent(getBaseContext(), DialogActivtyDaste.class));
        builder = new MaterialDialog.Builder(this)
                .title(R.string.titledaste)
                .itemsGravity(GravityEnum.END)

                //  .customView(R.layout.dialog_row, true)
                .buttonsGravity(GravityEnum.CENTER)
                .items(R.array.preference_values_group)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        if (text==null ) {
                            return true;
                        }

                            txt_group.setText(text.toString());
                            DasteModel.idgroup = which + 1;
                            Toast.makeText(getBaseContext(), text.toString(), Toast.LENGTH_SHORT).show();

                        return true;
                    }
                })


                .positiveText("انتخاب")
                .negativeText("بی خیال")
                .show();

    }





    public void OnClickSubDasteSelection(View v){
     //   startActivity(new Intent(getBaseContext(), DialogActivtySubdaste.class));
        builder = new MaterialDialog.Builder(this)
                .title(R.string.titlesubdaste)
                .itemsGravity(GravityEnum.END)

                //  .customView(R.layout.dialog_row, true)
                .buttonsGravity(GravityEnum.CENTER)
                .items(R.array.preference_values_subgroup)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        if (text==null ) {
                            return true;
                        }
                            txt_subdaste.setText(text.toString());
                            Subdaste.idsub = which + 1;
                            builder.setSelectedIndex(which);

                            Toast.makeText(getBaseContext(), text.toString(), Toast.LENGTH_SHORT).show();

                        return true;
                    }
                })


                .positiveText("انتخاب")
                .negativeText("بی خیال")
                .show();

    }

    public void OnClickLoctionSelection(View v){
       // startActivity(new Intent(getBaseContext(), DialogActivtyCity.class));
        builder = new MaterialDialog.Builder(this)
                .title(R.string.titlecity)
                .itemsGravity(GravityEnum.END)

                //  .customView(R.layout.dialog_row, true)
                .buttonsGravity(GravityEnum.CENTER)
                .items(R.array.preference_values_city_insert)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        if (text==null ) {
                            return true;
                        }

                            txt_selcity.setText(text.toString());
                            CityModel.idcity = which + 1;

                            Toast.makeText(getBaseContext(), text.toString(), Toast.LENGTH_SHORT).show();


                        return true;
                    }
                })


                .positiveText("انتخاب")
                .negativeText("بی خیال")
                .show();


    }


    public void OnClickReigonSelection(View v){
        // startActivity(new Intent(getBaseContext(), DialogActivtyCity.class));
        builder = new MaterialDialog.Builder(this)
                .title(R.string.titlereigon)
                .itemsGravity(GravityEnum.END)

                //  .customView(R.layout.dialog_row, true)
                .buttonsGravity(GravityEnum.CENTER)
                .items(R.array.preference_values_reigon_insert)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        if (text==null ) {
                            return true;
                        }

                        txt_location.setText(text.toString());
                        id_location = which + 1;

                        Toast.makeText(getBaseContext(), text.toString(), Toast.LENGTH_SHORT).show();


                        return true;
                    }
                })


                .positiveText("انتخاب")
                .negativeText("بی خیال")
                .show();


    }

    public void PictureSelection(){

        //open gallery to select single picture as image profile
        //before that check runtime permission
        if (checkRunTimePermissionIsGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startGalleryIntent();
        } else {
            //request write external permission for open camera intent
            requestRunTimePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 13, new PermissionEventListener() {
                @Override
                public void onGranted(int requestCode, String[] permissions) {
                    startGalleryIntent();
                }

                @Override
                public void onFailure(int requestCode, String[] permissions) {
                    Toast.makeText(getBaseContext(), "لطفا دسترسی به حافظه برای انتخاب عکس فعال کنید", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void startGalleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "انتخاب عکس"), Constants.GALLERY_REQUEST_CODE);
    }

    public void ImageCropFunction( Uri uri) {

        // Image Crop Code
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");

            intent.setData(uri);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            Intent i = new Intent(intent);


            startActivityForResult(i, 1);
        } catch (ActivityNotFoundException e) {

        }

    }

    private void requestRunTimePermission(String permissionType, int requestCode, PermissionEventListener permissionEventListener) {
        ActivityCompat.requestPermissions(this, new String[]{permissionType}, requestCode);
        mPermissionEventListener = permissionEventListener;
    }


    private boolean checkRunTimePermissionIsGranted(String permissionType) {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(TApplication.applicationContext, permissionType);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionEventListener != null) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionEventListener.onGranted(requestCode, permissions);
            } else {
                mPermissionEventListener.onFailure(requestCode, permissions);
            }
        }

    }


    public interface PermissionEventListener {
        void onGranted(int requestCode, String[] permissions);

        void onFailure(int requestCode, String[] permissions);
    }


}

