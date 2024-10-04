//package com.hadiubaidillah.user.service
//
//import com.hadiubaidillah.user.entity.UserAttribute
//import com.hadiubaidillah.user.repository.UserAttributesRepository
//import org.jetbrains.exposed.sql.transactions.transaction
//
//class UserAttributesService(
//    private val userAttributesRepository: UserAttributesRepository
//) {
//
//    fun getAll(): List<UserAttribute> = userAttributesRepository.getAll()
//
//    fun getById(id: String): UserAttribute = transaction() {
//        var userAttribute: UserAttribute? = userAttributesRepository.getById(id)
//        if(userAttribute == null) {
//            userAttribute = userAttributesRepository.add(UserAttribute(id))
//        }
//        return@transaction userAttribute
//    }
//}
