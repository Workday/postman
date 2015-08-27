/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.demo;

import com.workday.postman.adapter.AdapterPackage;
import com.workday.postman.adapter.BigDecimalParcelableAdapter;
import com.workday.postman.adapter.BigIntegerParcelableAdapter;
import com.workday.postman.adapter.BooleanParcelableAdapter;
import com.workday.postman.adapter.ByteParcelableAdapter;
import com.workday.postman.adapter.CharParcelableAdapter;
import com.workday.postman.adapter.DoubleParcelableAdapter;
import com.workday.postman.adapter.FloatParcelableAdapter;
import com.workday.postman.adapter.IntParcelableAdapter;
import com.workday.postman.adapter.LongParcelableAdapter;
import com.workday.postman.adapter.ShortParcelableAdapter;
import com.workday.postman.adapter.StringParcelableAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

/**
 * @author nathan.taylor
 * @since 2015-08-26.
 */
@RunWith(RobolectricTestRunner.class)
public class ParcelableAdapterTest {

    @Test
    public void testAsParcelable() {
        assertTrue(AdapterPackage.asParcelable(BigDecimal.TEN) instanceof BigDecimalParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable(BigInteger.TEN) instanceof BigIntegerParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable((byte) 5) instanceof ByteParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable(true) instanceof BooleanParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable('c') instanceof CharParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable(2.0) instanceof DoubleParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable(3.0f) instanceof FloatParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable(3) instanceof IntParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable(3L) instanceof LongParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable((short) 3) instanceof ShortParcelableAdapter);
        assertTrue(AdapterPackage.asParcelable("r2-d2") instanceof StringParcelableAdapter);
    }
}
