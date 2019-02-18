# Android Fido2 Experiment

This project launches a Fido2 RegisterIntent using a `Fido2PendingIntent`

When the intent is launched, it hangs on a blank white screen. After a while it times out with an error in
`onActivityResult` containing errorCode name `TIMEOUT_ERR` and errorMessage `Request doesn't finish on time!`

## Scripts

The `get_facet_ids.sh` script calculates the app's facet IDs as described in the [spec](https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.htm)

## Website

The web component in the web folder contains code to host the trusted facets on Firebase Hosting
