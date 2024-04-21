package ua.kpi.its.lab.rest.svc

import ua.kpi.its.lab.rest.dto.SoftwareModuleRequest
import ua.kpi.its.lab.rest.dto.SoftwareModuleResponse
import ua.kpi.its.lab.rest.dto.SoftwareProductsRequest
import ua.kpi.its.lab.rest.dto.SoftwareProductsResponse

interface SoftwareProductService {
    /**
     * Creates a new SoftwareProduct record.
     *
     * @param softwareProduct: The SoftwareProduct instance to be inserted
     * @return: The recently created SoftwareProduct instance
     */
    fun create(softwareProduct: SoftwareProductsRequest): SoftwareProductsResponse

    /**
     * Reads all created SoftwareProduct records.
     *
     * @return: List of created SoftwareProduct records
     */
    fun read(): List<SoftwareProductsResponse>

    /**
     * Reads a SoftwareProduct record by its index.
     * The order is determined by the order of creation.
     *
     * @param id: The index of SoftwareProduct record
     * @return: The SoftwareProduct instance at index
     */
    fun readById(id: Long): SoftwareProductsResponse

    /**
     * Updates a SoftwareProduct record data.
     *
     * @param softwareProduct: The SoftwareProduct instance to be updated
     * @return: The updated SoftwareProduct record
     */
    fun updateById(id: Long, softwareProduct: SoftwareProductsRequest): SoftwareProductsResponse

    /**
     * Deletes a SoftwareProduct record by its index.
     *
     * @param id: The index of SoftwareProduct record to delete
     * @return: The deleted SoftwareProduct instance at index
     */
    fun deleteById(id: Long): SoftwareProductsResponse
}

interface SoftwareModuleService {
    /**
     * Creates a new SoftwareModule record.
     *
     * @param softwareModule: The SoftwareModule instance to be inserted
     * @return: The recently created SoftwareModule instance
     */
    fun create(softwareModule: SoftwareModuleRequest): SoftwareModuleResponse

    /**
     * Reads all created SoftwareModule records.
     *
     * @return: List of created SoftwareModule records
     */
    fun read(): List<SoftwareModuleResponse>

    /**
     * Reads a SoftwareModule record by its index.
     * The order is determined by the order of creation.
     *
     * @param id: The index of SoftwareModule record
     * @return: The SoftwareModule instance at index
     */
    fun readById(id: Long): SoftwareModuleResponse

    /**
     * Updates a SoftwareModule record data.
     *
     * @param softwareModule: The SoftwareModule instance to be updated
     * @return: The updated SoftwareModule record
     */
    fun updateById(id: Long, softwareModule: SoftwareModuleRequest): SoftwareModuleResponse

    /**
     * Deletes a SoftwareModule record by its index.
     *
     * @param id: The index of SoftwareModule record to delete
     * @return: The deleted SoftwareModule instance at index
     */
    fun deleteById(id: Long): SoftwareModuleResponse
}