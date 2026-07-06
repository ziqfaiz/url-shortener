package com.ziqfaiz.url_shortener.repository

import com.ziqfaiz.url_shortener.entity.UrlMappingK
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UrlMappingRepositoryK {
    fun findByShortCode(shortCode: String): Optional<UrlMappingK>
}