package com.organization.jeptagsalpha.ui.activity.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductImageViewActivity extends BaseActivity {

    @Bind(R.id.prod_image)
    ImageView imageView;
    @Bind(R.id.prod_image_progress)
    ProgressBar progressBar;
    public static void start(Context context, String url) {
        Intent intent = new Intent(context, ProductImageViewActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image_view);
        setActionBar(getTitle().toString());
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        String url = getIntent().getStringExtra("url");
        if(url == null || url.equals(""))
        {
            finish();
        }
        else
        {
            Picasso.with(this).load(url).
                    error(R.drawable.default_product).
                    into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }

    }


}
