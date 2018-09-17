package co.project.cosy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class TestingActivity extends AppCompatActivity {

    String[] testarr = {"B","C","D","B","D","B"};
    String val;
    ArrayList x = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        for(int i = 0; i<testarr.length;i++)
        {
           val =  testarr[i];
           for (int j = 0;j<testarr.length;j++)
           {
               if(val == testarr[j])
               {
                   x.add(val);


               }
           }

        }
        System.out.println("final x "+x.toString());
    }
}
