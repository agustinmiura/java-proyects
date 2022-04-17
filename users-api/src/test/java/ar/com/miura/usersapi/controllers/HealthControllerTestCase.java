package ar.com.miura.usersapi.controllers;

import ar.com.miura.usersapi.controller.HealthController;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HealthController.class)
public class HealthControllerTestCase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetResponse() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .get("/health")
            .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(content().string("ok"))
            .andReturn();
    }

}
