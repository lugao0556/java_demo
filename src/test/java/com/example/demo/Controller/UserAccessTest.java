package com.example.demo.Controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.UserEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAccessTest {
    @Autowired private MockMvc mockMvc;

    @Test
    public void  testAddUserWithoutHeaders() throws  Exception {
        String userId = "liwei";
        List<String> rs = Arrays.asList("r1", "r2", "r3");
        UserEndpoints userEndpoints = new UserEndpoints(userId, rs);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userEndpoints);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void  testAddUserNotAdmin() throws  Exception {
        String userId = "liwei";
        List<String> rs = Arrays.asList("r1", "r2", "r3");
        UserEndpoints userEndpoints = new UserEndpoints(userId, rs);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userEndpoints);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("userId", "njh")
                        .header("accountName", "ddsa")
                        .header("role", "user"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("not a admin user"));
    }



    @Test
    public void  testAddUser() throws  Exception {
        String userId = "liwei";
        List<String> rs = Arrays.asList("r1", "r2", "r3");
        UserEndpoints userEndpoints = new UserEndpoints(userId, rs);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userEndpoints);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("userId", "njh")
                        .header("accountName", "ddsa")
                        .header("role", "admin"))
                .andExpect(status().isCreated())
                .andExpect(content().string("created"));
    }


    @Test
    public void testUserAccessWithoutHeaders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/resourceA"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserAccessWithHeader() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/resourceA")
                .header("userId", "123456")
                .header("accountName", "ddsa")
                .header("role", "user"))
                .andExpect(status().isOk())
                .andExpect(content().string("succuss"));

    }
}
