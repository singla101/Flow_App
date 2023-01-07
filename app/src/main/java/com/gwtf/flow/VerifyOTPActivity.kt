package com.gwtf.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gwtf.flow.R
import com.google.android.gms.tasks.OnCompleteListener
import android.content.Intent
import android.view.View
import android.widget.*
import com.google.firebase.auth.*
import com.gwtf.flow.Database.SqlDatabase
import com.gwtf.flow.MainActivity
import com.gwtf.flow.Utilites.LoadingDialog

class VerifyOTPActivity() : AppCompatActivity() {

    lateinit var VerificationId: String
    lateinit var txt_otp: EditText
    lateinit var btn_verify_otp: Button
    lateinit var Etphone: TextView
    lateinit var btn_resendOtp: TextView
    lateinit var auth: FirebaseAuth

    lateinit var dialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifyotp)
        auth=FirebaseAuth.getInstance()
        dialog = LoadingDialog(this)

        val help = findViewById<TextView>(R.id.help)
        help.setOnClickListener {
            val subject = "Support: Flow"
            val intent = Intent(Intent.ACTION_SEND)
            val addressees = arrayOf("gurjotsinghharika@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, addressees)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.setType("message/rfc822")
            startActivity(Intent.createChooser(intent, "Send Email using:"));
        }

        btn_resendOtp = findViewById(R.id.btn_resendOtp)
        txt_otp = findViewById(R.id.txt_otp)
        Etphone = findViewById(R.id.Etphone)
        btn_verify_otp = findViewById(R.id.btn_verify_otp)
        Etphone.setText(
            String.format(
                "We have sent OTP to your mobile number +91-%s | Change",
                intent.getStringExtra("phone")
            )
        )

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        btn_resendOtp.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        })

        val storedVerificationId= intent.getStringExtra("storedVerificationId")
        btn_verify_otp.isEnabled = true
        btn_verify_otp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                dialog.start()
                val otp = findViewById<EditText>(R.id.txt_otp).text.trim().toString()
                if(otp.isNotEmpty()){
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId.toString(), otp)
                    signInWithPhoneAuthCredential(credential)
                }else{
                    dialog.dismiss()
                    txt_otp.error = "Enter Valid OTP!"
                }
            }
        })
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val sharedPref = getSharedPreferences(packageName, MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.apply {
                        putString("PhoneNumber", intent.getStringExtra("phone"))
                        apply()
                    }
                    val db = SqlDatabase(this)


                    db.addPaymentMode("Online")
                    db.addPaymentMode("PhonePe")
                    db.addPaymentMode("Paytm")
                    db.addPaymentMode("Google Pay")
                    db.addPaymentMode("Bank Account")
                    db.addPaymentMode("Debit Card")
                    db.addPaymentMode("Credit Card")
                    db.addPaymentMode("Net Banking")

                    val intent = Intent(this , CreateBusinessActivity::class.java)
                    dialog.dismiss()
                    startActivity(intent)
                    finish()
                } else {
                    dialog.dismiss()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        txt_otp.error  = "Invalid OTP"
                    }
                }
            }
    }
}