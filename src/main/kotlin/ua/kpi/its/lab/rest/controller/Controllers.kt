package ua.kpi.its.lab.rest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.rest.dto.SoftwareModuleRequest
import ua.kpi.its.lab.rest.dto.SoftwareModuleResponse
import ua.kpi.its.lab.rest.dto.SoftwareProductsRequest
import ua.kpi.its.lab.rest.dto.SoftwareProductsResponse
import ua.kpi.its.lab.rest.svc.SoftwareModuleService
import ua.kpi.its.lab.rest.svc.SoftwareProductService

@RestController
@RequestMapping("/software-products")
class SoftwareProductController @Autowired constructor(
    private val softwareProductService: SoftwareProductService
) {
    /**
     * Gets the list of all software products
     *
     * @return: List of SoftwareProductsResponse
     */
    @GetMapping(path = ["", "/"])
    fun softwareProducts(): List<SoftwareProductsResponse> = softwareProductService.read()

    /**
     * Reads the software product by its id
     *
     * @param id: id of the software product
     * @return: SoftwareProductsResponse for the given id
     */
    @GetMapping("{id}")
    fun readSoftwareProduct(@PathVariable("id") id: Long): ResponseEntity<SoftwareProductsResponse> {
        return wrapNotFound { softwareProductService.readById(id) }
    }

    /**
     * Creates a new software product instance
     *
     * @param softwareProduct: SoftwareProductsRequest with set properties
     * @return: SoftwareProductsResponse for the created software product
     */
    @PostMapping(path = ["", "/"])
    fun createSoftwareProduct(@RequestBody softwareProduct: SoftwareProductsRequest): SoftwareProductsResponse {
        return softwareProductService.create(softwareProduct)
    }

    /**
     * Updates existing software product instance
     *
     * @param id: id of the software product to update
     * @param softwareProduct: SoftwareProductsRequest with properties set
     * @return: SoftwareProductsResponse of the updated software product
     */
    @PutMapping("{id}")
    fun updateSoftwareProduct(
        @PathVariable("id") id: Long,
        @RequestBody softwareProduct: SoftwareProductsRequest
    ): ResponseEntity<SoftwareProductsResponse> {
        return wrapNotFound { softwareProductService.updateById(id, softwareProduct) }
    }

    /**
     * Deletes existing software product instance
     *
     * @param id: id of the software product to delete
     * @return: SoftwareProductsResponse of the deleted software product
     */
    @DeleteMapping("{id}")
    fun deleteSoftwareProduct(@PathVariable("id") id: Long): ResponseEntity<SoftwareProductsResponse> {
        return wrapNotFound { softwareProductService.deleteById(id) }
    }

    fun <T> wrapNotFound(call: () -> T): ResponseEntity<T> {
        return try {
            // call function for result
            val result = call()
            // return "ok" response with result body
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            // catch not-found exception
            // return "404 not-found" response
            ResponseEntity.notFound().build()
        }
    }
}

@RestController
@RequestMapping("/software-modules")
class SoftwareModuleController @Autowired constructor(
    private val softwareModuleService: SoftwareModuleService
) {
    /**
     * Gets the list of all software modules
     *
     * @return: List of SoftwareModuleResponse
     */
    @GetMapping(path = ["", "/"])
    fun softwareModules(): List<SoftwareModuleResponse> = softwareModuleService.read()

    /**
     * Reads the software module by its id
     *
     * @param id: id of the software module
     * @return: SoftwareModuleResponse for the given id
     */
    @GetMapping("{id}")
    fun readSoftwareModule(@PathVariable("id") id: Long): ResponseEntity<SoftwareModuleResponse> {
        return wrapNotFound { softwareModuleService.readById(id) }
    }

    /**
     * Creates a new software module instance
     *
     * @param softwareModule: SoftwareModuleRequest with set properties
     * @return: SoftwareModuleResponse for the created software module
     */
    @PostMapping(path = ["", "/"])
    fun createSoftwareModule(@RequestBody softwareModule: SoftwareModuleRequest): SoftwareModuleResponse {
        return softwareModuleService.create(softwareModule)
    }

    /**
     * Updates existing software module instance
     *
     * @param id: id of the software module to update
     * @param softwareModule: SoftwareModuleRequest with properties set
     * @return: SoftwareModuleResponse of the updated software module
     */
    @PutMapping("{id}")
    fun updateSoftwareModule(
        @PathVariable("id") id: Long,
        @RequestBody softwareModule: SoftwareModuleRequest
    ): ResponseEntity<SoftwareModuleResponse> {
        return wrapNotFound { softwareModuleService.updateById(id, softwareModule) }
    }

    /**
     * Deletes existing software module instance
     *
     * @param id: id of the software module to delete
     * @return: SoftwareModuleResponse of the deleted software module
     */
    @DeleteMapping("{id}")
    fun deleteSoftwareModule(@PathVariable("id") id: Long): ResponseEntity<SoftwareModuleResponse> {
        return wrapNotFound { softwareModuleService.deleteById(id) }
    }

    fun <T> wrapNotFound(call: () -> T): ResponseEntity<T> {
        return try {
            // call function for result
            val result = call()
            // return "ok" response with result body
            ResponseEntity.ok(result)
        } catch (e: IllegalArgumentException) {
            // catch not-found exception
            // return "404 not-found" response
            ResponseEntity.notFound().build()
        }
    }
}