package com.xceptance.neodymium.util;

import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.RandomStringGenerator;

public class DataUtils
{
    /**
     * Returns a random email address using UUID.java
     *
     * @return random email
     */
    public static String randomEmail()
    {
        final String uuid = UUID.randomUUID().toString();
        final String data = uuid.replaceAll("-", "");
        final StringBuilder sb = new StringBuilder(42);

        sb.append(Context.get().configuration.dataUtilsEmailLocalPrefix());
        sb.append(data.concat(data).substring(0, 12));
        sb.append("@");
        sb.append(Context.get().configuration.dataUtilsEmailDomain());

        return sb.toString().toLowerCase();
    }

    /**
     * A random password that is strong enough for most services
     *
     * @return a password
     */
    public static String randomPassword()
    {
        final String upper = new RandomStringGenerator.Builder().selectFrom("abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray()).build()
                                                                .generate(Context.get().configuration.dataUtilsPasswordUppercaseCharAmount());
        final String lower = new RandomStringGenerator.Builder().selectFrom("abcdefghijklmnopqrstuvwxyz".toCharArray()).build()
                                                                .generate(Context.get().configuration.dataUtilsPasswordLowercaseCharAmount());
        final String number = new RandomStringGenerator.Builder().selectFrom("0123456789".toCharArray()).build()
                                                                 .generate(Context.get().configuration.dataUtilsPasswordDigitAmount());
        final String special = new RandomStringGenerator.Builder().selectFrom(Context.get().configuration.dataUtilsPasswordSpecialChars().toCharArray()).build()
                                                                  .generate(Context.get().configuration.dataUtilsPasswordSpecialCharAmount());

        final char[] all = (upper + lower + number + special).toCharArray();
        ArrayUtils.shuffle(all);

        return new String(all);
    }
}
