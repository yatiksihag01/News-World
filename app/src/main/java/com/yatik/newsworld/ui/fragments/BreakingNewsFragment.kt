package com.yatik.newsworld.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yatik.newsworld.R
import com.yatik.newsworld.adapters.NewsAdapter
import com.yatik.newsworld.databinding.FragmentBreakingNewsBinding
import com.yatik.newsworld.ui.NewsViewModel
import com.yatik.newsworld.utils.Constants
import com.yatik.newsworld.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.yatik.newsworld.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpRefreshBehaviour()
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    binding.connectionErrorLayout.root.visibility = View.GONE
                    response.data?.let { newsResponse ->

                        if (isRefreshed) {
                            isRefreshed = false
                            //TODO: breakingNewsPage++
                        }
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        // For round off => +1, Last page is empty => +1
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage) {
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    binding.connectionErrorLayout.root.visibility = View.VISIBLE
                    response.message?.let { message ->
                        Log.e("BrakingNewsFragment", "An error occurred: $message")
                        binding.connectionErrorLayout.ivConnect.setImageResource(R.drawable.connection_error_img)
                        binding.connectionErrorLayout.tvErrorMessage.text = message
                        binding.connectionErrorLayout.bvRetry.setOnClickListener {
                            viewModel.getBreakingNews(Constants.tempCountryCode)
                        }
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                    binding.connectionErrorLayout.root.visibility = View.GONE
                }
            }
        }
    }

    private var isRefreshed = false
    private fun setUpRefreshBehaviour() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            isRefreshed = true
            // Free API contains only limited articles.
            // Therefore, set breakingNewsPage = 1
            viewModel.breakingNewsPage = 1
            viewModel.clearBreakingNewsList()
            viewModel.getBreakingNews(Constants.tempCountryCode)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews(Constants.tempCountryCode)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}