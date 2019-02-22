#!/bin/bash
echo Android Certificate fingerprint

keytool -list -v -alias fido2 -storepass fido2password -keystore ../app/keystore.jks 2> /dev/null | grep SHA256: