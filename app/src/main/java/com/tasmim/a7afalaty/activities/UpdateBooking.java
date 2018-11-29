package com.tasmim.a7afalaty.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.tasmim.a7afalaty.model.MyBookingModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBooking extends AppCompatActivity implements View.OnClickListener {

    EditText update_booking_name_ed, update_booking_phone_ed, update_booking_date_ed, update_booking_desc_ed;
    Button update_booking_btn;
    TextView back;
    private Calendar myCalendar = Calendar.getInstance();

    ApiService apiService;
    MyBookingModel.Datum booking_data;
    NetworkTester networkTester = new NetworkTester(this);
    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking);

        if (getIntent().getExtras() != null) {
            booking_data = getIntent().getExtras().getParcelable("booking_data");
        }
        back = findViewById(R.id.update_booking_back_txt_id);
        update_booking_name_ed = findViewById(R.id.update_booking_name_ed);
        update_booking_phone_ed = findViewById(R.id.update_booking_phone_ed);
        update_booking_date_ed = findViewById(R.id.update_booking_date_ed);
        update_booking_desc_ed = findViewById(R.id.update_booking_details_ed);
        update_booking_btn = findViewById(R.id.confirm_update_booking_btn_id);

        update_booking_name_ed.setText(booking_data.getName());
        update_booking_phone_ed.setText(booking_data.getPhone());
        update_booking_date_ed.setText(booking_data.getDate());
        update_booking_desc_ed.setText(booking_data.getDisc());

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
                update_booking_date_ed.setText(sdf.format(myCalendar.getTime()));
            }
        };
        update_booking_date_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateBooking.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        update_booking_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (!FUtilsValidation.isEmpty(update_booking_name_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(update_booking_phone_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(update_booking_date_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(update_booking_desc_ed, getString(R.string.required))
                ) {
            if (networkTester.isNetworkAvailable()) {
//                startActivity(new Intent(Login.this, Home.class));
                dialog = new SpotsDialog(UpdateBooking.this, getString(R.string.updatting), R.style.Custom);
                dialog.show();
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<BookingModel> call = apiService.updateBooking(Home.user_Data.getId() + "", booking_data.getServiceId() + "", booking_data.getId() + "", update_booking_name_ed.getText().toString().trim(),
                        update_booking_phone_ed.getText().toString().trim(), update_booking_date_ed.getText().toString().trim(), update_booking_desc_ed.getText().toString().trim());
                call.enqueue(new Callback<BookingModel>() {
                    @Override
                    public void onResponse(Call<BookingModel> call, Response<BookingModel> response) {
                        dialog.dismiss();
                        BookingModel bookingModel = response.body();
                        String status = bookingModel.getStatus();
                        if (status.equals("success")) {
                            Toasty.error(UpdateBooking.this, "تم ارسال الطلب, فى انتظار الموافقة", 1500).show();
                        } else {
                            Toast.makeText(UpdateBooking.this, "برجاء اختيار تاريخ مستقبلى!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BookingModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        }
    }
}
