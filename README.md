# Android Fido2 Api Demo

> Google has released [offical docs][8] and a [sample project][9]

> Google released a [codelab][10]

This project uses `com.google.android.gms:play-services-fido`'s [Fido2ApiClient][1] to register a credential and also
sign a challenge.

It demonstrates the Fido2 Api with hardcoded values and is `NOT` a full example that gets requests from a server

It also does not validate any of the responses, as that would be done server side

Just run the app and tap the `Register Fido2` button. It will display the results on the app and in the logs.

Then you can tap the `Sign Fido2` button, and the Fido2 api will be used to sign a challenge with the key that was just
generated

As long as you do not change the Relying Party ID (in [PublicKeyCredentialRpEntity][2]), the signing key or the package
name, the app should just work

## Relying Party ID

According to the Web Authentication [spec][3] the relying party id is:

> A valid domain string that identifies the WebAuthn Relying Party on whose behalf a given registration or
> authentication ceremony is being performed. A public key credential can only be used for authentication with the same
> entity (as identified by RP ID) it was registered with.

According to [MakeCredentialOptions.Builder][7], Very hard to find, kind of hidden under `Public Methods` and then `setRp`:

> Note: the RpId should be an effective domain (aka, without scheme or port); and it should also be in secure context
> (aka https connection). Apps-facing API needs to check the package signature against Digital Asset Links, whose resource
> is the RP ID with prepended "//". Privileged (browser) API doesn't need the check.

So for Android apps you need to host an `assetlinks.json` file on `https://<rp_id>/.well-known/assetlinks.json` to allow
it to use the Fido2 apis for that domain.

For this sample app I have set the RP ID to `strategics-fido2.firebaseapp.com` and I am hosting this `assetlinks.json`:

```json
[
  {
    "relation": ["delegate_permission/common.handle_all_urls"],
    "target": {
      "namespace": "android_app",
      "package_name": "com.entersekt.fido2",
      "sha256_cert_fingerprints": [
        "C5:8B:E3:9B:36:B3:67:12:D7:0C:DA:C5:9D:65:2A:FC:43:9B:AE:1B:76:C9:7D:A1:7E:69:2B:7A:15:AB:27:96"
      ]
    }
  }
]
```

on <https://strategics-fido2.firebaseapp.com/.well-known/assetlinks.json> where:

`package_name` matches the `applicationId` in my `build.gradle` and the `sha256_cert_fingerprints` matches the
fingerprint of my signing key. This can be found by running the [get_cert_fingerprint.sh script included in this project][4]

## Hosting assetlinks.json

If you want to host your own assetlinks.json for a test, there is a firebase hosting project included in the [web][5] folder

You need to have a firebase project set up that you can deploy this site to first. When that is done, go to the `web` folder and run:

```console
firebase deploy --only hosting
```

You should now have the local [assetlinks.json][6] hosted on `https://<firebase-project-id>.firebaseapp.com/.well-known/assetlinks.json`
and just need to set your RP ID to `<firebase-project-id>.firebaseapp.com`

[1]: https://developers.google.com/android/reference/com/google/android/gms/fido/fido2/Fido2ApiClient
[2]: https://developers.google.com/android/reference/com/google/android/gms/fido/fido2/api/common/PublicKeyCredentialRpEntity
[3]: https://www.w3.org/TR/webauthn/#relying-party-identifier
[4]: ./scripts/get_cert_fingerprint.sh
[5]: ./web
[6]: ./web/public/.well-known/assetlinks.json
[7]: https://developers.google.com/android/reference/com/google/android/gms/fido/fido2/api/common/MakeCredentialOptions.Builder
[8]: https://developers.google.com/identity/fido/android/native-apps
[9]: https://github.com/googlesamples/android-fido
[10]: https://codelabs.developers.google.com/codelabs/fido2-for-android/#0
