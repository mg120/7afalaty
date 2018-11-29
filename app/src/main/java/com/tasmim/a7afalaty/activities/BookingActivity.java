package com.tasmim.a7afalaty.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.BookingModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name_ed, phone_ed, booking_date_ed, details_ed;
    Button confirm_booking_btn;
    TextView back;
    private Calendar myCalendar = Calendar.getInstance();
    int service_id;

    ApiService apiService;
    SpotsDialog dialog;
    NetworkTester networkTester = new NetworkTester(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        if (getIntent().getExtras() != null) {
            service_id = getIntent().getExtras().getInt("service_id");
        }
        back = findViewById(R.id.booking_back_txt_id);
        name_ed = findViewById(R.id.booking_name_ed);
        phone_ed = findViewById(R.id.booking_phone_ed);
        booking_date_ed = findViewById(R.id.booking_date_ed);
        details_ed = findViewById(R.id.booking_details_ed);
        confirm_booking_btn = findViewById(R.id.confirm_booking_btn_id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                booking_date_ed.setText(sdf.format(myCalendar.getTime()));
            }
        };
        booking_date_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(BookingActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        confirm_booking_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(booking_date_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(details_ed, getString(R.string.required))
                ) {

            Log.e("id : ", service_id + "");
            Log.e("booking_name : ", name_ed.getText().toString().trim());
            Log.e("booking_phone : ", phone_ed.getText().toString().trim());
            Log.e("booking_date : ", booking_date_ed.getText().toString().trim());
            Log.e("desc : ", details_ed.getText().toString().trim());
            if (networkTester.isNetworkAvailable()) {
                dialog = new SpotsDialog(BookingActivity.this, getString(R.string.sending), R.style.Custom);
                dialog.show();
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<BookingModel> call = apiService.booking_service(Home.user_id + "", service_id + "", name_ed.getText().toString().trim(),
                        phone_ed.getText().toString().trim(), booking_date_ed.getText().toString().trim(), details_ed.getText().toString().trim());
                call.enqueue(new Callback<BookingModel>() {
                    @Override
                    public void onResponse(Call<BookingModel> call, Response<BookingModel> response) {
                        dialog.dismiss();
                        BookingModel bookingModel = response.body();
                        String status = bookingModel.getStatus();
                        if (status.equals("success")) {
                            Toasty.success(BookingActivity.this, "تم ارسال الطلب, فى انتظار الموافقة", 1500).show();
                            finish();
                        } else {
                            Toasty.error(BookingActivity.this, bookingModel.getData(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BookingModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            } else {
                Toasty.error(BookingActivity.this, getString(R.string.error_connection), 1500).show();
            }
        }
    }
}