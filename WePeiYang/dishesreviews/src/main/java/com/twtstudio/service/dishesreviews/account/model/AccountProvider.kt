package com.twtstudio.service.dishesreviews.account.model

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twtstudio.service.dishesreviews.model.DishesAccountBean
import com.twtstudio.service.dishesreviews.model.DishesService

object AccountProvider {
    lateinit var dishesBeanLiveData: RefreshableLiveData<DishesAccountBean, CacheIndicator>
    fun getAccount(userId: String): RefreshableLiveData<DishesAccountBean, CacheIndicator> {
        val accountBeanLocalData = Cache.hawk<DishesAccountBean>("DishesAccount")
        val accountBeanRemote = Cache.from {
            DishesService.getAccount(userId)
        }.map(CommonBody<DishesAccountBean>::data)
        dishesBeanLiveData = RefreshableLiveData.use(accountBeanLocalData, accountBeanRemote)
        return dishesBeanLiveData
    }
}