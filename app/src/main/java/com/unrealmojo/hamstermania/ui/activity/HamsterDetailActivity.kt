package com.unrealmojo.hamstermania.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.squareup.picasso.Picasso
import com.unrealmojo.hamstermania.App
import com.unrealmojo.hamstermania.R
import com.unrealmojo.hamstermania.data.ui.Hamster
import com.unrealmojo.hamstermania.shareHamster
import kotlinx.android.synthetic.main.activity_hamster_detail.*

class HamsterDetailActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context, hamster: Hamster, options: ActivityOptionsCompat?) {
            val intent = Intent(context, HamsterDetailActivity::class.java)
            intent.putExtra("title", hamster.title)
            intent.putExtra("description", hamster.description)
            hamster.image?.also { intent.putExtra("image", hamster.image) }
            if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.startActivity(intent, options.toBundle())
            } else {
                context.startActivity(intent)
            }
        }
    }

    private lateinit var mTitle: String
    private lateinit var mDescription: String
    private var mImage: String? = null

    init {
        App.component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hamster_detail)

        mTitle = intent?.getStringExtra("title") ?: throw RuntimeException("Title was not specified")
        mDescription = intent?.getStringExtra("description") ?: throw RuntimeException("Description was not specified")
        mImage = intent?.getStringExtra("image")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_btn_back)

        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))

        tvTitle.text = mTitle
        tvDescription.text = mDescription
        ivImage.setImageResource(R.drawable.ic_logo)
        mImage?.also {
            Picasso.get().load(it)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(ivImage)
        } ?: ivImage.setImageResource(R.drawable.ic_logo)

        val fabIcon = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_btn_share))
        val wrappedfabIcon = DrawableCompat.wrap(fabIcon)
        DrawableCompat.setTint(wrappedfabIcon, ContextCompat.getColor(this, R.color.black))
        btnShare.setImageDrawable(wrappedfabIcon)
        btnShare.setOnClickListener {
            shareHamster(this@HamsterDetailActivity, Hamster(
                    mTitle,
                    mDescription,
                    mImage
            ))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}