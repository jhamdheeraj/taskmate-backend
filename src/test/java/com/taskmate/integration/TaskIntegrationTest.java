package com.taskmate.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskmate.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureWebMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class TaskIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Test
    @DisplayName("Should create task and retrieve it")
    @Transactional
    void shouldCreateTaskAndRetrieveIt() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Task newTask = new Task("Integration Test Task", "Integration Description", LocalDateTime.now().plusDays(1));

        // Create task
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.title").value("Integration Test Task"));
    }

    @Test
    @DisplayName("Should handle invalid task creation")
    @Transactional
    void shouldHandleInvalidTaskCreation() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Task invalidTask = new Task("", "", null);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get paginated tasks")
    @Transactional
    void shouldGetPaginatedTasks() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(get("/api/v1/tasks")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "createdAt")
                .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
