package org.example.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mainservice.controller.ResidenceNewsController;
import org.example.mainservice.model.reqest.ResidenceNewsRequest;
import org.example.mainservice.model.response.ResidenceNewsResponse;
import org.example.mainservice.service.ResidenceNewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResidenceNewsControllerTest {

    @Mock
    private ResidenceNewsService residenceNewsService;

    @InjectMocks
    private ResidenceNewsController residenceNewsController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(residenceNewsController).build();
    }

    @Test
    void shouldCreateResidenceNewsSuccessfully() throws Exception {
        // Arrange
        ResidenceNewsRequest newsRequest = createResidenceNewsRequest("Breaking News", "Some details about the news");
        ResidenceNewsResponse expectedResponse = createResidenceNewsResponse(UUID.randomUUID(), "Breaking News", "Some details about the news");

        when(residenceNewsService.createResidenceNews(newsRequest)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/residence-news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(newsRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Breaking News"))
                .andExpect(jsonPath("$.content").value("Some details about the news"));

        verify(residenceNewsService, times(1)).createResidenceNews(newsRequest);
    }

    @Test
    void shouldReturnResidenceNewsByIdSuccessfully() throws Exception {
        // Arrange
        UUID newsId = UUID.randomUUID();
        ResidenceNewsResponse expectedResponse = createResidenceNewsResponse(newsId, "Breaking News", "Some details about the news");

        when(residenceNewsService.getResidenceNewsById(newsId)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/residence-news/{id}", newsId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Breaking News"))
                .andExpect(jsonPath("$.content").value("Some details about the news"));

        verify(residenceNewsService, times(1)).getResidenceNewsById(newsId);
    }

    @Test
    void shouldReturnAllResidenceNewsSuccessfully() throws Exception {
        // Arrange
        ResidenceNewsResponse news1 = createResidenceNewsResponse(UUID.randomUUID(), "Breaking News", "Some details about the news");
        ResidenceNewsResponse news2 = createResidenceNewsResponse(UUID.randomUUID(), "New Update", "Details of the update");

        when(residenceNewsService.getAllResidenceNews(any(UUID.class))).thenReturn(List.of(news1, news2));

        // Act & Assert
        mockMvc.perform(get("/api/residence-news/all/{residenceId}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Breaking News"))
                .andExpect(jsonPath("$[1].title").value("New Update"));

        // Verify
        verify(residenceNewsService, times(1)).getAllResidenceNews(any(UUID.class));
    }

    @Test
    void shouldUpdateResidenceNewsSuccessfully() throws Exception {
        // Arrange
        UUID newsId = UUID.randomUUID();
        ResidenceNewsRequest updatedNewsRequest = createResidenceNewsRequest("Updated News", "Updated content");
        ResidenceNewsResponse updatedResponse = createResidenceNewsResponse(newsId, "Updated News", "Updated content");

        when(residenceNewsService.updateResidenceNews(newsId, updatedNewsRequest)).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/residence-news/{id}", newsId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(updatedNewsRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated News"))
                .andExpect(jsonPath("$.content").value("Updated content"));

        verify(residenceNewsService, times(1)).updateResidenceNews(newsId, updatedNewsRequest);
    }

    @Test
    void shouldDeleteResidenceNewsSuccessfully() throws Exception {
        // Arrange
        UUID newsId = UUID.randomUUID();
        doNothing().when(residenceNewsService).deleteResidenceNews(newsId);

        // Act & Assert
        mockMvc.perform(delete("/api/residence-news/{id}", newsId))
                .andExpect(status().isNoContent());

        verify(residenceNewsService, times(1)).deleteResidenceNews(newsId);
    }

    private ResidenceNewsRequest createResidenceNewsRequest(String title, String content) {
        return ResidenceNewsRequest.builder()
                .title(title)
                .content(content)
                .build();
    }

    private ResidenceNewsResponse createResidenceNewsResponse(UUID id, String title, String content) {
        return ResidenceNewsResponse.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
    }

    private String convertObjectToJson(Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }
}

