package com.alt.karman_pc.smsmaket.helperFiles

import me.everything.providers.android.telephony.Sms
import java.util.*

class Dialog (var address: String, var name: String, var avatar: String = "none") {

    var lastMessageDate: Long = 0
    var messages = emptyArray<RealmMessage>()
    var lastBody = ""

    fun addMessage(sms: RealmMessage) {
        if (messages.isEmpty()) {
            lastMessageDate = sms.getDate()
            lastBody = sms.body
        }
        messages += sms
    }

    fun search(text: String): Array<RealmMessage> {
        var result = emptyArray<RealmMessage>()
        for (sms in messages)
            if (sms.address != "date" && sms.body.contains(text, true))
                result += sms
        return result
    }

    fun generateStatistic(): Statistic {
        val countMessages: Int
        var countOutSms = 0
        var countInSms = 0
        var time = emptyArray<Long>()
        var chastota = emptyArray<Int>()


        for (sms in messages)
            if (sms.type != null) {
                if (sms.type == "INBOX")
                    countInSms++
                else if (sms.address != "date")
                    countOutSms++
            }

        var index = -1
        for (sms in messages) {
            if (sms.address == "date") {
                index++
                chastota += 0
                time += sms.sentDate
            }
            else
                try {
                    chastota[index]++
                } catch (e: Exception) {
                    index++
                    chastota += 0
                    time += sms.getDate()
                }
        }
        countMessages = countInSms + countOutSms
        return Statistic(countMessages, countInSms, countOutSms, chastota, time)
    }

    private fun isNotOneDay(first: Long, second: Long): Boolean {
        val one1 = Date(first)
        val two1 = Date(second)

        if (one1.year == two1.year
            && one1.month == two1.month
            && one1.day == two1.day)
            return false
        return true
    }

    private fun createDateSms(date: Long): RealmMessage {
        val dateSms = RealmMessage()
        dateSms.address = "date"
        dateSms.sentDate = date
        return dateSms
    }

    fun formatDialog() {
        messages.reverse()
        var lastDate: Long = 0
        var dialogArray = emptyArray<RealmMessage>()
        for (sms in messages) {
            if (isNotOneDay(sms.getDate(), lastDate) && Date(sms.getDate()).after(Date(lastDate))) {
                dialogArray += createDateSms(sms.getDate())
                lastDate = sms.getDate()
            }
            dialogArray += sms
        }
        messages = dialogArray
    }

    class Statistic(
        val allcount: Int,
        val inboxCount: Int,
        val outboxCount: Int,
        val chastota: Array<Int>,
        val time: Array<Long>
    ) {
        fun getProcInbox() = inboxCount.toFloat() / allcount
        fun getProcOutbox() = outboxCount.toFloat() / allcount
    }
}