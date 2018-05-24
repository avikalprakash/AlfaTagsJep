package com.organization.jeptagsalpha.ui.activity.base;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import be.appfoundry.nfclibrary.utilities.interfaces.NfcMessageUtility;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcReadUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcMessageUtilityImpl;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;


public abstract class  NfcActivity extends BaseActivity implements NfcAdapter.CreateNdefMessageCallback {

        private static final String TAG = be.appfoundry.nfclibrary.activities.NfcActivity.class.getName();
        private static final String beamEnabled = "androidBeamEnabled";
        NfcAdapter mNfcAdapter;
        private List<String> mNfcMessageStrings;
        private NfcMessageUtility mNfcMessageUtility = new NfcMessageUtilityImpl();
        private NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
        private PendingIntent pendingIntent;
        private IntentFilter[] mIntentFilters;
        private String[][] mTechLists;
        private boolean mBeamEnabled;

        public NfcActivity() {
        }

        public NfcMessageUtility getNfcMessageUtility() {
            return this.mNfcMessageUtility;
        }

        public NfcReadUtility getNfcReadUtility() {
            return this.mNfcReadUtility;
        }

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.initAdapter();
            this.initFields();
        }

        protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            if(savedInstanceState.getBoolean("androidBeamEnabled")) {
                this.enableBeam();
            } else {
                this.disableBeam();
            }

        }

        public void onResume() {
            super.onResume();
            this.initAdapter();
            if(this.getNfcAdapter() != null) {
                this.getNfcAdapter().enableForegroundDispatch(this, this.pendingIntent, this.mIntentFilters, this.mTechLists);
                Log.d(TAG, "FGD enabled");
            }

        }

        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            Log.d(TAG, "Received intent!");
            this.setIntent(intent);
            if(this.getIntent() != null && ("android.nfc.action.NDEF_DISCOVERED".equals(intent.getAction()) || "android.nfc.action.TECH_DISCOVERED".equals(intent.getAction()))) {
                this.mNfcMessageStrings = this.transformSparseArrayToArrayList(this.mNfcReadUtility.readFromTagWithSparseArray(intent));
            }

        }

        protected void onSaveInstanceState(@NotNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putBoolean("androidBeamEnabled", this.mBeamEnabled);
        }

        protected void onPause() {
            super.onPause();
            if(this.getNfcAdapter() != null) {
                this.getNfcAdapter().disableForegroundDispatch(this);
                Log.d(TAG, "FGD disabled");
            }

        }

        public NdefMessage createNdefMessage(NfcEvent event) {
            return (new NfcMessageUtilityImpl()).createText("You\'re seeing this message because you have not overridden the createNdefMessage(NfcEvent event) in your activity.");
        }

        protected List<String> getNfcMessages() {
            return this.mNfcMessageStrings;
        }

        private void initFields() {
            this.pendingIntent = PendingIntent.getActivity(this, 0, (new Intent(this, this.getClass())).addFlags(536870912), 0);
            this.mIntentFilters = new IntentFilter[]{new IntentFilter("android.nfc.action.NDEF_DISCOVERED")};
            this.mTechLists = new String[][]{{Ndef.class.getName()}, {NdefFormatable.class.getName()}};
        }

        private void initAdapter() {
            if(this.getNfcAdapter() == null) {
                this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
                Log.d(TAG, "Adapter initialized");
            }

        }

        protected void enableBeam() {
            if(this.getNfcAdapter() != null) {
                this.getNfcAdapter().setNdefPushMessageCallback(this, this, new Activity[0]);
                this.mBeamEnabled = true;
                Log.d(TAG, "Beam enabled");
            }

        }

        protected boolean beamEnabled() {
            return this.mBeamEnabled;
        }

        protected void disableBeam() {
            if(this.getNfcAdapter() != null) {
                this.getNfcAdapter().setNdefPushMessageCallback((NfcAdapter.CreateNdefMessageCallback)null, this, new Activity[0]);
                this.mBeamEnabled = false;
                Log.d(TAG, "Beam disabled");
            }

        }

        protected NfcAdapter getNfcAdapter() {
            return this.mNfcAdapter;
        }

        protected void setNfcAdapter(NfcAdapter nfcAdapter) {
            this.mNfcAdapter = nfcAdapter;
        }

        protected List<String> transformSparseArrayToArrayList(SparseArray<String> sparseArray) {
            ArrayList list = new ArrayList(sparseArray.size());

            for(int i = 0; i < sparseArray.size(); ++i) {
                list.add(sparseArray.valueAt(i));
            }

            return list;
        }
}


