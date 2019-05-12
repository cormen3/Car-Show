package hossein.gheisary.carshow.list.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import hossein.gheisary.carshow.list.PostFactory
import hossein.gheisary.carshow.ui.car.CarDataHolder
import hossein.gheisary.carshow.ui.car.manufacture.model.ManufactureRemote
import hossein.gheisary.carshow.ui.car.manufacture.model.ManufactureRepository
import hossein.gheisary.carshow.ui.car.manufacture.paging.ManufactureDataSourceFactory
import hossein.gheisary.carshow.ui.car.mappers.ManufactureMapper
import hossein.gheisary.data.remote.core.NetworkState
import hossein.gheisary.data.remote.core.RestDataSource
import hossein.gheisary.carshow.ui.car.manufacture.ManufactureUiModel
import hossein.gheisary.data.remote.testing.TestScheduler
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.concurrent.Executor
import org.junit.Rule
import org.mockito.Mockito


@RunWith(Parameterized::class)
class ManufactureRepositoryTest(type : Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters()
        fun params() = listOf(0)
    }

    @Suppress("unused")
    @get:Rule // used to make all live data calls sync
    val instantExecutor = InstantTaskExecutorRule()

    private val fakeApi = FakeRestApi()
    private val networkExecutor = Executor { command -> command.run() }
    private val remote = ManufactureRemote(RestDataSource(fakeApi))
    private val manufactureDataSourceFactory = ManufactureDataSourceFactory(remote, TestScheduler(),
        ManufactureMapper(), networkExecutor)

    private val repository = ManufactureRepository(networkExecutor, manufactureDataSourceFactory)
    private val postFactory = PostFactory()

    /**
     * asserts that empty list works fine
     */
    @Test
    fun emptyList() {
        val listing = repository.getData( 10)
        val pagedList = getPagedList(listing)
        MatcherAssert.assertThat(pagedList.size, CoreMatchers.`is`(0))
    }

    /**
     * asserts that a list w/ single item is loaded properly
     */
    @Test
    fun oneItem() {
        val post = postFactory.createManufacture("foo")
        fakeApi.addPost(post)
        val listing = repository.getData( pageSize = 10)
        val remotePagedList = getPagedList(listing)
        assertThat("not equal", remotePagedList.contains(post))
    }

    /**
     * asserts loading a full list in multiple pages
     */
    @Test
    fun verifyCompleteList() {
        val posts = (0..10).map { postFactory.createManufacture("bar") }
        posts.forEach(fakeApi::addPost)
        val listing = repository.getData(pageSize = 3)
        val pagedList = getPagedList(listing)
        assertThat("not contain", posts.containsAll(pagedList))
    }

    /**
     * asserts the failure message when the initial load cannot complete
     */
    @Test
    fun failToLoadInitial() {
        fakeApi.failureMsg = "xxx"
        val listing = repository.getData(pageSize = 3)
        // trigger load
        getPagedList(listing)
        assertThat(getNetworkState(listing), `is`(NetworkState.error("xxx")))
    }


    /**
     * asserts the retry logic when initial load request fails
     */
    @Test
    fun retryInInitialLoad() {
        fakeApi.addPost(postFactory.createManufacture("foo"))
        fakeApi.failureMsg = "xxx"
        val listing = repository.getData(pageSize = 3)
        // trigger load
        val pagedList = getPagedList(listing)
        assertThat(pagedList.size, `is`(0))

        @Suppress("UNCHECKED_CAST")
        val networkObserver = Mockito.mock(Observer::class.java) as Observer<NetworkState>
        listing.networkState.observeForever(networkObserver)
        fakeApi.failureMsg = null
        listing.retry()
        assertThat(pagedList.size, `is`(1 ))
        assertThat(getNetworkState(listing), `is`(NetworkState.LOADED))
        val inOrder = Mockito.inOrder(networkObserver)
        inOrder.verify(networkObserver).onChanged(NetworkState.error("xxx"))
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADED)
        inOrder.verifyNoMoreInteractions()
    }

    /**
     * asserts the retry logic when initial load succeeds but subsequent loads fails
     */
    @Test
    fun retryAfterInitialFails() {
        val posts = (0..10).map { postFactory.createManufacture("bar") }
        posts.forEach(fakeApi::addPost)
        val listing = repository.getData(pageSize = 2)
        val list = getPagedList(listing)
        assertThat("test sanity, we should not load everything",list.size < posts.size, `is`(true))
        assertThat(getNetworkState(listing), `is`(NetworkState.LOADED))
        fakeApi.failureMsg = "fail"
        list.loadAllData()
        assertThat(getNetworkState(listing), `is`(NetworkState.error("fail")))
        fakeApi.failureMsg = null
        listing.retry()
        assertThat(getNetworkState(listing), `is`(NetworkState.LOADED))
        assertThat("not contain", posts.containsAll(list))
    }

    /**
     * extract the latest paged list from the listing
     */
    private fun getPagedList(listing: CarDataHolder<ManufactureUiModel>): PagedList<ManufactureUiModel> {
        val observer = LoggingObserver<PagedList<ManufactureUiModel>>()
        listing.pagedList.observeForever(observer)
        MatcherAssert.assertThat(observer.value, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        return observer.value!!
    }

    /**
     * simple observer that logs the latest value it receives
     */
    private class LoggingObserver<T> : Observer<T> {
        var value : T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }


    private fun <T> PagedList<T>.loadAllData() {
        do {
            val oldSize = this.loadedCount
            this.loadAround(this.size - 1)
        } while (this.size != oldSize)
    }

    /**
     * extract the latest network state from the listing
     */
    private fun getNetworkState(listing: CarDataHolder<ManufactureUiModel>) : NetworkState? {
        val networkObserver = LoggingObserver<NetworkState>()
        listing.networkState.observeForever(networkObserver)
        return networkObserver.value
    }

}