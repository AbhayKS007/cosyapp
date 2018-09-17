package co.project.cosy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPasswordMobileVerification extends AppCompatActivity {

    String otpp;
    String mobile;
    EditText otp;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_mobile_verification);

        otpp=getIntent().getExtras().getString("otp");
        mobile=getIntent().getExtras().getString("mobile");

        otp = findViewById(R.id.otp);

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otp.getText().toString().equalsIgnoreCase(otpp))
                {
                    Intent intent = new Intent(ForgetPasswordMobileVerification.this,ForgetChangePassword.class);
                    intent.putExtra("mobile",mobile);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Correct OTP",Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}
