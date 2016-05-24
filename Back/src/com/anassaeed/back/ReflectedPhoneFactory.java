package com.anassaeed.back;

import java.lang.reflect.Method;

import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class ReflectedPhoneFactory {

public static final String TAG = "PHONE";

public static void makeDefaultPhones(Context context) throws IllegalArgumentException {

    try{

      ClassLoader cl = context.getClassLoader(); 
      @SuppressWarnings("rawtypes")
      Class PhoneFactory = cl.loadClass("com.android.internal.telephony.PhoneFactory");

      //Parameters Types
      @SuppressWarnings("rawtypes")
      Class[] paramTypes= new Class[1];
      paramTypes[0]= Context.class;

      Method get = PhoneFactory.getMethod("makeDefaultPhone",  paramTypes);

      //Parameters
      Object[] params= new Object[1];
      params[0]= context;

      get.invoke(null, params);

    }catch( IllegalArgumentException iAE ){
        throw iAE;
    }catch( Exception e ){
        Log.e(TAG, "makeDefaultPhones", e);
    }

}

public static void makeDefaultPhone(Context context) throws IllegalArgumentException {

    try{

      ClassLoader cl = context.getClassLoader(); 
      @SuppressWarnings("rawtypes")
      Class PhoneFactory = cl.loadClass("com.android.internal.telephony.PhoneFactory");

      //Parameters Types
      @SuppressWarnings("rawtypes")
      Class[] paramTypes= new Class[1];
      paramTypes[0]= Context.class;

      Method get = PhoneFactory.getMethod("makeDefaultPhone",  paramTypes);

      //Parameters
      Object[] params= new Object[1];
      params[0]= context;

      get.invoke(null, params);

    }catch( IllegalArgumentException iAE ){
        throw iAE;
    }catch( Exception e ){
        Log.e(TAG, "makeDefaultPhone", e);
    }

}

/*
 * This function returns the type of the phone, depending
 * on the network mode.
 *
 * @param network mode
 * @return Phone Type
 */
public static Integer getPhoneType(Context context, int networkMode) throws IllegalArgumentException {

    Integer ret= -1;

    try{

      ClassLoader cl = context.getClassLoader(); 
      @SuppressWarnings("rawtypes")
      Class PhoneFactory = cl.loadClass("com.android.internal.telephony.PhoneFactory");

      //Parameters Types
      @SuppressWarnings("rawtypes")
      Class[] paramTypes= new Class[1];
      paramTypes[0]= Integer.class;

      Method get = PhoneFactory.getMethod("getPhoneType", paramTypes);

      //Parameters
      Object[] params= new Object[1];
      params[0]= new Integer(networkMode);

      ret= (Integer) get.invoke(PhoneFactory, params);

    }catch( IllegalArgumentException iAE ){
        throw iAE;
    }catch( Exception e ){
        ret= -1;
    }

    return ret;

}

public static Object getDefaultPhone(Context context) throws IllegalArgumentException {

    Object ret= null;

    try{

        ClassLoader cl = context.getClassLoader(); 
        @SuppressWarnings("rawtypes")
        Class PhoneFactory = cl.loadClass("com.android.internal.telephony.PhoneFactory");

        Method get = PhoneFactory.getMethod("getDefaultPhone",  (Class[]) null);
        ret= (Object)get.invoke(null, (Object[]) null);

    }catch( IllegalArgumentException iAE ){
        throw iAE;
    }catch( Exception e ){
        Log.e(TAG, "getDefaultPhone", e);
    }

    return ret;

}

public static Phone getCdmaPhone(Context context) throws IllegalArgumentException {

    Phone ret= null;

    try{

      ClassLoader cl = context.getClassLoader(); 
      @SuppressWarnings("rawtypes")
      Class PhoneFactory = cl.loadClass("com.android.internal.telephony.PhoneFactory");

      Method get = PhoneFactory.getMethod("getCdmaPhone",  (Class[]) null);
      ret= (Phone)get.invoke(null, (Object[]) null);

    }catch( IllegalArgumentException iAE ){
        throw iAE;
    }catch( Exception e ){
        //
    }

    return ret;

}

public static Phone getGsmPhone(Context context) throws IllegalArgumentException {

    Phone ret= null;

    try{

      ClassLoader cl = context.getClassLoader(); 
      @SuppressWarnings("rawtypes")
      Class PhoneFactory = cl.loadClass("com.android.internal.telephony.PhoneFactory");

      Method get = PhoneFactory.getMethod("getGsmPhone",  (Class[]) null);
      ret= (Phone)get.invoke(null, (Object[]) null);

    }catch( IllegalArgumentException iAE ){
        throw iAE;
    }catch( Exception e ){
        //
    }

    return ret;

}
}