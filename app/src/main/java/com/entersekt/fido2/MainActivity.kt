package com.entersekt.fido2

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.api.common.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "Fido2Demo"
        private const val REQUEST_CODE_REGISTER = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerFido2Button.setOnClickListener { registerFido2() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {
            data?.extras?.getByteArray("FIDO2_ERROR_EXTRA")?.let {
                val authenticatorErrorResponse = AuthenticatorErrorResponse.deserializeFromBytes(it)
                val errorName = authenticatorErrorResponse.errorCode.name
                val errorMessage = authenticatorErrorResponse.errorMessage

                Log.e(LOG_TAG, "errorCode.name: $errorName")
                Log.e(LOG_TAG, "errorMessage: $errorMessage")

                val registerFidoResult = "An Error Ocurred\n\nError Name:\n$errorName\n\nError Message:\n$errorMessage"
                registerFidoResultText.text = registerFidoResult
            }
        }
    }

    private fun registerFido2() {
        val publicKeyCredentialCreationOptions = PublicKeyCredentialCreationOptions.Builder()
            .setRp(PublicKeyCredentialRpEntity("webauthndemo.appspot.com", "webauthndemo.appspot.com", null))
            .setUser(PublicKeyCredentialUserEntity("test@example.com".toByteArray(), "test@example.com", null, "test@example.com"))
            .setChallenge(Base64.decode("elJqUyk/3LqVui51GSsyLky2flVPEFdtCAUsNUvFHvo=", Base64.DEFAULT))
            .setParameters(listOf(PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY.toString(), EC2Algorithm.ES256.algoValue)))
            .build()

        val fido2ApiClient = Fido.getFido2ApiClient(applicationContext)
        val fido2PendingIntentTask = fido2ApiClient.getRegisterIntent(publicKeyCredentialCreationOptions)
        fido2PendingIntentTask.addOnSuccessListener { fido2PendingIntent ->
            if (fido2PendingIntent.hasPendingIntent()) {
                try {
                    Log.d(LOG_TAG, "launching Fido2 Pending Intent")
                    fido2PendingIntent.launchPendingIntent(this@MainActivity, REQUEST_CODE_REGISTER)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
