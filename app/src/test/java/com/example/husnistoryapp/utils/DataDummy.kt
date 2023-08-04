package com.example.husnistoryapp.utils

import com.example.husnistoryapp.service.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "Cerita $i",
                "Kegabutan Kemarin $i?",
                "goo.gl/$i",
                "2023-07-01",
                0.0,
                0.0
            )
            items.add(story)
        }
        return items
    }
}