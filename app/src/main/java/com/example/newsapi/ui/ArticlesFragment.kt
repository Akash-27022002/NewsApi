package com.example.newsapi.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.MainActivity
import com.example.newsapi.R
import com.example.newsapi.data.Sorting
import com.example.newsapi.databinding.FragmentArticlesBinding
import com.example.newsapi.viewModels.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticlesFragment : Fragment() {
    private lateinit var binding : FragmentArticlesBinding
    private lateinit var activity: MainActivity
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[NewsViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_articles, container, false)
        binding = FragmentArticlesBinding.bind(view)

        /**
         * [ArticlesAdapter] this is the Adapter through which we are able to display the list
         * The Important thing about this [ArticlesAdapter] is very optimized due to the [DiffCallback]
         * and this is not and normal RecyclerView Adapter it is an ListAdapter that can change the ui
         * according to the change in the data doesn't change the whole Ui after submitting the list
         * */

        val adapter = ArticlesAdapter {

            /**
             * THis is simple callback or listener that we make in Article Adapter so
             * whatever the news Article can be tapped we are able to get i and transfer it to the
             * web browser for the further reading of article
             * */
            /**
             * THis is simple callback or listener that we make in Article Adapter so
             * whatever the news Article can be tapped we are able to get i and transfer it to the
             * web browser for the further reading of article
             * */
            /**
             * THis is simple callback or listener that we make in Article Adapter so
             * whatever the news Article can be tapped we are able to get i and transfer it to the
             * web browser for the further reading of article
             * */

            /**
             * THis is simple callback or listener that we make in Article Adapter so
             * whatever the news Article can be tapped we are able to get i and transfer it to the
             * web browser for the further reading of article
             * */
            /**
             * THis is simple callback or listener that we make in Article Adapter so
             * whatever the news Article can be tapped we are able to get i and transfer it to the
             * web browser for the further reading of article
             * */
            /**
             * THis is simple callback or listener that we make in Article Adapter so
             * whatever the news Article can be tapped we are able to get i and transfer it to the
             * web browser for the further reading of article
             * */
            /**
             * THis is simple callback or listener that we make in Article Adapter so
             * whatever the news Article can be tapped we are able to get i and transfer it to the
             * web browser for the further reading of article
             * */
            /**
             * THis is simple callback or listener that we make in Article Adapter so
             * whatever the news Article can be tapped we are able to get i and transfer it to the
             * web browser for the further reading of article
             * */
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(it.url)
            )
            startActivity(urlIntent)
        }

        val articles = binding.articles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
            setHasFixedSize(true)
        }

        viewModel.error.observe(viewLifecycleOwner){
            binding.loadingProgressBar.visibility = View.GONE
            binding.articles.visibility = View.GONE
            binding.errorMessage.visibility = View.VISIBLE
        }

        viewModel.loading.observe(viewLifecycleOwner){isLoading->
            if (isLoading){
                binding.loadingProgressBar.visibility = View.VISIBLE
                binding.articles.visibility = View.GONE
                binding.errorMessage.visibility = View.GONE
            }else{
                binding.loadingProgressBar.visibility = View.GONE
                binding.articles.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.GONE
            }
        }

        /**
         *  [viewModel.sorting] here we observing that if the sorting will change we will sort the
         *  list accordingly and submit it again to the ListAdapter with the selected sorted manner
         *  And we didn't call the Api again for the that we shall call the api and get the data and then
         *  sort it instead of this we get the list from [viewModel] itself.
         * */

        viewModel.sorting.observe(viewLifecycleOwner){sorting->
            binding.errorMessage.visibility = View.GONE
            (articles.adapter as ArticlesAdapter).submitList(activity.shortArticle(sorting))
        }

        /**
         *  In this [viewModel.articles] we are observing that if any changes our from anywhere in the app
         *  with the articles list we will submit it again to the list Adapter with changes in the List
         * */
        viewModel.articles.observe(viewLifecycleOwner){
            (articles.adapter as ArticlesAdapter).submitList(it)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu_sorting, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_to_latest_first -> {
                item.isChecked = true
                viewModel.shortArticle(Sorting.Ace)
                true
            }
            R.id.sort_to_oldest_first -> {
                viewModel.shortArticle(Sorting.Dsce)
                item.isChecked = true
                true
            }
            R.id.refresh ->{
                handleRefresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as MainActivity
    }

    private fun handleRefresh() {
        activity.getArticles()
        viewModel.shortArticle(Sorting.Ace)
    }


    companion object{
        private const val TAG = "ArticlesFragment"
    }


}