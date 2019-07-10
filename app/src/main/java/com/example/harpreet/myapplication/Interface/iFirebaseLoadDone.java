package com.example.harpreet.myapplication.Interface;

import com.example.harpreet.myapplication.Data;

import java.util.List;

public interface iFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Data> list);
    void onFirebaseLoadFailed(String message);

}
