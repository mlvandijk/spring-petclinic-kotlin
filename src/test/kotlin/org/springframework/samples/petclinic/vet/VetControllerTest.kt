package org.springframework.samples.petclinic.vet

import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Test class for the [VetController]
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(VetController::class)
class VetControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var vets: VetRepository

    @BeforeEach
    fun setup() {
        val james = Vet()
        james.firstName = "James"
        james.lastName = "Carter"
        james.id = 1
        val helen = Vet()
        helen.firstName = "Helen"
        helen.lastName = "Leary"
        helen.id = 2
        val radiology = Specialty()
        radiology.id = 1
        radiology.name = "radiology"
        helen.addSpecialty(radiology)
        given(this.vets.findAll()).willReturn(listOf(james, helen))
    }

    @Test
    fun testShowVetListHtml() {
        mockMvc.perform(get("/vets.html"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("vets"))
                .andExpect(view().name("vets/vetList"))
    }


    @Test
    fun testVetsEndpointPagination() {
        val page = 2
        mockMvc.perform(get("/vets.html").param("page", page.toString()))
            .andExpect(status().isOk)
            .andExpect(view().name("vets/vetList"))
            .andExpect(model().attributeExists("currentPage"))
            .andExpect(model().attribute("currentPage", CoreMatchers.`is`(page)))
    }

}
