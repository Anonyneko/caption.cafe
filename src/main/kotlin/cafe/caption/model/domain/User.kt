package cafe.caption.model.domain

data class User (
    var id: String = "",
    var username: String,
    var email: String?,
    var password: String?,
    var deleted: Boolean = false
)