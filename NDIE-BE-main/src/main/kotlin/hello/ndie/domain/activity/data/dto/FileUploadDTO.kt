package hello.ndie.domain.activity.data.dto

import org.springframework.web.multipart.MultipartFile

data class FileUploadDTO(
    val file: MultipartFile
)
