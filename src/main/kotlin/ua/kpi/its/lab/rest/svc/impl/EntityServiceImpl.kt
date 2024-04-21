package ua.kpi.its.lab.rest.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.rest.dto.SoftwareModuleResponse
import ua.kpi.its.lab.rest.dto.SoftwareProductsRequest
import ua.kpi.its.lab.rest.dto.SoftwareProductsResponse
import ua.kpi.its.lab.rest.entity.SoftwareModule
import ua.kpi.its.lab.rest.entity.SoftwareProduct
import ua.kpi.its.lab.rest.repo.SoftwareProductRepository
import ua.kpi.its.lab.rest.svc.SoftwareProductService
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class SoftwareProductServiceImpl @Autowired constructor(
    private val repository: SoftwareProductRepository
): SoftwareProductService {
    override fun create(softwareProduct: SoftwareProductsRequest): SoftwareProductsResponse {
        val newSoftwareProduct = SoftwareProduct(
            name = softwareProduct.name,
            developer = softwareProduct.developer,
            version = softwareProduct.version,
            releaseDate = this.stringToDate(softwareProduct.releaseDate),
            distributionSize = softwareProduct.distributionSize,
            bitness = softwareProduct.bitness,
            crossPlatform = softwareProduct.crossPlatform,
            modules = mutableListOf() // No modules added initially
        )
        val savedProduct = repository.save(newSoftwareProduct)
        return softwareProductEntityToDto(savedProduct)
    }

    override fun read(): List<SoftwareProductsResponse> {
        return repository.findAll().map { softwareProductEntityToDto(it) }
    }

    override fun readById(id: Long): SoftwareProductsResponse {
        val softwareProduct = getSoftwareProductById(id)
        return softwareProductEntityToDto(softwareProduct)
    }

    override fun updateById(id: Long, softwareProduct: SoftwareProductsRequest): SoftwareProductsResponse {
        val oldSoftwareProduct = getSoftwareProductById(id)

        oldSoftwareProduct.apply {
            name = softwareProduct.name
            developer = softwareProduct.developer
            version = softwareProduct.version
            releaseDate = this@SoftwareProductServiceImpl.stringToDate(softwareProduct.releaseDate)
            distributionSize = softwareProduct.distributionSize
            bitness = softwareProduct.bitness
            crossPlatform = softwareProduct.crossPlatform
        }

        val savedProduct = repository.save(oldSoftwareProduct)
        return softwareProductEntityToDto(savedProduct)
    }

    override fun deleteById(id: Long): SoftwareProductsResponse {
        val softwareProduct = getSoftwareProductById(id)
        repository.delete(softwareProduct)
        return softwareProductEntityToDto(softwareProduct)
    }

    private fun getSoftwareProductById(id: Long): SoftwareProduct {
        return repository.findById(id).getOrElse {
            throw IllegalArgumentException("Software product not found by id = $id")
        }
    }

    private fun softwareProductEntityToDto(softwareProduct: SoftwareProduct): SoftwareProductsResponse {
        return SoftwareProductsResponse(
            id = softwareProduct.id,
            name = softwareProduct.name,
            developer = softwareProduct.developer,
            version = softwareProduct.version,
            releaseDate = this.dateToString(softwareProduct.releaseDate),
            distributionSize = softwareProduct.distributionSize,
            bitness = softwareProduct.bitness,
            crossPlatform = softwareProduct.crossPlatform,
            modules = softwareProduct.modules.map { moduleEntityToDto(it) }
        )
    }

    private fun moduleEntityToDto(module: SoftwareModule): SoftwareModuleResponse {
        return SoftwareModuleResponse(
            id = module.id,
            description = module.description ?: "",
            author = module.author,
            language = module.language,
            lastEditDate = this.dateToString(module.lastEditDate),
            size = module.size,
            linesOfCode = module.linesOfCode,
            crossPlatform = module.crossPlatform
        )
    }

    private fun dateToString(date: Date): String {
        val instant = date.toInstant()
        val dateTime = instant.atOffset(ZoneOffset.UTC).toLocalDateTime()
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    private fun stringToDate(date: String): Date {
        val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val instant = dateTime.toInstant(ZoneOffset.UTC)
        return Date.from(instant)
    }
}