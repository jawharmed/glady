package com.glady.challenge.service;


import com.glady.challenge.service.common.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ErrorMessageTest {

    @Test
    void shouldReturnIllegalStateException(){
        assertThrows(IllegalStateException.class, ErrorMessage::new);
    }
}
