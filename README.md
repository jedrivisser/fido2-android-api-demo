# Android Fido2 Experiment

This project uses `play-services-fido`'s [Fido2ApiClient][1] to register a credential

Just run the app and click the `Register Fido2` button

As long as you do not change the Relying Party ID (in [PublicKeyCredentialRpEntity][2]), the signing key or the package
name, it should just work

## Relying Party ID

According to the Web Authentication [spec][3] the relying party id is:

> A valid domain string that identifies the WebAuthn Relying Party on whose behalf a given registration or authentication ceremony is being performed. A public key credential can only be used for authentication with the same entity (as identified by RP ID) it was registered with.

For Android apps you need to host an `assetlinks.json` file on `https://<rp_id>/.well-known/assetlinks.json` to allow it
to use the Fido2 apis for that domain.

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

`package_name` matched the `applicationId` in your `build.gradle`

`sha256_cert_fingerprints` matches the fingerprint of your signing key. This can be found by running the [script
included in this project][4]

## Hosting assetlinks.json

If you want to host your own assetlinks.json for a test, there is a firebase hosting project included in the [web][5] folder

You need to have a firebase project set up that you can deploy this site to first. When that is done, go to the `web` folder and run:

```console
firebase deploy --only hosting
```

You should now have the local [assetlinks.json][6] hosted on `https://<firebase-project-id>.firebaseapp.com/.well-known/assetlinks.json` and just need to set your RP ID to `<firebase-project-id>.firebaseapp.com`

[1]: https://developers.google.com/android/reference/com/google/android/gms/fido/fido2/Fido2ApiClient
[2]: https://developers.google.com/android/reference/com/google/android/gms/fido/fido2/api/common/PublicKeyCredentialRpEntity
[3]: https://www.w3.org/TR/webauthn/#relying-party-identifier
[4]: ./scripts/get_cert_fingerprint.sh
[5]: ./web
[6]: ./web/public/.well-known/assetlinks.json