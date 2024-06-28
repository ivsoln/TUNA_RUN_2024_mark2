package com.inventive.tunarun

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.afterKeyEnteredThenCloseKeyboard
import com.inventive.tunarun.Instant.Companion.popupText
import java.util.Calendar

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUserId: EditText
    private lateinit var txtPassword: EditText
    private lateinit var lnkLogin: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var overlay: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        txtUserId = findViewById(R.id.txt_userId)
        txtPassword = findViewById(R.id.txt_password)

        progressBar = findViewById(R.id.progressBar)
        overlay = findViewById(R.id.overlay)

        txtUserId.afterKeyEntered {
            txtPassword.requestFocus()
            txtPassword.selectAll()
        }



        txtPassword.requestFocus()
        txtPassword.afterKeyEnteredThenCloseKeyboard {
            login()
        }

        lnkLogin = findViewById(R.id.lnk_login)
        lnkLogin.setOnClickListener {
            login()
        }

    }


    private fun login() {
        progressBar.visibility = View.VISIBLE
        overlay.visibility = View.VISIBLE

        val userId = txtUserId.text.toString();
        val password = txtPassword.text.toString()


        val user = Fish.Security.User()
        user.UserId = userId
        user.Password = password

        user.CreateTime = Calendar.getInstance().time

        val callback = object : ActionRequest.Callback {
            override fun <T> onSuccess(result: T) {
                progressBar.visibility = View.GONE
                overlay.visibility = View.GONE
                var identity = result as Fish.Auth.Identity

                if (identity.IsAuthenticated) {
                    popupText(identity.SessionId)
                    FishClient.Companion.Skipjack.Identity = identity
                    Log.i("TUNA RUN > LOGIN", "OK")
                    setResult(RESULT_OK)
                    finish() //close form
                }else{
                    txtPassword.requestFocus()
                    txtPassword.selectAll()
                    popupText(identity.Description)
                }
            }

            override fun onError(result: String) {
                progressBar.visibility = View.GONE
                overlay.visibility = View.GONE
                txtUserId.requestFocus()
                txtUserId.selectAll()
                Log.e("TUNA RUN > LOGIN > ERROR", result)
            }
        }
        FishClient.AuthClient(this).also {
            it.login(user, callback)
        }
    }
}