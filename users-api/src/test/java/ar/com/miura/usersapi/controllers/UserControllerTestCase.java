package ar.com.miura.usersapi.controllers;

import ar.com.miura.usersapi.controller.UserController;
import ar.com.miura.usersapi.dto.UserDto;
import ar.com.miura.usersapi.service.UserService;
import ar.com.miura.usersapi.utils.CustomFileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTestCase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldListAll() throws Exception {
        PageRequest pageRequest = PageRequest.of(
                0,
                10,
                Sort.by("id").descending());
        UserDto dto = new UserDto(
                "id",
                "username",
                "fullName",
                new HashSet(),
                "address@gmail.com",
                "status"
        );
        var dtos = new ArrayList();
        dtos.add(dto);
        Mockito.when(userService.findAll(pageRequest)).thenReturn(dtos);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/v1/users")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().string(CustomFileUtils.readContent("/json/response_unit/get_all.json")))
            .andReturn();
    }


}
