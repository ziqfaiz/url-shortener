package com.ziqfaiz.url_shortener.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "url_mapping")
data class UrlMappingK (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    val originalUrl: String="",

    @Column(nullable = false, length = 10, unique = true)
    var shortCode: String="",

    val createdAt: LocalDateTime? = null,
    val expiresAt: LocalDateTime? = null,
    val clickCount: Int = 0,
    var lastAccessedAt: LocalDateTime? = null
)
