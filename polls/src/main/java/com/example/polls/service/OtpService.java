package com.example.polls.service;




import com.example.polls.dto.StoreOtp;
import org.springframework.stereotype.Component;


@Component
public class OtpService {


    public void send(Long phone)  {

        int min = 100000;
        int max = 999999;
        int number = (int) (Math.random() * (max - min + 1) + min);

        String msg = "your OTP " + number + "please verify this OTP in your app by k";

        System.out.println(msg);

        StoreOtp.setOtp(number);
    }



}
