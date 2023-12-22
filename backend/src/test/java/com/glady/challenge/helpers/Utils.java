package com.glady.challenge.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

    public static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
