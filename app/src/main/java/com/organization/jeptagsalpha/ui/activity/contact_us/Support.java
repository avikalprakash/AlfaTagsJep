package com.organization.jeptagsalpha.ui.activity.contact_us;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.organization.jeptagsalpha.R;

public class Support extends AppCompatActivity {
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        send=(Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
    }
}
