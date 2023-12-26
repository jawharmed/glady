 package com.glady.challenge.controller;

 import com.glady.challenge.helpers.DtoObjectHelper;
 import com.glady.challenge.service.gladyuser.GladyUserService;
 import com.glady.challenge.web.dto.user.GladyUserDTO;
 import com.glady.challenge.web.dto.user.GladyUserInfoDTO;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
 import org.springframework.boot.test.mock.mockito.MockBean;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.PageImpl;
 import org.springframework.http.MediaType;
 import org.springframework.test.web.servlet.MockMvc;
 import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

 import java.util.Arrays;
 import java.util.List;

 import static com.glady.challenge.helpers.Utils.asJsonString;
 import static org.mockito.Mockito.doNothing;
 import static org.mockito.Mockito.times;
 import static org.mockito.Mockito.verify;
 import static org.mockito.Mockito.when;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 @WebMvcTest(GladyUserController.class)
class GladyUserControllerTest {

     @MockBean
     private GladyUserService gladyUserService;

     @Autowired
     private MockMvc mockMvc;


     @Test
     void testGetAll2() throws Exception {
         int page = 0;
         int size = 10;
         String sortOrder = "asc";

         List<GladyUserDTO> gladyUserDTOList = Arrays.asList(
                 GladyUserDTO.builder().id(1L).username("Jean").companyId(1L).build(),
                 GladyUserDTO.builder().id(2L).username("Jeanne").companyId(2L).build());

         Page<GladyUserDTO> gladyUserPage = new PageImpl<>(gladyUserDTOList);

         when(gladyUserService.getAll(page, size, sortOrder)).thenReturn(gladyUserPage);

         mockMvc.perform(MockMvcRequestBuilders.get("/glady-user")
                         .param("page", String.valueOf(page))
                         .param("size", String.valueOf(size))
                         .param("sortOrder", sortOrder))
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$").isMap())
                 .andReturn();

         verify(gladyUserService, times(1)).getAll(page, size, sortOrder);
     }

     @Test
     void testGetById() throws Exception {
         long userId = 1L;
         when(gladyUserService.getDtoById(userId)).thenReturn(new GladyUserDTO());

         mockMvc.perform(MockMvcRequestBuilders.get("/glady-user/{id}", userId))
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$").isMap());

         verify(gladyUserService, times(1)).getDtoById(userId);
     }

     @Test
     void testGetGladyUserInfo() throws Exception {
         long userId = 1L;
         when(gladyUserService.getGladyUserInfo(userId)).thenReturn(new GladyUserInfoDTO());

         mockMvc.perform(MockMvcRequestBuilders.get("/glady-user/{id}/info", userId))
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$").isMap());

         verify(gladyUserService, times(1)).getGladyUserInfo(userId);
     }

     @Test
     void testCreate() throws Exception {
         GladyUserDTO gladyUserDTO = DtoObjectHelper.getGladyUserDTO();
         when(gladyUserService.create(gladyUserDTO)).thenReturn(gladyUserDTO);

         mockMvc.perform(MockMvcRequestBuilders.post("/glady-user")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(asJsonString(gladyUserDTO)))
                 .andExpect(status().isCreated())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$").isMap());

         // Verify that the gladyUserService.create method was called
         verify(gladyUserService).create(gladyUserDTO);
     }

     @Test
     void testUpdate() throws Exception {
         GladyUserDTO gladyUserDTO = DtoObjectHelper.getGladyUserDTO();
         gladyUserDTO.setId(1L);
         when(gladyUserService.update(gladyUserDTO)).thenReturn(gladyUserDTO);

         mockMvc.perform(MockMvcRequestBuilders.put("/glady-user")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(asJsonString(gladyUserDTO)))
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$").isMap());

         verify(gladyUserService, times(1)).update(gladyUserDTO);
     }

     @Test
     void testDelete() throws Exception {
         long userId = 1L;
         doNothing().when(gladyUserService).delete(userId);

         mockMvc.perform(MockMvcRequestBuilders.delete("/glady-user/{id}", userId))
                 .andExpect(status().isOk());

         verify(gladyUserService, times(1)).delete(userId);
     }

 }
