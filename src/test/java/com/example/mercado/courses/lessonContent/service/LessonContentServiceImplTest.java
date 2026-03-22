package com.example.mercado.courses.lessonContent.service;

import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.dto.UpdateLessonContentRequest;
import com.example.mercado.courses.lessonContent.entity.LessonContent;
import com.example.mercado.courses.lessonContent.exception.LessonContentNotFoundException;
import com.example.mercado.courses.lessonContent.mapper.LessonContentMapper;
import com.example.mercado.courses.lessonContent.repository.LessonContentRepository;
import com.example.mercado.courses.testutils.lessonContent.LessonContentFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("LessonContentServiceImpl Test")
public class LessonContentServiceImplTest {

    @Mock
    private LessonContentMapper mapper;

    @Mock
    private LessonContentRepository repository;

    @InjectMocks
    private LessonContentServiceImpl service;

    private LessonContent lessonContent;
    private CreateLessonContentRequest createLessonContentRequest;
    private UpdateLessonContentRequest updateLessonContentRequest;
    private LessonContentResponse lessonContentResponse;

    @BeforeEach
    void setUp() {
        lessonContent = LessonContentFactory.createLessonContent();
        createLessonContentRequest = LessonContentFactory.createLessonContentRequest();
        lessonContentResponse = LessonContentFactory.createLessonContentResponse();
        updateLessonContentRequest = LessonContentFactory.updateLessonContentResponse();
    }

    /*##################### TESTING FUNCTION <<CREATE LESSON CONTENT>> #####################*/

    @Test
    @DisplayName("Func createLessonContent should return response")
    void createLessonContent_shouldReturnLessonContentResponse() {
        Long lessonId = 1L;

        Mockito.when(mapper.toEntity(lessonId, createLessonContentRequest)).thenReturn(lessonContent);
        Mockito.when(mapper.toResponse(lessonContent)).thenReturn(lessonContentResponse);
        Mockito.when(repository.save(Mockito.any(LessonContent.class))).thenAnswer(i -> i.getArgument(0));

        LessonContentResponse response = service.createLessonContent(lessonId, createLessonContentRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, lessonContentResponse);

        Mockito.verify(mapper).toEntity(Mockito.anyLong(), Mockito.any(CreateLessonContentRequest.class));
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(LessonContent.class));
    }

    @Test
    @DisplayName("Func createLessonContent should set correct position")
    void createLessonContent_shouldSetCorrectPosition() {
        Long lessonId = 1L;

        CreateLessonContentRequest request1 = createLessonContentRequest;
        CreateLessonContentRequest request2 = LessonContentFactory.createLessonContentRequest("Test 2");

        Mockito.when(mapper.toEntity(Mockito.eq(lessonId), Mockito.any(CreateLessonContentRequest.class)))
                .thenReturn(lessonContent);
        Mockito.when(mapper.toResponse(Mockito.any(LessonContent.class)))
                .thenReturn(lessonContentResponse);
        Mockito.when(repository.save(Mockito.any(LessonContent.class)))
                .thenAnswer(i -> i.getArgument(0));

        Mockito.when(repository.countByLessonId(lessonId)).thenReturn(0);
        LessonContentResponse response1 = service.createLessonContent(lessonId, request1);

        Mockito.when(repository.countByLessonId(lessonId)).thenReturn(1);
        LessonContentResponse response2 = service.createLessonContent(lessonId, request2);

        Assertions.assertNotNull(response1);
        Assertions.assertNotNull(response2);

        Assertions.assertEquals(1, response1.position());
        Assertions.assertEquals(1, response2.position());

        Mockito.verify(mapper, Mockito.times(2))
                .toEntity(Mockito.anyLong(), Mockito.any(CreateLessonContentRequest.class));
        Mockito.verify(repository, Mockito.times(2))
                .save(Mockito.any(LessonContent.class));
    }

    /*##################################################################################*/



    /*##################### TESTING FUNCTION <<UPDATE LESSON CONTENT>> #####################*/

    @Test
    @DisplayName("Func updateLessonContent should return response")
    void updateLessonContent_shouldReturnLessonContentResponse() {
        Long lessonId = 1L;
        Long lessonContentId = 1L;

        CreateLessonContentRequest request = createLessonContentRequest;
        LessonContent existing = lessonContent;

        Mockito.when(repository.findByLessonIdAndId(lessonId, lessonContentId))
                .thenReturn(Optional.of(existing));
        Mockito.when(mapper.toResponse(existing))
                .thenReturn(lessonContentResponse);

        LessonContentResponse response = service.updateLessonContent(lessonId, lessonContentId, updateLessonContentRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, lessonContentResponse);

        Mockito.verify(repository).findByLessonIdAndId(lessonId, lessonContentId);
        Mockito.verify(mapper).toResponse(Mockito.any(LessonContent.class));
    }

    @Test
    @DisplayName("Func updateLessonContent should return exception if LessonContent doesn't exist")
    void updateLessonContent_shouldThrowException_ifLessonContentDoesntExist() {
        Long lessonId = 1L;
        Long lessonContentId = 1L;

        Mockito.when(repository.findByLessonIdAndId(lessonId, lessonContentId))
                .thenThrow(new LessonContentNotFoundException(lessonContentId, lessonId));

        Assertions.assertThrows(
                LessonContentNotFoundException.class,
                () -> service.updateLessonContent(lessonId, lessonContentId, updateLessonContentRequest)
        );

        Mockito.verify(repository).findByLessonIdAndId(lessonId, lessonContentId);
        Mockito.verify(mapper, Mockito.never()).toResponse(Mockito.any());
    }

    /*##################################################################################*/



    /*##################### TESTING FUNCTION <<GET LESSON CONTENT BY ID>> #####################*/

    @Test
    @DisplayName("Func getLessonContentById should return response")
    void getLessonContentById_shouldReturnResponse() {
        Long lessonId = 1L;
        Long lessonContentId = 1L;
        LessonContent existing = lessonContent;

        Mockito.when(repository.findByLessonIdAndId(lessonId, lessonContentId))
                .thenReturn(Optional.of(existing));
        Mockito.when(mapper.toResponse(existing))
                .thenReturn(lessonContentResponse);

        LessonContentResponse response = service.getLessonContentById(lessonId, lessonContentId);

        Assertions.assertNotNull(response);

        Mockito.verify(repository).findByLessonIdAndId(lessonId, lessonContentId);
        Mockito.verify(mapper).toResponse(existing);
    }

    @Test
    @DisplayName("Func getLessonContentById should throw exception if LessonContent doesn't exist")
    void getLessonContentById_shouldThrowException_ifLessonContentDoesntExist() {
        Long lessonId = 1L;
        Long lessonContentId = 1L;
        LessonContent existing = lessonContent;

        Mockito.when(repository.findByLessonIdAndId(lessonId, lessonContentId))
                .thenThrow(new LessonContentNotFoundException(lessonContentId, lessonId));

        Assertions.assertThrows(
                LessonContentNotFoundException.class,
                () -> service.getLessonContentById(lessonId, lessonContentId)
        );

        Mockito.verify(repository).findByLessonIdAndId(lessonId, lessonContentId);
        Mockito.verify(mapper, Mockito.never()).toResponse(existing);
    }

    /*#########################################################################################*/



    /*##################### TESTING FUNCTION <<DELETE LESSON CONTENT>> #####################*/

    @Test
    @DisplayName("Func deleteLessonContent should delete LessonContent")
    void deleteLessonContent_shouldDeleteLessonContent() {
        Long lessonId = 1L;
        Long lessonContentId = 1L;
        LessonContent existing = lessonContent;

        Mockito.when(repository.findByLessonIdAndId(lessonId, lessonContentId))
                .thenReturn(Optional.of(existing));

        service.deleteLessonContent(lessonId, lessonContentId);

        Mockito.verify(repository).findByLessonIdAndId(lessonId, lessonContentId);
        Mockito.verify(repository).delete(existing);
    }

    @Test
    @DisplayName("Func deleteLessonContent should throw exception if LessonContent doesn't exist")
    void deleteLessonContent_shouldThrowException_ifLessonContentDoesntExist() {
        Long lessonId = 1L;
        Long lessonContentId = 1L;
        LessonContent existing = lessonContent;

        Mockito.when(repository.findByLessonIdAndId(lessonId, lessonContentId))
                .thenThrow(new LessonContentNotFoundException(lessonContentId, lessonId));

        Assertions.assertThrows(
                LessonContentNotFoundException.class,
                () -> service.deleteLessonContent(lessonId, lessonContentId)
        );

        Mockito.verify(repository).findByLessonIdAndId(lessonId, lessonContentId);
        Mockito.verify(repository, Mockito.never()).delete(existing);
    }

    /*######################################################################################*/



    /*##################### TESTING FUNCTION <<GET ALL LESSON CONTENT>> #####################*/

    @Test
    @DisplayName("Func getAllLessonContents should return list of LessonContents")
    void getAllLessonContent_shouldReturnListOfLessonContent() {
        Long lessonId = 1L;

        LessonContent existing1 = lessonContent;
        LessonContent existing2 = LessonContentFactory.createLessonContent("Test 2");

        LessonContentResponse response1 = lessonContentResponse;
        LessonContentResponse response2 = LessonContentFactory.createLessonContentResponse(existing2.getName());

        List<LessonContent> contents = List.of(existing1, existing2);

        Mockito.when(repository.findAllByLessonIdOrderByPositionAsc(lessonId))
                .thenReturn(contents);

        Mockito.when(mapper.toResponse(existing1)).thenReturn(lessonContentResponse);
        Mockito.when(mapper.toResponse(existing2)).thenReturn(lessonContentResponse);

        List<LessonContentResponse> result = service.getAllLessonContents(lessonId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        Mockito.verify(repository).findAllByLessonIdOrderByPositionAsc(lessonId);
        Mockito.verify(mapper).toResponse(existing1);
        Mockito.verify(mapper).toResponse(existing2);
    }

    /*#######################################################################################*/

}
