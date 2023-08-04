package com.example.husnistoryapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.husnistoryapp.model.StoryRepository
import com.example.husnistoryapp.service.ListStoryItem
import com.example.husnistoryapp.ui.story.ListStoryAdapter
import com.example.husnistoryapp.utils.DataDummy
import com.example.husnistoryapp.utils.MainDispatcherRule
import com.example.husnistoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `Berhasil memuat data cerita`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data = StoryPagingSource.snapshot(dummyStory)

        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val vm = HomeViewModel(storyRepository)
        val actualStory = vm.getListStories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_ITEM_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(actualStory) // Memastikan data tidak null
        assertEquals(dummyStory.size, differ.snapshot().size) // Memastikan jumlah data sesuai dengan yang diharapkan
        assertEquals(dummyStory[0], differ.snapshot()[0]!!) // Memastikan data pertama yang dikembalikan sesuai
    }

    @Test
    fun `Ketika tidak ada data cerita`() = runTest {
        val data = PagingData.from(emptyList<ListStoryItem>())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val vm = HomeViewModel(storyRepository)
        val actualStory = vm.getListStories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_ITEM_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size) // Memastikan jumlah data yang dikembalikan nol.
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null
        )
    }

    companion object {
        fun snapshot(story: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(story)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}