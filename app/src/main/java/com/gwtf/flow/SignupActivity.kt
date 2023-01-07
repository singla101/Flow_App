package com.gwtf.flow

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import android.widget.EditText
import android.os.Bundle
import com.gwtf.flow.R
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import com.gwtf.flow.VerifyOTPActivity
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.gwtf.flow.Utilites.LoadingDialog
import com.gwtf.flow.Utilities.LoadingScreen
import com.gwtf.flow.databinding.ActivitySignupBinding
import io.grpc.internal.DnsNameResolver.SrvRecord
import java.util.concurrent.TimeUnit

class SignupActivity : AppCompatActivity() {

    lateinit var mauth: FirebaseAuth
    lateinit var mCallbacks: OnVerificationStateChangedCallbacks
    lateinit var txt_mobile: EditText
    lateinit var btn_sendOtp: Button

    var number : String = ""
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    lateinit var dialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mauth = FirebaseAuth.getInstance()

        dialog = LoadingDialog(this)

        txt_mobile = findViewById(R.id.txt_mobile)
        btn_sendOtp = findViewById(R.id.btn_sendOtp)
        btn_sendOtp.isEnabled = true
        btn_sendOtp.setOnClickListener(View.OnClickListener {
            dialog.start()
            login()
        })

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                dialog.dismiss()
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                dialog.dismiss()
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
                val intent = Intent(applicationContext, VerifyOTPActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                intent.putExtra("phone",txt_mobile.text.toString())
                dialog.dismiss()
                startActivity(intent)
                finish()
            }
        }
    }

    private fun login() {
        number = txt_mobile.text.toString()
        if (number.isNotEmpty()){
            number = "+91$number"
            sendVerificationCode(number)
        }else{
            txt_mobile.error = "Enter valid mobile number"
            dialog.dismiss()
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mauth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG" , "Auth started")
    }
}