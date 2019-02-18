# Fido2 Android Demo Web Component

Fido2 AppIDs can point to a list of trusted facets.

This projects hosts that list of trusted facets that contain the android certificate hashes using Firebase Hosting

See the [spec](https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html) for more info

## Deploying

You need to have a firebase project set up that you can deploy this site to first. When that is done, run:

```console
firebase deploy --only hosting
```