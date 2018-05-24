package com.organization.jeptagsalpha.utils;


import com.google.gson.Gson;
import com.mobapphome.mahencryptorlib.MAHEncryptor;
import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.ui.model.NfcModel;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public  class NfcConvertor {
    public static String encode(NfcModel nfcModel) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        if(nfcModel != null) {
            Gson gson = new Gson();
            String data=  gson.toJson(nfcModel);
            MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(MyConstants.ENC_KEY);
            return mahEncryptor.encode(data);
        }
        return  "";
    }
    public static NfcModel decode(String message) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        if(message!= null || message!= "")
        {
            MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(MyConstants.ENC_KEY);
            String data = mahEncryptor.decode(message);
            Gson gson = new Gson();
            return gson.fromJson(data,NfcModel.class);
        }
        return null;
    }
}
