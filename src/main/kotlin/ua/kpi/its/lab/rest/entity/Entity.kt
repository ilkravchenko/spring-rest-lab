package ua.kpi.its.lab.rest.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "software_products")
class SoftwareProduct(
    @Column
    var name: String,

    @Column
    var developer: String,

    @Column
    var version: String,

    @Column
    var releaseDate: Date,

    @Column
    var distributionSize: Long,

    @Column
    var bitness: Int,

    @Column
    var crossPlatform: Boolean,

    @OneToMany(mappedBy = "softwareProduct", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var modules: MutableList<SoftwareModule> = mutableListOf()
) : Comparable<SoftwareProduct> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = -1

    override fun compareTo(other: SoftwareProduct): Int {
        val nameComparison = name.compareTo(other.name)
        return if (nameComparison != 0) nameComparison else id.compareTo(other.id)
    }

    override fun toString(): String {
        return "SoftwareProduct(name=$name, version=$version, releaseDate=$releaseDate, developer=$developer, modules=$modules)"
    }
}

@Entity
@Table(name = "software_modules")
class SoftwareModule(
    @Column
    var description: String? = null, // Nullable to handle potential null values

    @Column
    var author: String,

    @Column
    var language: String,

    @Column
    var lastEditDate: Date,

    @Column
    var size: Long,

    @Column
    var linesOfCode: Int,

    @Column
    var crossPlatform: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "software_product_id")
    var softwareProduct: SoftwareProduct? = null
) : Comparable<SoftwareModule> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = -1

    override fun compareTo(other: SoftwareModule): Int {
        val descriptionComparison = description?.compareTo(other.description ?: "") ?: 0 // Handle null values
        return if (descriptionComparison != 0) descriptionComparison else id.compareTo(other.id)
    }

    override fun toString(): String {
        return "SoftwareModule(description=$description, author=$author, language=$language)"
    }
}