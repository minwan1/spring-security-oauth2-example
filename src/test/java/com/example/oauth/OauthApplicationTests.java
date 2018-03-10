package com.example.oauth;

import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OauthApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    private String CONTENT_TYPE = "application/json;charset=UTF-8";
    private String SCOPE = "read";
    private String CLIENT_ID = "foo";
    private String CLIENT_SECRET = "bar";
    private String SECURITY_USERNAME = "user";
    private String SECURITY_PASSWORD = "test";

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void when_callApi_expect_unauthorized() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isUnauthorized());
    }

    @Test
    public void when_callUsers_expect_success() throws Exception {
        String accessToken =obtainAccessToken(SECURITY_USERNAME,SECURITY_PASSWORD);
        mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + accessToken)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));
    }

    @Test
    public void when_ckeck2User_expect_success() throws Exception{

        String accessToken =obtainAccessToken(SECURITY_USERNAME, SECURITY_PASSWORD);

        mockMvc.perform(get("/users/2")
                .header("Authorization", "Bearer " + accessToken)
                .accept(CONTENT_TYPE))
                .andExpect(jsonPath("$.username", is("test2")));
    }

    @Test
    public void when_callGetPost_expect_success() throws Exception {

        JSONObject user = setUser();
        String accessToken =obtainAccessToken(SECURITY_USERNAME, SECURITY_PASSWORD);

        createUser(user, accessToken);
        verifyUser(accessToken);
    }

    private void verifyUser(String accessToken) throws Exception {
        mockMvc.perform(get("/users/1")
                .header("Authorization", "Bearer " + accessToken)
                .accept(CONTENT_TYPE))
                .andExpect(jsonPath("$.username", is("test")));
    }

    private void createUser(JSONObject user, String accessToken) throws Exception {
        mockMvc.perform(post("/users")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(CONTENT_TYPE)
                .content(user.toString())
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated());
    }

    private JSONObject setUser() throws JSONException {
        JSONObject user = new JSONObject();
        user.put("username","test");
        user.put("id","1");
        return user;
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);
        params.add("scope", SCOPE);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

}
