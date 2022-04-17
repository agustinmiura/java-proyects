package ar.com.miura.usersapi.controllers;

import ar.com.miura.usersapi.controller.HealthController;
import ar.com.miura.usersapi.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTestCase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldListAll() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/v1/users")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andReturn();
        Assertions.assertEquals("ok", result.getResponse().getContentAsString());
    }
}
