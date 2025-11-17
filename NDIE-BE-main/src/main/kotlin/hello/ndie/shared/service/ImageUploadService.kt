package hello.ndie.shared.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.IOException
import java.util.*

@Service
class ImageUploadService(
    @Value("\${aws.s3.access-key}")
    private val accessKey: String,

    @Value("\${aws.s3.secret-key}")
    private val secretKey: String,

    @Value("\${aws.s3.region}")
    private val region: String,

    @Value("\${aws.s3.bucket}")
    private val bucketName: String
) {

    private fun getS3Client(): S3Client {
        val awsCredentials = AwsBasicCredentials.create(accessKey, secretKey)
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build()
    }

    /**
     * 이미지 업로드 실행
     * @param multipartFile 업로드할 파일
     * @return S3 URL
     * @throws IOException 파일 읽기 실패 시
     */
    @Throws(IOException::class)
    fun upload(multipartFile: MultipartFile): String? {
        if (multipartFile.isEmpty) {
            return null
        }

        val uuid = UUID.randomUUID().toString()
        val contentType = multipartFile.contentType
        val s3Client = getS3Client()

        try {
            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uuid)
                .contentType(contentType)
                .build()

            s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(multipartFile.inputStream, multipartFile.size)
            )

            return "https://$bucketName.s3.$region.amazonaws.com/$uuid"
        } finally {
            s3Client.close()
        }
    }

    /**
     * 이미지 삭제
     * @param imageUrl 전체 S3 URL
     * @throws IOException S3 삭제 실패 시
     */
    @Throws(IOException::class)
    fun delete(imageUrl: String) {
        val fileName = extractFileNameFromUrl(imageUrl)
        val s3Client = getS3Client()

        try {
            val deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build()

            s3Client.deleteObject(deleteObjectRequest)
        } catch (e: Exception) {
            throw IllegalArgumentException("이미지를 찾을 수 없거나 삭제에 실패했습니다: $imageUrl", e)
        } finally {
            s3Client.close()
        }
    }

    /**
     * S3 URL에서 파일 이름만 추출
     * @param url 전체 이미지 URL
     * @return 파일 이름
     */
    private fun extractFileNameFromUrl(url: String): String {
        return url.substringAfterLast("/")
    }
}
