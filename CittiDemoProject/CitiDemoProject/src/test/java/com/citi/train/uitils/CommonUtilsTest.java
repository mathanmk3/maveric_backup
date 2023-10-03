package com.citi.train.uitils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.citi.train.serviceimpl.CancelDetailServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ContextConfiguration(classes = {CommonUtils.class})
@ExtendWith(SpringExtension.class)
class CommonUtilsTest {

    @Test
    void testGetMapper() throws InterruptedException {
        String mapFrom = "MapValue";
        Class<String> mapTo = String.class;
        String result = CommonUtils.getMapper(mapFrom, mapTo);
        assertEquals(mapFrom, result);
    }
    @Test
    void testCheckNullable() {

        assertFalse(CommonUtils.checkNullable("value"));
    }


    @Test
    void testGetMapper3() {

        assertNull(CommonUtils.getMapper("MapValue", null));
    }
}

