package cafe.caption.utils

import org.mindrot.jbcrypt.BCrypt

fun encryptPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun checkPassword(password: String, hash: String): Boolean {
    return BCrypt.checkpw(password, hash)
}