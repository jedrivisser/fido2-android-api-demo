#!/bin/bash
# Based on https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html#determining-the-facetid-of-a-calling-application
# Can also be calculated from within app as shown here: https://fidoalliance.org/specs/fido-v2.0-id-20180227/fido-appid-and-facets-v2.0-id-20180227.html#obtaining-facetid-of-android-native-app

echo Android Facet IDs:

echo -n android:apk-key-hash-sha256:
keytool -exportcert \
        -alias fido2 \
        -storepass fido2password \
        -keystore ../app/keystore.jks 2> /dev/null | \
        openssl sha256 -binary | \
        openssl base64 | \
        sed 's/=//g'

echo -n android:apk-key-hash:
keytool -exportcert \
        -alias fido2 \
        -storepass fido2password \
        -keystore ../app/keystore.jks 2> /dev/null | \
        openssl sha1 -binary | \
        openssl base64 | \
        sed 's/=//g'