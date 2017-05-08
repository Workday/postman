# postman

Postman is a java library that uses code generation to handle the details of implementing the [Parcelable](http://developer.android.com/reference/android/os/Parcelable.html) interface on Android.

__Latest Version:__ [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.workday/postman/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.workday/postman)

__Build Status:__ [![Build Status](https://travis-ci.org/Workday/postman.svg?branch=master)](https://travis-ci.org/Workday/postman)

## The Problem

Postman is intended to be used in cases where you have parcelable classes with a lot of fields that need to be serialized. At Workday, we had a lot of classes like the following to represent the responses from server.

```java
public class MyParcelable implements Parcelable {

    int myInt;
    String myString;
    MyChildParcelable myChildParcelable;
    ArrayList<MyChildParcelable> myParcelableList;
    ArrayList<String> myStringList;

    public static final Creator<MyParcelable> CREATOR = new Creator<MyParcelable>() {

        @Override
        public MyParcelable createFromParcel(Parcel source) {
            MyParcelable myParcelable = new MyParcelable();
            Bundle bundle = source.readBundle();
            myParcelable.myInt = bundle.getInt("myInt");
            myParcelable.myString = bundle.getString("myString");
            myParcelable.myChildParcelable = bundle.getParcelable("myChildData");
            myParcelable.myParcelableList = bundle.getParcelableArrayList("myParcelableList");
            myParcelable.myStringList = bundle.getStringArrayList("myStringList");
            return myParcelable;
        }

        @Override
        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt("myInt", myInt);
        bundle.putString("myString", myString);
        bundle.putParcelable("myChildData", myChildParcelable);
        bundle.putParcelableArrayList("myParcelableList", myParcelableList);
        bundle.putStringArrayList("myStringList", myStringList);
        dest.writeBundle(bundle);
    }
}
```

If you have to write several classes like this, it gets very tedious and tiresome very quickly. And if you're like most software developers, you want to avoid tedious and tiresome like the plague.

## The Solution

There is nothing fancy or unexpected in making a class parcelable. There is a lot of boiler plate and it all follows the same pattern. We could write a program that writes this code for us. So we did.

We wrote an annotation processor that can read through a class that you mark as a candidate for processing, and it will generate all the code in the `Creator` and in `writeToParcel(Parcel, int)`. The same class from above now becomes this:

```java
@Parceled
public class MyParcelable implements Parcelable {

    int myInt;
    String myString;
    MyChildParcelable myChildParcelable;
    ArrayList<MyChildParcelable> myParcelableList;
    ArrayList<String> myStringList;

    public static final Creator<MyParcelable> CREATOR = Postman.getCreator(MyParcelable.class);

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Postman.writeToParcel(this, dest);
    }
}
```

That's all you need to do get Postman working!

## How to Use Postman

Add the following lines to your `build.gradle` file, replacing `$postman_version` with latest version from Maven Central.

```gradle
repositories {
    mavenCentral()
}

dependencies {
    compile "com.workday:postman:$postman_version"
    compile "com.workday:postman-processor:$postman_version"
}
```

Note that if you use the [android-apt plugin](https://bitbucket.org/hvisser/android-apt) or the [kotlin-android plugin](https://kotlinlang.org/docs/reference/using-gradle.html), you may use `apt` or `kapt` respectively instead of `compile` for `postman-processor`, e.g.

```gradle
apt "com.workday:postman-processor:$postman_version"
```

### Postman Idioms

The previous example demonstrates the most common way you will use Postman: annotate the class in question with `@Parceled`. Postman will pick up all member fields in the class and write them to or read them from the Parcel when `Postman.writeToParcel(Object, Parcel)` or `Postman.readFromParcel(Class, Parcel)` is called.

Unfortunately, there is still some boiler plate code you have to deal with. In your parcelable class, you must include the following lines:

```java
public static final Creator<MyParcelable> CREATOR = Postman.getCreator(MyParcelable.class);

@Override
public int describeContents() {
  return 0;
}

@Override
public void writeToParcel(Parcel dest, int flags) {
  Postman.writeToParcel(this, dest);
}
```

replacing MyParcelable with your own class name. We hope you agree though that this is much less painful than writing the full implementations yourself.

However, to make this part easier, we have included a [live template](https://www.jetbrains.com/idea/help/live-templates.html) that you can use in Android Studio. Include [AndroidParcelable.xml](./AndroidParcelable.xml) in the following directory depending on your operating system.

- Windows: `<your home directory>\.AndroidStudio<version number>\config\templates`
- Linux: `~/.AndroidStudio<version number>/config/templates`
- OS X: `~/Library/Preferences/AndroidStudio<version number>/templates`

Restart Android Studio. Then in your Parcelable class, type `postman` on an empty line and hit enter. Android Studio will write the block for you (sans imports).

#### What if there's a field I don't want to include in the Parcel?

In that case, annotate the offending field with `@NotParceled`.

#### What if I don't want to include most of the fields in the Parcel?

No problem. In that case, instead of annotating the class with `@Parceled`, annotate the fields you want included with `@Parceled`. All other fields will be ignored.

### Things to Watch Out For

* Postman cannot access private fields. Any field you want to include in the Parcel must be either package-private (aka default) or public. If you annotate a private field with `@Parceled`, Postman will generate a compilation error. A private field in a class annotated with `@Parceled` will generate a compiler warning.
* Postman cannot populate final fields. If you annotate a final field with `@Parceled`, Postman will generate a compilation error. A final field in a class annotated with `@Parceled` will generate a compiler warning.
* Postman requires a public or package-private, no arg constructor in order to instantiate the class. A class marked with `@Parceled` that does not have default constructor will generate a compilation error.

### Types Postman Can Handle

Postman can write to the Parcel any of the following types.

* All primitives (e.g. `int`, `boolean`) as well as their boxed counterparts (e.g. [Integer](http://developer.android.com/reference/java/lang/Integer.html), [Boolean](http://developer.android.com/reference/java/lang/Boolean.html)).
* [BigDecimal](http://developer.android.com/reference/java/math/BigDecimal.html) and [BigInteger](http://developer.android.com/reference/java/math/BigInteger.html)
* Any class that implements Parcelable.
* String and [CharSequence](http://developer.android.com/reference/java/lang/CharSequence.html)
* Any class that implements [Serializable](http://developer.android.com/reference/java/io/Serializable.html)
* The following Collections, so long as they only contain items enumerated in this list (this includes `List<List<String>>`)
    * [ArrayList](http://developer.android.com/reference/java/util/ArrayList.html), [LinkedList](http://developer.android.com/reference/java/util/LinkedList.html)
    * [HashSet](http://developer.android.com/reference/java/util/HashSet.html), [LinkedHashSet](http://developer.android.com/reference/java/util/LinkedHashSet.html), [TreeSet](http://developer.android.com/reference/java/util/TreeSet.html)
* The following Maps so long as they only contain keys and values enumerated in this list (this include `Map<String, List<String>>`)
    * [HashMap](http://developer.android.com/reference/java/util/HashMap.html), [LinkedHashMap](http://developer.android.com/reference/java/util/LinkedHashMap.html)

Note that Postman will compile if it encounters a `List<Object>` or a `Map<String, Object>`, but if one of the items is not of a type that Postman can handle, it will throw a runtime exception when it tries to write the object to the parcel.

### Working with Proguard

If you're using [ProGuard](http://proguard.sourceforge.net/), you must add the following line to your ProGuard rules

```
-keep class * implements com.workday.postman.parceler.Parceler
-dontwarn com.workday.postman.codegen.*
```

otherwise your app will blow up.

Postman also doesn't work with obfuscation, so you if you have obfuscation turned on, you'll need to keep all of your parceled classes.
