package com.bosch.ecommerce.product.web;

import com.bosch.ecommerce.controller.ProductController;
import com.bosch.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void list_ok() throws Exception {
        Mockito.when(productService.findAll(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new com.bosch.ecommerce.paging.PageResponse<>(java.util.List.of(),0,10,0,0,true,true));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }
}
