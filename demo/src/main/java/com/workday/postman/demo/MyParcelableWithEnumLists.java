package com.workday.postman.demo;

import android.os.Parcel;
import android.os.Parcelable;
import com.workday.postman.Postman;
import com.workday.postman.annotations.Parceled;

import java.util.ArrayList;

/**
 * @author kenneth.nickles
 * @since 2016-03-27.
 */
@Parceled
public class MyParcelableWithEnumLists implements Parcelable {

    public static final Creator<MyParcelableWithEnumLists> CREATOR = Postman.getCreator(
            MyParcelableWithEnumLists.class);

    ArrayList<MyEnum> myEnums = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Postman.writeToParcel(this, dest);
    }
}
