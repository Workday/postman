/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter

import android.os.Parcelable
import com.workday.meta.InvalidTypeException
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Locale

/**
 * @author nathan.taylor
 * @since 2015-08-26.
 */

public fun asParcelable(o: Any?): Parcelable {
    when (o) {
        is Parcelable -> return o

        is BigDecimal -> return BigDecimalParcelableAdapter(o)
        is BigInteger -> return BigIntegerParcelableAdapter(o)
        is Boolean -> return BooleanParcelableAdapter(o)
        is Byte -> return ByteParcelableAdapter(o)
        is Char -> return CharParcelableAdapter(o)
        is Double -> return DoubleParcelableAdapter(o)
        is Float -> return FloatParcelableAdapter(o)
        is Int -> return IntParcelableAdapter(o)
        is Long -> return LongParcelableAdapter(o)
        is Short -> return ShortParcelableAdapter(o)

        is String -> return StringParcelableAdapter(o)
        is CharSequence -> return CharSequenceParcelableAdapter(o)
        else -> throw InvalidTypeException("Could not convert class of type %s to Parcelable"
                                                   .format(Locale.US, o?.javaClass))
    }
}
