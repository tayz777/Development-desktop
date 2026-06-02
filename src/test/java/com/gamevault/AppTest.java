package com.gamevault;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AppTest {

    @Test
    void applicationClassIsLoadable() {
        assertDoesNotThrow(() -> Class.forName("com.gamevault.App"));
    }
}
