package com.hubertart.crud;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsersRepository repository;

    @Test
    @Transactional
    @Rollback
    public void testGetAllUsers() throws Exception {
        Users user1 = new Users();
        user1.setEmail("test1@email.com");
        user1.setPassword("testPassword1");
        repository.save(user1);
        Users user2 = new Users();
        user2.setEmail("test2@email.com");
        user2.setPassword("testPassword2");
        repository.save(user2);

        MockHttpServletRequestBuilder request = get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
//                .andExpect(content().string(""));
                .andExpect(jsonPath("$[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$[0].email", is("test1@email.com")))
                .andExpect(jsonPath("$[1].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$[1].email", is("test2@email.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void testPostAUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> newUser = new HashMap<String, Object>(){
            {
                put("email", "test3@email.com");
                put("password", "testPassword3");
            }
        };
        String json = objectMapper.writeValueAsString(newUser);

        MockHttpServletRequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.email", is("test3@email.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetUserById() throws Exception {
        Users user1 = new Users();
        user1.setEmail("test1@email.com");
        user1.setPassword("testPassword1");
        repository.save(user1);
        Users user2 = new Users();
        user2.setEmail("test2@email.com");
        user2.setPassword("testPassword2");
        repository.save(user2);

        MockHttpServletRequestBuilder request = get("/users/" + user1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$.email", is("test1@email.com")));
    }
    @Test
    @Transactional
    @Rollback
    public void testPatchUserByIdOnlyEmail() throws Exception {
        Users user1 = new Users();
        user1.setEmail("test1@email.com");
        user1.setPassword("testPassword1");
        repository.save(user1);

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> newUser = new HashMap<String, Object>(){
            {
                put("email", "newEmail@email.com");
            }
        };
        String json = objectMapper.writeValueAsString(newUser);

        MockHttpServletRequestBuilder request = patch("/users/" + user1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.email", is("newEmail@email.com")));
    }
    @Test
    @Transactional
    @Rollback
    public void testPatchUserByIdForEmailAndPassword() throws Exception {
        Users user1 = new Users();
        user1.setEmail("test1@email.com");
        user1.setPassword("testPassword1");
        repository.save(user1);

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> newUser = new HashMap<String, Object>(){
            {
                put("email", "newEmail@email.com");
                put("password", "newPassword");
            }
        };
        String json = objectMapper.writeValueAsString(newUser);

        MockHttpServletRequestBuilder request = patch("/users/" + user1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.email", is("newEmail@email.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteById() throws Exception {
        Users user1 = new Users();
        user1.setEmail("test1@email.com");
        user1.setPassword("testPassword1");
        repository.save(user1);
        Users user2 = new Users();
        user2.setEmail("test2@email.com");
        user2.setPassword("testPassword2");
        repository.save(user2);

        MockHttpServletRequestBuilder request = delete("/users/" + user1.getId())
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count",is(1)));
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticationTrue() throws Exception {
        Users user1 = new Users();
        user1.setEmail("test1@email.com");
        user1.setPassword("testPassword1");
        repository.save(user1);

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> newUser = new HashMap<String, Object>(){
            {
                put("email", "test1@email.com");
                put("password", "testPassword1");
            }
        };
        String json = objectMapper.writeValueAsString(newUser);

        MockHttpServletRequestBuilder request = post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(true)))
                .andExpect(jsonPath("$.user.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.user.email", is("test1@email.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticationFalse() throws Exception {
        Users user1 = new Users();
        user1.setEmail("test1@email.com");
        user1.setPassword("testPassword1");
        repository.save(user1);

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> newUser = new HashMap<String, Object>(){
            {
                put("email", "test1@email.com");
                put("password", "wrongPassword");
            }
        };
        String json = objectMapper.writeValueAsString(newUser);

        MockHttpServletRequestBuilder request = post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(false)));
    }
}
