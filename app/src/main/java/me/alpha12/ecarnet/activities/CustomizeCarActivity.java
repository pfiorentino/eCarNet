package me.alpha12.ecarnet.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.classes.ScalingUtilities;
import me.alpha12.ecarnet.classes.Utils;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.CarModel;

import static me.alpha12.ecarnet.classes.ScalingUtilities.bitmapCirclify;
import static me.alpha12.ecarnet.classes.ScalingUtilities.createScaledBitmap;
import static me.alpha12.ecarnet.classes.ScalingUtilities.decodeFile;

public class CustomizeCarActivity
        extends AppCompatActivity
        implements OnDateSetListener, ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {
    private static final int SELECT_PICTURE_INTENT = 1;
    private static final int SELECT_COVER_INTENT = 2;

    private CarModel selectedCar;
    private Calendar selectedDate;

    private TextView titleTextView;
    private TextView dateTextView;

    private EditText imat;
    private EditText kilometers;

    private Button finishButton;
    private Button backButton;

    private Uri pictureUri;
    private Uri coverUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_car);

        int idModel = getIntent().getExtras().getInt("id");
        selectedCar = CarModel.findById(idModel);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.append(" "+ Utils.ucWords(selectedCar.getBrand()+" "+selectedCar.getModel()));

        imat = (EditText) findViewById(R.id.imat);
        imat.addTextChangedListener(mTextWatcher);
        kilometers = (EditText) findViewById(R.id.kilometers);
        kilometers.addTextChangedListener(mTextWatcher);

        dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setOnClickListener(this);

        ((Button)findViewById(R.id.btn_addPicture)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_addCover)).setOnClickListener(this);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(this);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void afterTextChanged(Editable editable) {
            checkForm();
        }
    };

    @Override
    public void onClick(View v) {
        Intent photoPickerIntent;

        switch (v.getId()){
            case R.id.dateTextView:
                DialogFragment newFragment = DatePickerFragment.newInstance(this.selectedDate, this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.btn_addPicture:
                photoPickerIntent = new Intent(Intent.ACTION_PICK).setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PICTURE_INTENT);
                break;
            case R.id.btn_addCover:
                photoPickerIntent = new Intent(Intent.ACTION_PICK).setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_COVER_INTENT);
                break;
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.finishButton:
                if (noPictureSet() || isStoragePermissionGranted())
                    saveCarAndContinue();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        selectedDate = Calendar.getInstance();
        selectedDate.set(year, monthOfYear, dayOfMonth);
        dateTextView.setText(getFormattedDate(this, selectedDate));
        checkForm();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // Save car with pictures
            saveCarAndContinue();
        } else {
            // ToDo
            // Add a warning message to user wich specify that his images won't be uploaded if he
            // didn't allow eCarNet to access to his local storage

            // Save the car without pictures
            saveCarAndContinue(false);
        }
    }

    private void checkForm() {
        finishButton.setEnabled(
            !imat.getText().toString().matches("") &&
            !kilometers.getText().toString().matches("") &&
            selectedDate != null
        );
    }

    private boolean noPictureSet() {
        return pictureUri == null && coverUri == null;
    }

    private static String getFormattedDate(Context ctx, Calendar c) {
        return DateUtils.formatDateTime(ctx, c.getTimeInMillis(),
            DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case GlobalContext.RESULT_CLOSE_ALL:
                setResult(GlobalContext.RESULT_CLOSE_ALL);
                finish();
                break;
            case RESULT_OK:
                switch (requestCode) {
                    case SELECT_PICTURE_INTENT:
                        pictureUri = data.getData();
                        break;
                    case SELECT_COVER_INTENT:
                        coverUri = data.getData();
                        break;
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveCarAndContinue() {
        saveCarAndContinue(true);
    }

    private void saveCarAndContinue(boolean withPictures) {
        Car car = new Car(
            imat.getText().toString(),
            Integer.parseInt(kilometers.getText().toString()),
            new Date(selectedDate.getTimeInMillis()),
            selectedCar
        );

        if (getIntent().hasExtra("carId")){
            int carId = getIntent().getExtras().getInt("carId");
            if (carId > 0)
                car.setId(carId);
        }
        car.persist(true);

        GlobalContext.setCurrentCar(car.getId());

        if (withPictures) {
            if (pictureUri != null)
                savePicture(pictureUri, false);

            if (coverUri != null)
                savePicture(coverUri, true);
        }

        Intent intent = new Intent(CustomizeCarActivity.this, MainActivity.class);
        CustomizeCarActivity.this.startActivity(intent);
        setResult(GlobalContext.RESULT_CLOSE_ALL);
        finish();
    }

    private void savePicture(Uri selectedImage, boolean isCover) {
        try {
            if (!Environment.getExternalStorageDirectory().canWrite())
                return;

            // Handling source file
            String sourceImagePath = mediaUriToString(selectedImage);
            File source = new File(sourceImagePath);
            if (!source.exists())
                return;
/*
            Bitmap unscaledBitmap; = ScalingUtilities.decodeFile(source.getAbsolutePath(), dstWidth, dstHeight, scalingLogic);
            Bitmap scaledBitmap; = createScaledBitmap(unscaledBitmap, dstWidth, dstHeight, scalingLogic);*/


            // Cropping and resizing
            Bitmap outputBitmap;
            String outputFilename = String.valueOf(GlobalContext.getCurrentCar());
            Bitmap.CompressFormat format;
            float density = GlobalContext.getInstance().getResources().getDisplayMetrics().density;
            if (isCover) {
                outputFilename += "_cover.png";
                format = Bitmap.CompressFormat.PNG;

                int imageWidth = GlobalContext.getInstance().getResources().getDisplayMetrics().widthPixels;
                int imageHeight = imageWidth * 9 / 16;

                outputBitmap = createScaledBitmap(decodeFile(
                        source.getAbsolutePath(),
                        imageWidth,
                        imageHeight,
                        ScalingUtilities.ScalingLogic.CROP
                ), imageWidth, imageHeight, ScalingUtilities.ScalingLogic.CROP);
            } else {
                outputFilename += "_picture.png";
                format = Bitmap.CompressFormat.PNG;

                int imageLength = (int)(64 * density);

                // Preparing the source bitmap
                outputBitmap = bitmapCirclify(createScaledBitmap(decodeFile(
                        source.getAbsolutePath(),
                        imageLength,
                        imageLength,
                        ScalingUtilities.ScalingLogic.CROP
                ), imageLength, imageLength, ScalingUtilities.ScalingLogic.CROP));
            }

            // Handling writing
            File destination = new File(GlobalContext.getAppPicturePath() + outputFilename);
            if (!destination.getParentFile().exists())
                destination.getParentFile().mkdirs();

            if (destination.exists())
                destination.delete();

            destination.createNewFile();

            FileOutputStream fos = new FileOutputStream(destination);
            outputBitmap.compress(format, 45, fos); // The "quality" is ignored for PNG
            fos.close();
        } catch (Exception e) {
            Log.e("CustomizeCar", e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    private String mediaUriToString(Uri uri)
    {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String string = cursor.getString(columnIndex);
        cursor.close();

        return string;
    }

    public static class DatePickerFragment extends DialogFragment {
        private OnDateSetListener onDateSetListener;

        public static DatePickerFragment newInstance(Calendar date, OnDateSetListener onDateSetListener) {
            DatePickerFragment pickerFragment = new DatePickerFragment();
            pickerFragment.setOnDateSetListener(onDateSetListener);

            //Pass the date in a bundle.
            Bundle bundle = new Bundle();
            bundle.putSerializable("SELECTED_DATE", date);
            pickerFragment.setArguments(bundle);
            return pickerFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar initialDate = (Calendar) getArguments().getSerializable("SELECTED_DATE");

            final Calendar c;
            if (initialDate != null) {
                c = initialDate;
            } else {
                c = Calendar.getInstance();
            }

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (CustomizeCarActivity) getActivity(), year, month, day);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                dialog.getDatePicker().setMaxDate(new Date().getTime());
            }
            return dialog;
        }

        private void setOnDateSetListener(OnDateSetListener listener) {
            this.onDateSetListener = listener;
        }
    }
}
