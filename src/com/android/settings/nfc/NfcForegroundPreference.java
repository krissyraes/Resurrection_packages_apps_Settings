/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.nfc;

import android.content.Context;
import android.support.v7.preference.DropDownPreference;

import com.android.settings.R;

public class NfcForegroundPreference extends DropDownPreference implements
        PaymentBackend.Callback {

    private final PaymentBackend mPaymentBackend;
    public NfcForegroundPreference(Context context, PaymentBackend backend) {
        super(context);
        mPaymentBackend = backend;
        mPaymentBackend.registerCallback(this);
        refresh();
    }

    @Override
    public void onPaymentAppsChanged() {
        refresh();
    }

    void refresh() {
        PaymentBackend.PaymentAppInfo defaultApp = mPaymentBackend.getDefaultApp();
        boolean foregroundMode = mPaymentBackend.isForegroundMode();
        setPersistent(false);
        setTitle(getContext().getString(R.string.nfc_payment_use_default));
        CharSequence favorOpen;
        CharSequence favorDefault;
        setEntries(new CharSequence[] {
                getContext().getString(R.string.nfc_payment_favor_open),
                getContext().getString(R.string.nfc_payment_favor_default)
        });
        setEntryValues(new CharSequence[] { "1", "0" });
        if (foregroundMode) {
            setValue("1");
        } else {
            setValue("0");
        }
    }

    @Override
    protected boolean persistString(String value) {
        mPaymentBackend.setForegroundMode(Integer.parseInt(value) != 0);
        return true;
    }
}
