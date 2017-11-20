package com.assignment.photostory.helper;

import android.content.Context;

import io.realm.Realm;

/**
 * Created by heeyan on 2017. 11. 20..
 */

public class RealmHelper {
    private static Realm realm;

    public static void init(Context context){
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public static Realm getRealm() {
        return realm;
    }

    public static void transaction(Realm.Transaction execute) {
        realm.executeTransactionAsync(execute);
    }

    public static void transaction(Realm.Transaction execute, Realm.Transaction.OnSuccess success) {
        realm.executeTransactionAsync(execute, success);
    }

    public static void transaction(Realm.Transaction execute, Realm.Transaction.OnSuccess success, Realm.Transaction.OnError error) {
        realm.executeTransactionAsync(execute, success, error);
    }
}
