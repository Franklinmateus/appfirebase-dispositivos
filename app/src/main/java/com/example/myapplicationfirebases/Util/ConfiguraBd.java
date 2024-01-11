package com.example.myapplicationfirebases.Util;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguraBd {
    private static FirebaseAuth auth;
    public  static  FirebaseAuth firebaseautenticacao(){
    if (auth==null){
        auth = FirebaseAuth.getInstance();


    }
        return auth;
    }
}
