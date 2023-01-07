package com.gwtf.flow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.BitmapThumbnailImageViewTarget;

import java.text.DecimalFormat;

public class GSTCalculateActivity extends AppCompatActivity {

    private Button _btnOne;
    private Button _btnTwo;
    private Button _btnThree;
    private Button _btnFour;
    private Button _btnFive;
    private Button _btnSix;
    private Button _btnSeven;
    private Button _btnEight;
    private Button _btnNine;
    private Button _btnZero;
    private Button btnac;
    private Button _btnPoint;
    private FrameLayout _btnClear;
    private TextView _finalPrice;
    private TextView _beforeTaxPrice;

    private double _finalValue;
    private TextView _tvFinalPrice;
    private TextView _tvBeforeTax;
    private static int _count = 0;

    private Button threePer;
    private Button fivePer;
    private Button twelweper;
    private Button eighteenper;
    private Button tweentyeightper;

    private String _GSTTaxes;
    private TextView stateGST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gstcalculate);

        _tvBeforeTax = (TextView) findViewById(R.id.tv_beforeTax);
        _finalPrice = (TextView) findViewById(R.id.tv_finalPriceNo);
        _beforeTaxPrice = (TextView) findViewById(R.id.tv_beforeTaxNo);
        stateGST = (TextView) findViewById(R.id.stateGST);

        threePer = (Button) findViewById(R.id.threePer);
        fivePer = (Button) findViewById(R.id.fivePer);
        twelweper = (Button) findViewById(R.id.twelweper);
        eighteenper = (Button) findViewById(R.id.eighteenper);
        tweentyeightper = (Button) findViewById(R.id.tweentyeightper);

        _btnOne = (Button) findViewById(R.id.btn_one);
        _btnTwo = (Button) findViewById(R.id.btn_two);
        _btnThree = (Button) findViewById(R.id.btn_three);
        _btnFour = (Button) findViewById(R.id.btn_four);
        _btnFive = (Button) findViewById(R.id.btn_five);
        _btnSix = (Button) findViewById(R.id.btn_six);
        _btnSeven = (Button) findViewById(R.id.btn_seven);
        _btnEight = (Button) findViewById(R.id.btn_eight);
        _btnNine = (Button) findViewById(R.id.btn_nine);
        _btnZero = (Button) findViewById(R.id.btn_zero);
        btnac = (Button) findViewById(R.id.btnac);
        _btnPoint = (Button) findViewById(R.id.btn_point);

        _btnClear = (FrameLayout) findViewById(R.id.layout_clear);

        _btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "1");
                calculateFinalPrice();
            }
        });

        _btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "2");
                calculateFinalPrice();
            }
        });

        _btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "3");
                calculateFinalPrice();
            }
        });

        _btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "4");
                calculateFinalPrice();
            }
        });

        _btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "5");
                calculateFinalPrice();
            }
        });

        _btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "6");
                calculateFinalPrice();
            }
        });

        _btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "7");
                calculateFinalPrice();
            }
        });

        _btnEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "8");
                calculateFinalPrice();
            }
        });

        _btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "9");
                calculateFinalPrice();
            }
        });

        _btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "0");
                calculateFinalPrice();
            }
        });

        _btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_count == 0){
                    if(_beforeTaxPrice.length() == 0){
                        _beforeTaxPrice.setText(_beforeTaxPrice.getText() + "0.");
                        _count++;
                    }else{
                        _beforeTaxPrice.setText(_beforeTaxPrice.getText() + ".");
                        _count++;
                    }
                }

                calculateFinalPrice();
            }
        });

        _btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(_beforeTaxPrice.getText().length() != 0){
                    String str = _beforeTaxPrice.getText().toString();
                    str = str.substring(0, str.length() - 1);
                    _beforeTaxPrice.setText(str);
                }

                if(_beforeTaxPrice.getText().length() == 0){
                    _finalPrice.setText("");
                    _count = 0;
                }

                if(_beforeTaxPrice.getText().length() !=0) {
                    String mString = _beforeTaxPrice.getText().toString();
                    if(mString.contains(".")){
                        _count ++;
                    }else{
                        _count = 0;
                    }
                }

                calculateFinalPrice();
            }
        });

        twelweper.callOnClick();
        _GSTTaxes = "12";
        calculateFinalPrice();
//        mClearValue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                _beforeTaxPrice.setText("");
//                _finalPrice.setText("");
//                _count = 0;
//            }
//        });

        threePer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _GSTTaxes = "03";
                calculateFinalPrice();
            }
        });

        fivePer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _GSTTaxes = "05";
                calculateFinalPrice();
            }
        });

        twelweper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _GSTTaxes = "12";
                calculateFinalPrice();
            }
        });

        eighteenper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _GSTTaxes = "18";
                calculateFinalPrice();
            }
        });

        tweentyeightper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _GSTTaxes = "28";
                calculateFinalPrice();
            }
        });

        btnac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _beforeTaxPrice.setText("0");
                calculateFinalPrice();
            }
        });

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void calculateFinalPrice(){
        float gst_percent = Float.parseFloat(_GSTTaxes);
        if(!TextUtils.isEmpty(_beforeTaxPrice.getText().toString())) {
            float total = Float.parseFloat(_beforeTaxPrice.getText().toString());
            float gst_amount = (gst_percent / 100) * total;
            float totalAmount = total + gst_amount;
            _finalPrice.setText("\u20B9 " + totalAmount);
            stateGST.setText("CGST/SGST: " + gst_percent/2);
        } else {
            _beforeTaxPrice.setText("0");
            _finalPrice.setText("0");
            stateGST.setText("CGST/SGST: " + gst_percent/2);
        }

    }

}