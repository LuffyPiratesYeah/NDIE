package hello.ndie.shared.exception

class ImageUploadException(
    override val message: String = "이미지 업로드에 실패했습니다."
) : RuntimeException(message)