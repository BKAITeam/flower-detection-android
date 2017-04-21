package com.bkai.flowerdetect.logic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Random;

/**
 * Created by marsch on 4/20/17.
 */

public class Prediction extends Thread {
    Handler _handler;
    String result;

    public Prediction(Handler handler){
        this._handler = handler;
    }

    @Override
    public void run() {
        super.run();
        predict();
        Bundle bundle = new Bundle();
        bundle.putString("result", this.result);
        Message msg = new Message();
        msg.setData(bundle);
        this._handler.sendMessage(msg);
    }

    public void predict(){
        long sum = 0;

        for(int i=0;i< 700000000; i++){
            sum += 1;
        }
        Random rand = new Random();
        this.result = String.valueOf(rand.nextInt(10)+1);
    }
}
