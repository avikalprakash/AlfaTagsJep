package com.organization.jeptagsalpha.ui.activity.contact_us;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ContactUsActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setActionBar(getTitle().toString());
        ButterKnife.bind(this);
    }
    @OnClick(R.id.feedback)
    public void onClickFeedBack(View view)
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@jeptags.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback JepTags-Beta");
        email.putExtra(Intent.EXTRA_TEXT, "Write your feedback here");
        email.setType("message/rfc822");
        try{
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Not send", Toast.LENGTH_SHORT).show();
        }
    }
}
