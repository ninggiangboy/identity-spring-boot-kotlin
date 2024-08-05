package dev.ngb.identity.common.util

import java.util.*

class StringUtil {
    companion object {
        fun toSlug(input: String?): String {
            if (input.isNullOrBlank()) return UUID.randomUUID().toString()
            return input.lowercase()
                .replace("[áàảạãăắằẳẵặâấầẩẫậäåæą]".toRegex(), "a")
                .replace("[óòỏõọôốồổỗộơớờởỡợöœø]".toRegex(), "o")
                .replace("[éèẻẽẹêếềểễệěėëę]".toRegex(), "e")
                .replace("[úùủũụưứừửữự]".toRegex(), "u")
                .replace("[iíìỉĩịïîį]".toRegex(), "i")
                .replace("[ùúüûǘůűūų]".toRegex(), "u")
                .replace("[ßşśšș]".toRegex(), "s")
                .replace("[źžż]".toRegex(), "z")
                .replace("[ýỳỷỹỵÿ]".toRegex(), "y")
                .replace("[ǹńňñ]".toRegex(), "n")
                .replace("[çćč]".toRegex(), "c")
                .replace("[ğǵ]".toRegex(), "g")
                .replace("[ŕř]".toRegex(), "r")
                .replace("[·/_,:;]".toRegex(), "-")
                .replace("[ťț]".toRegex(), "t")
                .replace("ḧ", "h")
                .replace("ẍ", "x")
                .replace("ẃ", "w")
                .replace("ḿ", "m")
                .replace("ṕ", "p")
                .replace("ł", "l")
                .replace("đ", "d")
                .replace("\\s+", "-")
                .replace("&", "-and-")
                .replace("[^\\w\\-]+".toRegex(), "")
                .replace("--+".toRegex(), "-")
                .replace("^-+".toRegex(), "")
                .replace("-+$".toRegex(), "")
        }
    }
}