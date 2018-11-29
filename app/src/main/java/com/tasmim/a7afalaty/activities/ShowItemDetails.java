package com.tasmim.a7afalaty.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.fourhcode.forhutils.FUtilsValidation;
import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.adapter.CommentsAdapter;
import com.tasmim.a7afalaty.model.CommentServiceModel;
import com.tasmim.a7afalaty.model.ItemDetailsModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowItemDetails extends AppCompatActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    SliderLayout sliderLayout;
    ArrayList<String> layouts = new ArrayList<>();
    TextView back, item_name_txt, item_views_txt, no_comments_txt, item_desc_txt, rate_service_txt;
    RatingBar ratingBar;
    Button book_now_btn;
    ProgressBar progressBar;
    LinearLayout details_layout;
    RecyclerView comments_recycler;
    CommentsAdapter commentsAdapter;

    ApiService apiService;
    int item_id;
    SpotsDialog dialog;
    NetworkTester networkTester = new NetworkTester(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_details);
        if (getIntent().getExtras() != null) {
            item_id = getIntent().getExtras().getInt("item_id");
//            Toast.makeText(this, "" + item_id, Toast.LENGTH_SHORT).show();
        }
        back = findViewById(R.id.show_item_detail_back_txt_id);
        sliderLayout = findViewById(R.id.banner_slider);
        item_name_txt = findViewById(R.id.show_details_item_Name);
        item_views_txt = findViewById(R.id.show_details_item_shows);
        item_desc_txt = findViewById(R.id.show_details_item_desc);
        rate_service_txt = findViewById(R.id.rate_txtV_id);
        ratingBar = findViewById(R.id.show_details_item_rate);
        book_now_btn = findViewById(R.id.booking_now_btn_id);
        no_comments_txt = findViewById(R.id.no_available_comments);
        comments_recycler = findViewById(R.id.comments_recycler);
        details_layout = findViewById(R.id.item_details_layout_id);
        progressBar = findViewById(R.id.item_details_progress);

        getItemData(item_id + "");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        book_now_btn.setOnClickListener(this);
        rate_service_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Home.user_id != -1) {
                    final Dialog pass_Dialog = new Dialog(ShowItemDetails.this);
                    pass_Dialog.setContentView(R.layout.new_pass_popup);
                    TextView close_txt = pass_Dialog.findViewById(R.id.txt_close);
                    final RatingBar ratingBar = pass_Dialog.findViewById(R.id.service_ratingbar);
                    final EditText comment_ed = pass_Dialog.findViewById(R.id.comment_ed_id);
                    Button publish_btn = pass_Dialog.findViewById(R.id.send_pass_btn);

                    pass_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    pass_Dialog.show();
                    pass_Dialog.setCancelable(false);
                    close_txt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pass_Dialog.dismiss();
                        }
                    });
                    publish_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String comment = comment_ed.getText().toString().trim();
                            float rate = ratingBar.getRating();

                            if (!FUtilsValidation.isEmpty(comment_ed, getString(R.string.required))) {
                                dialog = new SpotsDialog(ShowItemDetails.this, getString(R.string.sending), R.style.Custom);
                                dialog.show();

                                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                                Call<CommentServiceModel> call = apiService.commentService(Home.user_id + "", item_id + "", comment, rate + "");
                                call.enqueue(new Callback<CommentServiceModel>() {
                                    @Override
                                    public void onResponse(Call<CommentServiceModel> call, Response<CommentServiceModel> response) {
                                        dialog.dismiss();
                                        CommentServiceModel commentServiceModel = response.body();
                                        pass_Dialog.dismiss();
                                        Toasty.success(ShowItemDetails.this, "تم اضافة تعليقك بنجاح", 1500).show();
                                    }

                                    @Override
                                    public void onFailure(Call<CommentServiceModel> call, Throwable t) {
                                        dialog.dismiss();
                                        pass_Dialog.dismiss();
                                        t.printStackTrace();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(ShowItemDetails.this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(ShowItemDetails.this, Login.class));
                }
            }
        });
    }

    private void getItemData(String item_id) {

        apiService = ApiClient.getClient().create(ApiService.class);
        Call<ItemDetailsModel> call = apiService.itemDetails(item_id);
        call.enqueue(new Callback<ItemDetailsModel>() {
            @Override
            public void onResponse(Call<ItemDetailsModel> call, Response<ItemDetailsModel> response) {
                ItemDetailsModel itemDetailsModel = response.body();
                String status = itemDetailsModel.getStatus();
                ItemDetailsModel.Data data = itemDetailsModel.getData();
                List<ItemDetailsModel.Comment> commenst_list = data.getComments();
                progressBar.setVisibility(View.GONE);
                details_layout.setVisibility(View.VISIBLE);
                item_name_txt.setText(String.valueOf(data.getTitle()));
                item_views_txt.setText(data.getViews() + "");
                item_desc_txt.setText(data.getDisc());
                ratingBar.setRating(Float.parseFloat(String.valueOf(data.getStars())));
                item_desc_txt.setText(data.getDisc());

                // set Images to Slider Layout...
                layouts.add(data.getImage());
                for (String name : layouts) {
                    TextSliderView textSliderView = new TextSliderView(ShowItemDetails.this);
                    // initialize a SliderLayout
                    textSliderView.image(name)
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(ShowItemDetails.this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    sliderLayout.addSlider(textSliderView);
                }
                sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                sliderLayout.setCustomAnimation(new DescriptionAnimation());
                sliderLayout.setDuration(4000);
                sliderLayout.addOnPageChangeListener(ShowItemDetails.this);

                if (commenst_list.size() > 0) {
                    GridLayoutManager layoutManager = new GridLayoutManager(ShowItemDetails.this, 1);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    comments_recycler.setLayoutManager(layoutManager);
                    comments_recycler.setHasFixedSize(true);

                    commentsAdapter = new CommentsAdapter(ShowItemDetails.this, commenst_list);
                    comments_recycler.setAdapter(commentsAdapter);
                } else {
                    comments_recycler.setVisibility(View.GONE);
                    no_comments_txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ItemDetailsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (Home.user_id != -1) {
            Intent intent = new Intent(ShowItemDetails.this, BookingActivity.class);
            // Send Data to Booking Activity...
            intent.putExtra("service_id", item_id);
            startActivity(intent);
        } else {
            Toast.makeText(ShowItemDetails.this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(ShowItemDetails.this, Login.class));
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
