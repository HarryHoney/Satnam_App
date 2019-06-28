package com.example.harpreet.myapplication.Account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.harpreet.myapplication.MainActivity;
import com.example.harpreet.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.CropImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class setup extends AppCompatActivity {

    private EditText Name;
    private CircleImageView circleImageView=null;//this is been add to make the image round and github dependence is been used for it
    private Uri inputUri=null;
    private Uri OuputUri=null;
    String user_id;
    private FirebaseAuth mauth;
    private StorageReference storageReference;
    private ProgressBar setup_progressbar;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mauth=FirebaseAuth.getInstance();//connection to the authentication server

        storageReference=FirebaseStorage.getInstance().getReference(); //getting the reference of the storage
        //that is the path to storage on the server is saved here

        firebaseFirestore=FirebaseFirestore.getInstance();//data is stored on the cloud here the connection to the firestore is been established
        user_id=mauth.getCurrentUser().getUid();

        //getting id from the layout
        circleImageView = findViewById(R.id.profile_image);
        Name=findViewById(R.id.name);
        setup_progressbar=findViewById(R.id.progressBar3);
        setup_progressbar.setVisibility(View.INVISIBLE);

        if(circleImageView!=null)
        {
            setup_progressbar.setVisibility(View.VISIBLE);
        }

        //if the user is already logged in then we should retrive the data of his profile to display so refering to the same collection and userID
        //the below line will only make the connection and will consider as successful is the connection is establised with FireStore
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {

                        //this will check whether the path existed or not for which the data retriveal is asked i.e ID is newly made or old
                        //for old Id
                        String name=task.getResult().getString("name");
                        String image=task.getResult().getString("image");

                        Name.setText(name);
                        RequestOptions requestOptions=new RequestOptions();
                        requestOptions.placeholder(R.drawable.default_image);
                        Glide.with(setup.this).setDefaultRequestOptions(requestOptions).load(image).into(circleImageView);
                        Glide.with(setup.this).load(image).into(circleImageView);
                    }
                    else
                    {
                        Toast.makeText(setup.this,"no data",Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    //if the connection wasn't properly establised with Firestore
                    String error=task.getException().getMessage();
                    Toast.makeText(setup.this,"Firestore Retrieve Error:"+error,Toast.LENGTH_LONG).show();
                }

                setup_progressbar.setVisibility(View.INVISIBLE);

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for the android versions greater than the mashmallo the permission to the storage is to be granted during runtime
                //before this use doing while installing from playstore
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(setup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(setup.this, "Permission Denied!", Toast.LENGTH_LONG).show();
                        //the toast message will appear for the deny for the first time but in below line the connection is established
                        ActivityCompat.requestPermissions(setup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        //when there is no problem for accessing the storage. the Intent is been created here to choose the file from the storage
                        //here Crop dependence is been used from git hub for cropping
                        CropImage.activity()
                                .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                                .start(setup.this);


                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                doSomethingWithCroppedImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }




    }

    private void doSomethingWithCroppedImage(Uri outputUri) {
        OuputUri=outputUri;
        String s=outputUri.toString();
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
        circleImageView.setImageURI(outputUri);
    }

    public void save(View view)
    {

        //Important thing to note the image is been stored to the storage(Firebase Storage) and its reference is been stored in the Database(Firestore or cloud)

        final String user_name=Name.getText().toString();
        if(TextUtils.isEmpty(user_name))
        {
            Toast.makeText(setup.this,"Please Enter the Name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            setup_progressbar.setVisibility(View.VISIBLE);

            user_id=mauth.getCurrentUser().getUid();//id of each image will be uniquely created

            StorageReference image_path=storageReference.child("Profile_Image").child(user_id+".jpg");//storing the image to storage on specified path
            image_path.putFile(OuputUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        //Toast.makeText(setup.this, "here1", Toast.LENGTH_SHORT).show();
                        Uri download_uri=task.getResult().getUploadSessionUri();//in short getting the referal code where the image is been stored in storage
                        //actual fuction was getdownloaduri
                       // Toast.makeText(setup.this, download_uri.toString(), Toast.LENGTH_SHORT).show();
                        HashMap<String,String> user_map=new HashMap<>();
                        user_map.put("Name",user_name);
                        user_map.put("Profile_Image",download_uri.toString());

                        //setting data to the firestore or cloud in below line
                        firebaseFirestore.collection("Users").add(user_map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(setup.this,"Upload Complete",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(setup.this,MainActivity.class));
                                    finish();
                                }
                                else
                                {
                                    String error=task.getException().getMessage();
                                    Toast.makeText(setup.this,"Firestore Error:"+error,Toast.LENGTH_LONG).show();
                                }

                                setup_progressbar.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                    else
                    {
                        String error=task.getException().getMessage();
                        Toast.makeText(setup.this,"Image Error:"+error,Toast.LENGTH_LONG).show();
                        setup_progressbar.setVisibility(View.INVISIBLE);
                    }

                }
            });

        }
    }
}
