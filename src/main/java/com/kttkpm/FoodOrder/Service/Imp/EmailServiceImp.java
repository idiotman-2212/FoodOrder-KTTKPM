package com.kttkpm.FoodOrder.Service.Imp;

public interface EmailServiceImp {
    void sendEmail(String to, String subject, String text);
}
