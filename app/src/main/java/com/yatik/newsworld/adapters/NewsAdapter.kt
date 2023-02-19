package com.yatik.newsworld.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yatik.newsworld.R
import com.yatik.newsworld.models.Article
import com.yatik.newsworld.utils.DateFormat

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallbacks = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            // Id is null for articles received from api
            // So use url instead
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallbacks)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val article = differ.currentList[position]
        val itemView = holder.itemView

        val articleImage = itemView.findViewById<ImageView>(R.id.ivArticleImage)
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val source = itemView.findViewById<TextView>(R.id.tvSource)
        val publishedAt = itemView.findViewById<TextView>(R.id.tvPublishedAt)

        val circularProgressDrawable = CircularProgressDrawable(itemView.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        itemView.apply {
            Glide.with(this)
                .load(article.urlToImage)
                .transform(RoundedCorners(35))
                .placeholder(circularProgressDrawable)
                .into(articleImage)

            val trimmedTitle = article.title.toString().replace("- ${article.source?.name}", "")
            title.text = trimmedTitle
            source.text = article.source?.name
            publishedAt.text = DateFormat.convertDate(article.publishedAt)

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

}