package ua.kpi.its.lab.rest.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.rest.dto.SoftwareModuleRequest
import ua.kpi.its.lab.rest.dto.SoftwareModuleResponse
import ua.kpi.its.lab.rest.dto.SoftwareProductsRequest
import ua.kpi.its.lab.rest.dto.SoftwareProductsResponse
import ua.kpi.its.lab.rest.entity.SoftwareModule
import ua.kpi.its.lab.rest.entity.SoftwareProduct
import ua.kpi.its.lab.rest.repo.SoftwareModuleRepository
import ua.kpi.its.lab.rest.repo.SoftwareProductRepository
import ua.kpi.its.lab.rest.svc.SoftwareModuleService
import ua.kpi.its.lab.rest.svc.SoftwareProductService
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.jvm.optionals.getOrElse

@Service
class SoftwareProductServiceImpl @Autowired constructor(
    private val repository: SoftwareProductRepository,
    private val moduleService: SoftwareModuleServiceImpl
): SoftwareProductService {
    override fun create(softwareProduct: SoftwareProductsRequest): SoftwareProductsResponse {
        // Create the software modules and collect them in a list
        val modulesResponse = softwareProduct.modules.map { moduleRequest ->
            moduleService.create(moduleRequest) // Ensure moduleService.create returns SoftwareModuleResponse
        }

        // Convert SoftwareModuleResponse to SoftwareModule
        val modules = modulesResponse.map { moduleResponse ->
            SoftwareModule(
                description = moduleResponse.description,
                author = moduleResponse.author,
                language = moduleResponse.language,
                lastEditDate = stringToDate(moduleResponse.lastEditDate),
                size = moduleResponse.size,
                linesOfCode = moduleResponse.linesOfCode,
                crossPlatform = moduleResponse.crossPlatform
            )
        }

        // Create a new SoftwareProduct entity
        val newSoftwareProduct = SoftwareProduct(
            name = softwareProduct.name,
            developer = softwareProduct.developer,
            version = softwareProduct.version,
            releaseDate = stringToDate(softwareProduct.releaseDate),
            distributionSize = softwareProduct.distributionSize,
            bitness = softwareProduct.bitness,
            crossPlatform = softwareProduct.crossPlatform,
            modules = modules.toMutableList() // Assign the list of modules to the new software product
        )

        // Save the new software product to the repository
        val savedProduct = repository.save(newSoftwareProduct)

        // Convert the saved software product entity to DTO and return it
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

    private fun dateToString(date: LocalDate): String {
        val formatter = DateTimeFormatter.ISO_DATE
        return date.format(formatter)
    }

    private fun stringToDate(date: String): LocalDate {
        val formatter = DateTimeFormatter.ISO_DATE
        return LocalDate.parse(date, formatter)
    }
}

@Service
class SoftwareModuleServiceImpl @Autowired constructor(
    private val repository: SoftwareModuleRepository
): SoftwareModuleService {
    override fun create(softwareModule: SoftwareModuleRequest): SoftwareModuleResponse {
        val newModule = SoftwareModule(
            description = softwareModule.description,
            author = softwareModule.author,
            language = softwareModule.language,
            lastEditDate = stringToDate(softwareModule.lastEditDate),
            size = softwareModule.size,
            linesOfCode = softwareModule.linesOfCode,
            crossPlatform = softwareModule.crossPlatform
        )
        val savedModule = repository.save(newModule)
        return moduleEntityToDto(savedModule)
    }

    override fun read(): List<SoftwareModuleResponse> {
        return repository.findAll().map { moduleEntityToDto(it) }
    }

    override fun readById(id: Long): SoftwareModuleResponse {
        val module = getSoftwareModuleById(id)
        return moduleEntityToDto(module)
    }

    override fun updateById(id: Long, softwareModule: SoftwareModuleRequest): SoftwareModuleResponse {
        val oldModule = getSoftwareModuleById(id)

        oldModule.apply {
            description = softwareModule.description
            author = softwareModule.author
            language = softwareModule.language
            lastEditDate = stringToDate(softwareModule.lastEditDate)
            size = softwareModule.size
            linesOfCode = softwareModule.linesOfCode
            crossPlatform = softwareModule.crossPlatform
        }

        val savedModule = repository.save(oldModule)
        return moduleEntityToDto(savedModule)
    }

    override fun deleteById(id: Long): SoftwareModuleResponse {
        val module = getSoftwareModuleById(id)
        repository.delete(module)
        return moduleEntityToDto(module)
    }

    private fun getSoftwareModuleById(id: Long): SoftwareModule {
        return repository.findById(id).orElseThrow {
            IllegalArgumentException("Software module not found by id = $id")
        }
    }

    private fun moduleEntityToDto(module: SoftwareModule): SoftwareModuleResponse {
        return SoftwareModuleResponse(
            id = module.id,
            description = module.description ?: "",
            author = module.author,
            language = module.language,
            lastEditDate = dateToString(module.lastEditDate),
            size = module.size,
            linesOfCode = module.linesOfCode,
            crossPlatform = module.crossPlatform
        )
    }

    private fun dateToString(date: LocalDate): String {
        val formatter = DateTimeFormatter.ISO_DATE
        return date.format(formatter)
    }

    private fun stringToDate(date: String): LocalDate {
        val formatter = DateTimeFormatter.ISO_DATE
        return LocalDate.parse(date, formatter)
    }
}