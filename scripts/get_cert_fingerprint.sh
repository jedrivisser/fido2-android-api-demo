#!/bin/bash
# Based on https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html#determining-the-facetid-of-a-calling-application
# Can also be calculated from within app as shown here: https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html#obtaining-facetid-of-android-native-app

echo Android Certificate fingerprint

keytool -list -v -alias fido2 -storepass fido2password -keystore ../app/keystore.jks 2> /dev/null | grep SHA256: