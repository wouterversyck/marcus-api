package be.wouterversyck.shoppinglistapi.admin.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    private AdminController adminController = new AdminController();

    @Test
    public void placeholder() {
        assertEquals(adminController.test(), "test");
        assertEquals(adminController.test2(), "test2");
    }
}