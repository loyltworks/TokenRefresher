
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json
@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "access_token")
    var accessToken: String? = null,
    @Json(name = ".expires")
    var expires: String? = null,
    @Json(name = "expires_in")
    var expiresIn: Int? = null,
    @Json(name = ".issued")
    var issued: String? = null,
    @Json(name = "token_type")
    var tokenType: String? = null,
    @Json(name = "userName")
    var userName: String? = null
)

