package ar.com.miura.usersapi.controllers;

public class HealthControllersTests {

    private ar.com.miura.usersapi.controller.HealthController controller = new ar.com.miura.usersapi.controller.HealthController();

    @org.junit.jupiter.api.Test
    public void shouldGetStatus() {
        org.junit.jupiter.api.Assertions.assertEquals("ok", controller.getStatus());
    }

}
