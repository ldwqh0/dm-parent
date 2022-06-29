package com.dm.security.core;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public class TestInterner {

    public static void main(String[] args) {

        String str1 = new String("1");
        String str2 = new String("1");
        System.out.println("str1==str2");
        System.out.println(str1 == str2);

        Interner<String> interner1 = Interners.newWeakInterner();
        Interner<String> interner2 = Interners.newWeakInterner();

        System.out.println("str intern 1==str intern 2");
        System.out.println(interner1.intern(str1) == interner2.intern(str2));
    }
}
