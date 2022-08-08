package com.felipeveiga.fvcatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipeveiga.fvcatalog.entities.dto.ProductDTO;
import com.felipeveiga.fvcatalog.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceTestIT {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	
	private Long existindId;
	private Long nonExistingId;
	private Long countTotalProducts;
	private ProductDTO productDto;
	
	@BeforeEach
	void setUp() throws Exception {
		existindId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		productDto = Factory.createProductDTO();
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception{
		
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		
		ResultActions result = mockMvc.perform(get("/products", pageRequest)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String jsonBody = mapper.writeValueAsString(productDto);
		
		String expectedName = productDto.getName();
		String expectedDescription = productDto.getDescription();
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existindId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existindId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));
		
	}
	
	@Test
	public void updateShouldThrowObjectNotFoundExceptionWhenIdNotExists() throws Exception {
		
		String jsonBody = mapper.writeValueAsString(productDto);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
}
