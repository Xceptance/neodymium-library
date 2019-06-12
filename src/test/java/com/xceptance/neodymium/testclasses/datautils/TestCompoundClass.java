package com.xceptance.neodymium.testclasses.datautils;

import java.util.List;
import java.util.Map;

public class TestCompoundClass
{
    enum Level
    {
        LOW,
        MEDIUM,
        HIGH
    }

    String clubCardNumber;

    String description;

    Object notSet;

    Double numberValue;

    Object nullValue = new Object();

    int age;

    TestCreditCard creditCard;

    List<String> names;

    List<TestPerson> persons;

    Map<String, String> keyValueMap;

    Level level;

    public List<TestPerson> getPersons()
    {
        return persons;
    }

    public void setPersons(List<TestPerson> persons)
    {
        this.persons = persons;
    }

    public List<String> getNames()
    {
        return names;
    }

    public void setNames(List<String> names)
    {
        this.names = names;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getClubCardNumber()
    {
        return clubCardNumber;
    }

    public void setClubCardNumber(String clubCardNumber)
    {
        this.clubCardNumber = clubCardNumber;
    }

    public TestCreditCard getCreditCard()
    {
        return creditCard;
    }

    public void setCreditCard(TestCreditCard creditCard)
    {
        this.creditCard = creditCard;
    }

    public Map<String, String> getKeyValueMap()
    {
        return keyValueMap;
    }

    public void setKeyValueMap(Map<String, String> keyValueMap)
    {
        this.keyValueMap = keyValueMap;
    }

    public Level getLevel()
    {
        return level;
    }

    public void setLevel(Level level)
    {
        this.level = level;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Double getNumberValue()
    {
        return numberValue;
    }

    public void setNumberValue(Double numberValue)
    {
        this.numberValue = numberValue;
    }

    public Object getNullValue()
    {
        return nullValue;
    }

    public void setNullValue(Object nullValue)
    {
        this.nullValue = nullValue;
    }

    public void setNotSet(Object notSet)
    {
        this.notSet = notSet;
    }

    public Object getNotSet()
    {
        return notSet;
    }
}
