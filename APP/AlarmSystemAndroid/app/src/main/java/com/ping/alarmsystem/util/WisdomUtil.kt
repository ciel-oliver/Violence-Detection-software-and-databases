package com.ping.alarmsystem.util

import java.util.*

/**
 * @ClassName: WisdomUtil
 * @Description: 名言获取
 */
object WisdomUtil {
    private val wisdoms = arrayOf("痛天枉之幽厄，惜坠学之昏愚。乃博采群经。", "做人一身正气，为医一尘不染。", "医之为道大矣，医之为任重矣。", "正气存内，邪不可干。", "食能以时，身必无灾。", "师古人之意，而不泥古人之方，乃为善学古人。", "医者仁心", "明天的希望，让我们忘了今天的痛苦","什么事都只有活着才能干，所以平安最重要。","种一颗种子，收获一份果实。","奉献爱心，快乐你我他。","志愿传递文明，服务成就精彩。","爱心无限，大爱无边。","被需要是一种幸福。","用心点燃希望，用爱撒播人间。","您的善举可能挽救的就是一条生命，一个家庭。\n")
    private var index = 0

    private var random: Random? = null

    /**
     * 获取名人名言
     */
    fun getWisdoms(): String {
        if (random == null) random = Random()

        fun getIndex(): Int {
            val randomIndex = random!!.nextInt(wisdoms.size - 1)
            if (randomIndex == index) {
                getIndex()
            }
            return randomIndex
        }

        index = getIndex()

        return wisdoms[index]
    }

}