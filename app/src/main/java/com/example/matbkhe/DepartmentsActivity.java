package com.example.matbkhe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class DepartmentsActivity extends AppCompatActivity {

    final private static int CAMERA_PIC_REQUEST=54;
    final   private static int PICK_IMAGE_REQUEST=36;
    Uri filepath;
    ImageView img;
    EditText txtname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        img=findViewById(R.id.imgdept);
txtname=findViewById(R.id.txtdname);
    }

    public void ChooseImage(View view) {
        AlertDialog.Builder ms=new AlertDialog.Builder(this);
        ms.setTitle("Please choose method to get image")
                .setPositiveButton("From Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);


                    }
                }).setNegativeButton("From Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);


            }
        });
        ms.create().show();
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent h) {
        super.onActivityResult(reqCode, resultCode, h);
        switch (reqCode) {
            case (CAMERA_PIC_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bb = (Bitmap) h.getExtras().get("data");
                    img.setImageBitmap(bb);
                }
                break;
            case PICK_IMAGE_REQUEST:
                if (resultCode == Activity.RESULT_OK) {

                    filepath=h.getData();

                    Bitmap ss = null;
                    try {
                        ss = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(ss);
                }
                break;

        }
    }

    private void uploadImage() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        final StorageReference ref = storageRef.child("Departments/"+ txtname.getText().toString());
        ref.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                progressDialog.dismiss();
                                String path = downloadUrl.toString();
                                Database db=new Database();
                                db.ConnectDB();
                                db.RunDML("insert into departments values('"+txtname.getText().toString()+"','"+path+"')");
                                Toast.makeText(DepartmentsActivity.this, "Department Saved Done", Toast.LENGTH_SHORT).show();


                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(DepartmentsActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%....");
                    }
                });

    }


    public void Upload(View view) {
        uploadImage();
    }
}
