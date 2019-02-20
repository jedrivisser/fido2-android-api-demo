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
import com.google.android.gms.fido.fido2.api.common.AuthenticatorAttestationResponse


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
        Log.d(LOG_TAG, "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")

        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {
            data?.extras?.getByteArray(Fido.FIDO2_KEY_ERROR_EXTRA)?.let {
                val authenticatorErrorResponse = AuthenticatorErrorResponse.deserializeFromBytes(it)
                val errorName = authenticatorErrorResponse.errorCode.name
                val errorMessage = authenticatorErrorResponse.errorMessage

                Log.e(LOG_TAG, "errorCode.name: $errorName")
                Log.e(LOG_TAG, "errorMessage: $errorMessage")

                val registerFidoResult = "An Error Ocurred\n\nError Name:\n$errorName\n\nError Message:\n$errorMessage"
                registerFidoResultText.text = registerFidoResult
                return
            }

            data?.extras?.getByteArray(Fido.FIDO2_KEY_RESPONSE_EXTRA)?.let {
                val response = AuthenticatorAttestationResponse.deserializeFromBytes(it)
                val keyHandleBase64 = Base64.encodeToString(response.keyHandle, Base64.DEFAULT)
                val clientDataJson = String(response.clientDataJSON, Charsets.UTF_8)
                val attestationObjectBase64 = Base64.encodeToString(response.attestationObject, Base64.DEFAULT)

                Log.d(LOG_TAG, "keyHandleBase64: $keyHandleBase64")
                Log.d(LOG_TAG, "clientDataJSON: $clientDataJson")
                Log.d(LOG_TAG, "attestationObjectBase64: $attestationObjectBase64")

                val registerFidoResult = "Authenticator Attestation Response\n\n" +
                        "keyHandleBase64:\n" +
                        "$keyHandleBase64\n\n" +
                        "clientDataJSON:\n" +
                        "$clientDataJson\n\n" +
                        "attestationObjectBase64:\n" +
                        "$attestationObjectBase64\n"
                registerFidoResultText.text = registerFidoResult
            }
        }
    }

    private fun registerFido2() {
        val publicKeyCredentialCreationOptions = PublicKeyCredentialCreationOptions.Builder()
            .setRp(PublicKeyCredentialRpEntity("strategics-fido2.firebaseapp.com", "Fido2Demo", null))
            .setUser(PublicKeyCredentialUserEntity("demo@example.com".toByteArray(), "demo@example.com", null, "Demo User"))
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
