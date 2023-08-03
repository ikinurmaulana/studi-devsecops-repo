package com.example.jujojazbase;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.view.Menu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import json.JSONArray;
import json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddVehicle extends AppCompatActivity implements View.OnClickListener {

    private String Document_img1="";

//    private String textCarName;
//    private String textTipe;
//    private String textMerk;
//    private String textPajakHari;
//    private String textServisHari;
//    private String textFrom;
    private EditText textServisMulai;
    private EditText textPajakMulai;

    private ImageView IDProf;
    private ImageButton addPicture, addPhoto;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

//        textFrom = ((EditText)findViewById(R.id.textFrom)).getText().toString();
//        textCarName = ((EditText)findViewById(R.id.textCarName)).getText().toString();
//        textTipe = ((EditText)findViewById(R.id.textTipe)).getText().toString();
//        textMerk = ((EditText)findViewById(R.id.textMerk)).getText().toString();
//        textPajakHari = ((EditText)findViewById(R.id.textPajakHari)).getText().toString();
//        textServisHari = ((EditText)findViewById(R.id.textServisHari)).getText().toString();
        textPajakMulai = findViewById(R.id.textPajakMulai);
        textServisMulai = findViewById(R.id.textServisMulai);

        IDProf = findViewById(R.id.imagePict0);
        addPicture = findViewById(R.id.addPicture);
        addPicture.setOnClickListener(this);
        addPhoto = findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(this);
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(AddVehicle.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddVehicle.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(AddVehicle.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

        if (ContextCompat.checkSelfPermission(AddVehicle.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddVehicle.this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(AddVehicle.this,
                        new String[]{Manifest.permission.CAMERA},
                        2);
            }
        }

        textPajakMulai.setFocusableInTouchMode(false);
        textPajakMulai.setInputType(InputType.TYPE_NULL);
        textPajakMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.println(Log.INFO, "AddVehicle", "onClick called");
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(AddVehicle.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                textPajakMulai.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();

            }
        });

        textServisMulai.setFocusableInTouchMode(false);
        textServisMulai.setInputType(InputType.TYPE_NULL);
        textServisMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.println(Log.INFO, "AddVehicle", "onClick called");
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(AddVehicle.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                textServisMulai.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    IDProf.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath, options);

                if (thumbnail==null){
                    System.out.println("THUMBANAIL NULL");
                }
                thumbnail=getResizedBitmap(thumbnail, 400);
                Log.w("path of image: ", picturePath+"");
                IDProf.setImageBitmap(thumbnail);
                Log.d("AddVehicle", "Galery");
                BitMapToString(thumbnail);
            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        Log.d("LEN", String.valueOf(b.length));
        Document_img1 = Base64.encodeToString(b, Base64.NO_WRAP);
        Log.d("AddVehicle", Document_img1);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void sendDetail() {
        final ProgressDialog loading = new ProgressDialog(AddVehicle.this);
        loading.setMessage("Please Wait...");
        loading.show();
        loading.setCanceledOnTouchOutside(false);
        JujojazLib net = new JujojazLib(){

            @Override
            public  void onDone(List<Byte> x)  {
                Byte[] byteArray = new Byte[x.size()];
                x.toArray(byteArray);
                JSONObject data = new JSONObject(new String( Lib.Companion.Bytetobyte (byteArray) ));
                if (data.get("success").equals("1")) {
                    /*
                    final ProgressDialog loading = new ProgressDialog(AddVehicle.this);
                    loading.setMessage("Please Wait...");
                    loading.show();
                    loading.setCanceledOnTouchOutside(false);
                     */
                    JujojazLib net = new JujojazLib() {

                        @Override
                        public void onDone(List<Byte> x) {
                            Byte[] byteArray = new Byte[x.size()];
                            x.toArray(byteArray);
                            JSONObject data = new JSONObject(new String(Lib.Companion.Bytetobyte(byteArray)));
                            System.out.println(data.toString());
                            if (data.get("success").equals("1")) {
                                Auth.datas = new ArrayList<>();
                                for (Object o: (JSONArray) data.get("data")) {
                                    JSONObject dataObject = (JSONObject) o;
                                    Log.d("AddVehicleData", dataObject.toString());
                                    JSONObject dataFields = (JSONObject) dataObject.get("fields");
                                    Auth.datas.add(new ModelData(Integer.valueOf(dataObject.get("pk").toString()),
                                            dataFields.get("file_foto_b64").toString(),
                                            dataFields.get("from_name").toString(),
                                            dataFields.get("car_name").toString(),
                                            dataFields.get("merk").toString(),
                                            dataFields.get("tipe").toString(),
                                            dataFields.get("servis_selanjutnya").toString(),
                                            dataFields.get("pajak_selanjutnya").toString()

                                    ));
                                }
                            } else {
                                System.out.println("Fail");
                            }
                            Log.d("AddVehicleData", String.valueOf(Auth.datas.size()));
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    };
                    JSONObject authJson = new JSONObject();
                    authJson.put("username", Auth.user.getUsername());
                    authJson.put("password", Auth.user.getPassword());
                    net.sendUrl(Configuration.Companion.getAPI_SERVER() + "/api/allvehicles/", Lib.Companion.byteToByte(("data=" + authJson.toString()).getBytes()), 0);
                } else {
                    System.out.println("Gagal Menyimpan data");
                }
                loading.dismiss();

            }

            @Override
            public void onError(String msg){
                loading.dismiss();
                System.out.println("ERROR: " + msg);
            }
        };
        JSONObject dataJson = new JSONObject();
        dataJson.put("username", Auth.user.getUsername());
        dataJson.put("password", Auth.user.getPassword());
        dataJson.put("from_name", ((EditText)findViewById(R.id.textFrom)).getText().toString());
        dataJson.put("car_name", ((EditText)findViewById(R.id.textCarName)).getText().toString());
        dataJson.put("tipe", ((EditText)findViewById(R.id.textTipe)).getText().toString());
        dataJson.put("merk", ((EditText)findViewById(R.id.textMerk)).getText().toString());
//        dataJson.put("pajak_setiap_berapa_hari", ((EditText)findViewById(R.id.textPajakHari)).getText().toString());
        dataJson.put("pajak_selanjutnya", ((EditText)findViewById(R.id.textPajakMulai)).getText().toString());
//        dataJson.put("servis_setiap_berapa_hari", ((EditText)findViewById(R.id.textServisHari)).getText().toString());
        dataJson.put("servis_selanjutnya", ((EditText)findViewById(R.id.textServisMulai)).getText().toString());
        dataJson.put("photo", Document_img1);
        net.sendUrl(Configuration.Companion.getAPI_SERVER() + "/api/addvehicle/", Lib.Companion.byteToByte(("data="+dataJson.toString()).getBytes()), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPicture :
                Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                picture.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(AddVehicle.this, getPackageName() + ".provider", f));
                picture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(picture, 1);
                break;

            case R.id.addPhoto :
                Intent photo = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photo, 2);
                break;

            case R.id.fabAdd :
                sendDetail();
                break;
        }
    }
}