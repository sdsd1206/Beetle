package com.example.jack.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class sell_selldata_good_additem extends AppCompatActivity {

    private static final int pic = 100; //驗證碼
    private static final int pro = 101; //驗證碼
    private static final int REQUEST_EXTERNAL_STORAGE = 200;
    String imgpathitem;
    String imgpathprove;
    private FirebaseStorage mStorageRef = FirebaseStorage.getInstance();
    StorageReference afteruploaditem;
    StorageReference afteruploadprove;

    private Button yes, cancel;
    private sell_selldata_good store = new sell_selldata_good();
    private DatabaseReference ref;
    private ArrayList<Object> data = new ArrayList();
    private TextView sellername;
    private TextView nickname;
    String[] tag = {"3C", "配件", "服飾", "鞋類", "美妝", "食品", "日常", "其他"};
    Spinner tagspinner;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String spinnertx;
    private ImageButton itempic;
    private ImageButton itemprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_selldata_good_additem);

        itempic = findViewById(R.id.imageButton2);
        itemprove = findViewById(R.id.imageButton4);

        tagspinner = findViewById(R.id.spinner2);
        setspinner();
        sellername = findViewById(R.id.textView11);
        Intent msg = this.getIntent();
        String name = msg.getStringExtra("seller");
        sellername.setText(name + " 的賣場");

        nickname = findViewById(R.id.textView12);
        getsellername();
        getdata();

        myactionbar();

        //取得存取手機權限
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            getimg(); //取圖片
        }

        yes = (Button) findViewById(R.id.additem_yes_btn);
        cancel = (Button) findViewById(R.id.additem_can_btn);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //傳給資料庫並返回商品頁面
                Intent intent = new Intent(getApplicationContext(), sell.class);
                startActivity(intent);
                additem();
                sell_selldata_good_additem.this.finish();
                Toast.makeText(getApplicationContext(), "新增成功", Toast.LENGTH_SHORT).show();
            }//end onClick
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消返回商品頁面
                Intent intent = new Intent(getApplicationContext(), sell.class);
                startActivity(intent);
                sell_selldata_good_additem.this.finish();
                Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
            }//end onClick
        });

    }

    public void myactionbar() //actionbar
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("新增賣場");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void getsellername() {
        ref = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nickname.setText("Nickname: " + dataSnapshot.child("nickname").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getdata() {
        ref = FirebaseDatabase.getInstance().getReference("good").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            item item;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    item = ds.getValue(item.class);
                    data.add(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void additem() {
        if (user == null) return;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        final DatabaseReference myRef = database.getReference("good");
        //圖片ref
        Uri file = Uri.fromFile(new File(imgpathitem));
        afteruploaditem = mStorageRef.getReference("productpic").child(file.getLastPathSegment());
        Uri file1 = Uri.fromFile(new File(imgpathprove));
        afteruploadprove = mStorageRef.getReference("prove").child(file1.getLastPathSegment());

        EditText name = findViewById(R.id.editText4);
        final String itemname = name.getText().toString();
        EditText price = findViewById(R.id.editText5);
        final String itemprice = price.getText().toString();
        EditText info = findViewById(R.id.multiAutoCompleteTextView2);
        final String iteminfo = info.getText().toString();

        Random r = new Random();
        final String months = String.valueOf(r.nextInt(200) + 1);
        final item item;
        item = new item(itemname, itemprice, "0", iteminfo, spinnertx, user.getDisplayName(), user.getUid());

        //圖片url
        if (afteruploaditem == null) {
            Toast.makeText(sell_selldata_good_additem.this, "請先上傳照片", Toast.LENGTH_SHORT).show();
        }
        afteruploaditem.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                item.setPic(uri.toString());
                if (afteruploadprove == null) {
                    Toast.makeText(sell_selldata_good_additem.this, "請先上傳照片", Toast.LENGTH_SHORT).show();
                }
                afteruploadprove.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        item.setProve(uri.toString());
                        String key = myRef.child(user.getUid()).push().getKey();
                        item.setKey(key); //刪商品會用到
                        data.add(item);

                        myRef.child(user.getUid()).child(key).setValue(data.get(data.size() - 1));//只能刪最後一個，不然會崩壞
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.getMessage();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.getMessage();
            }
        });


    }

    public void setspinner() {
        ArrayAdapter<String> quantity = new ArrayAdapter<String>(sell_selldata_good_additem.this,
                android.R.layout.simple_spinner_dropdown_item,
                tag);
        tagspinner.setAdapter(quantity);
        tagspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnertx = tagspinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //取圖片下載網址
    public String getimguri(final StorageReference ref) {
        final String[] s = {""};
        if (ref == null) {
            Toast.makeText(sell_selldata_good_additem.this, "請先上傳照片", Toast.LENGTH_SHORT).show();
        }
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("geturi,uri" + uri.toString());
                s[0] = uri.toString();
                System.out.println("geturi,String" + s[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.getMessage();
            }
        });
        return s[0];
    }


    //取權限,不同意就結束App
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getimg();
                } else {
                    Toast.makeText(sell_selldata_good_additem.this, "請開啟權限", Toast.LENGTH_SHORT).show();
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //選圖片
    public void getimg() {
        itempic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("image/*");
                picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                Intent destIntent = Intent.createChooser(picker, null);
                startActivityForResult(destIntent, pic);
            }
        });
        itemprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("image/*");
                picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                Intent destIntent = Intent.createChooser(picker, null);
                startActivityForResult(destIntent, pro);
            }
        });
    }

    //接住回傳回來的資料,設置img+上傳
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                imgpathitem = getPath(sell_selldata_good_additem.this, uri);//傳回手機檔案位址
                if (imgpathitem != null && !imgpathitem.equals("")) {
                    Glide.with(sell_selldata_good_additem.this).load(imgpathitem).into(itempic);
                } else {
                    Toast.makeText(sell_selldata_good_additem.this, "Unknow error", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(sell_selldata_good_additem.this, "請等候上傳", Toast.LENGTH_SHORT).show();
                //上傳
                String path = getPath(sell_selldata_good_additem.this, uri);
                Uri file = Uri.fromFile(new File(path));
                afteruploaditem = mStorageRef.getReference("productpic").child(file.getLastPathSegment());
                UploadTask uploadTask = afteruploaditem.putFile(file);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.getMessage();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(sell_selldata_good_additem.this, "上傳成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (requestCode == pro) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                imgpathprove = getPath(sell_selldata_good_additem.this, uri);//傳回uri
                if (imgpathprove != null && !imgpathprove.equals("")) {
                    Glide.with(sell_selldata_good_additem.this).load(imgpathprove).into(itemprove);
                } else {
                    Toast.makeText(sell_selldata_good_additem.this, "Unknow error", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(sell_selldata_good_additem.this, "請等候上傳", Toast.LENGTH_SHORT).show();

                //上傳
                String path = getPath(sell_selldata_good_additem.this, uri);
                Uri file = Uri.fromFile(new File(path));
                afteruploadprove = mStorageRef.getReference("prove").child(file.getLastPathSegment());
                UploadTask uploadTask = afteruploadprove.putFile(file);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.getMessage();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(sell_selldata_good_additem.this, "上傳成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    //舊版取path
//    public static String getRealPathFromURI(Context context, Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = {MediaStore.Images.Media.DATA};
//            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}

