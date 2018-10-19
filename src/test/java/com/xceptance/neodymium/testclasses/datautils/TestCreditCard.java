package com.xceptance.neodymium.testclasses.datautils;

public class TestCreditCard
{
    private String cardNumber;

    private String ccv;

    private int month;

    private int year;

    public String getCardNumber()
    {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    public String getCcv()
    {
        return ccv;
    }

    public void setCcv(String ccv)
    {
        this.ccv = ccv;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }
}
