package com.manLoader.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A C++-like string class, without unmutable bulldshit.
 * Created by KithLenovo on 06/04/2015.
 */
public class CppString {

    private static final int DEFAULT_SIZE = 16;

    private char[] base;
    private int length;

    public CppString(){
        base = new char[DEFAULT_SIZE];
        length = 0;

    }

    public CppString(String str){
        str.getChars(0,str.length(),base,0);
    }

    public CppString(char[] base){
        this.base = new char[base.length];
        System.arraycopy(base,0,this.base,0,base.length);

        length = base.length;
    }


    public CppString(StringBuffer buffer){
        buffer.getChars(0,buffer.length(),base,0);
        length = base.length;
    }

    public CppString(CppString cppString) {
        this(cppString.base);
        this.length = cppString.length;
    }


    public void append(char c){
        if(base.length == length){
            enlargeBuffer(length +1);
        }
        length++;
        base[length] = c;



    }

    public CppString substring(int begin,int end){
        if(begin < 0 || end > length){
            throw new IndexOutOfBoundsException(end+" is out of the string");
        }
        return  new CppString(Arrays.copyOfRange(base,begin,end));
    }

    public CppString substring(int begin){
        return substring(begin,base.length);

    }


    public int indexOf(String expression){
        return indexOf(expression,0);
    }

    public int indexOf(String expression, int beginIndex){
        StringBuffer buffer = new StringBuffer();
        buffer.append(Arrays.copyOfRange(base,beginIndex,length));

        return buffer.indexOf(expression);

    }

    @Override
    public String toString() {
        return new String(base,0,length);

    }

    private void enlargeBuffer(int min) {
        int newCount = ((base.length >> 1) + base.length) + 2;
        char[] newData = new char[min > newCount ? min : newCount];
        System.arraycopy(base, 0, newData, 0, length);
        base = newData;
    }

    public String[] split(String s) {
        int currentIndex = 0;
        CppString currentString = new CppString(this);
        List<String> result = new ArrayList<String>();
        while(currentIndex != -1){
            currentIndex = currentString.indexOf(s);
            if(currentIndex == -1){
                break;
            }
            String token = currentString.substring(0,currentIndex).toString();
            result.add(token);

            currentString = currentString.substring(currentIndex+s.length(),length);
        }

        return result.toArray(new String[result.size()]);
    }
}
