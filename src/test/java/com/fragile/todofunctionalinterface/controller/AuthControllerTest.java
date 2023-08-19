package com.fragile.todofunctionalinterface.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fragile.todofunctionalinterface.repository.UserRepository;
import com.fragile.todofunctionalinterface.service.CustomUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CustomUserServiceImpl customUserServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("testdb")
            .withUsername("sa")
            .withPassword("sa");


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        // Clear the database before each test
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUserHandler_Success() throws Exception {
        // Prepare your mock behaviors (if needed)
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // Create a JSON request body for the User entity
        String requestBody = "{"
                + "\"firstName\": \"Test\","
                + "\"lastName\": \"User\","
                + "\"email\": \"testuser@gmail.com\","
                + "\"password\": \"testpass\""
                + "}";

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User created successfully."));

    }


    @Test
    public void testLogin_Success() throws Exception {
        // Prepare your mock behaviors
        UserDetails userDetails = User.builder()
                .username("testuser@gmail.com")
                .password("encodedPassword") // Make sure this matches your password encoding
                .roles("USER") // Set appropriate roles
                .build();

        when(customUserServiceImpl.loadUserByUsername(any())).thenReturn(userDetails);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"testuser@gmail.com\", \"password\": \"testpass\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User login successfully."));
    }


}
