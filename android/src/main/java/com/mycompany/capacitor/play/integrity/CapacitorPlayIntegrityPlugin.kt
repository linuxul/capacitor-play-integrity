package com.mycompany.capacitor.play.integrity

import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginException
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest

@CapacitorPlugin(name = "CapacitorPlayIntegrity")
public class CapacitorPlayIntegrityPlugin : Plugin() {
    @PluginMethod
    public fun requestIntegrityToken(call: PluginCall) {
        val nonce = call.getString("nonce")
        // Both options are required by the TypeScript API. Leaving out the project number was a
        // NullPointerException when the Long was unboxed, which this keeps.
        val googleCloudProjectNumber = call.getLong("googleCloudProjectNumber")!!

        if (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) != ConnectionResult.SUCCESS) {
            throw PluginException("Play Services not found")
        }
        val integrityManager = IntegrityManagerFactory.create(context)

        val requestBuilder = IntegrityTokenRequest.builder().setNonce(nonce)
        if (googleCloudProjectNumber != 0L) {
            requestBuilder.setCloudProjectNumber(googleCloudProjectNumber)
        }
        val integrityTokenResponse = integrityManager.requestIntegrityToken(requestBuilder.build())
        integrityTokenResponse.addOnSuccessListener { response ->
            val ret = JSObject()
            ret.put("token", response.token())
            call.resolve(ret)
        }
        integrityTokenResponse.addOnFailureListener { e ->
            call.reject(e.message)
        }
    }
}
